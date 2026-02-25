package com.uzumtech.finespenalties.service.impl.court;

import com.uzumtech.finespenalties.component.adapter.CourtAdapter;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.court.CourtOffenseRegistrationRequest;
import com.uzumtech.finespenalties.dto.response.court.CourtOffenseResponse;
import com.uzumtech.finespenalties.exception.kafka.nontransients.CourtTokenNotExistsException;
import com.uzumtech.finespenalties.service.intr.court.CourtAuthService;
import com.uzumtech.finespenalties.service.intr.court.CourtHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CourtOffenseRegistrationServiceImplTest {


    @Mock
    private CourtAuthService courtAuthService;

    @Mock
    private CourtHelperService courtHelperService;

    @Mock
    private CourtAdapter courtAdapter;

    @InjectMocks
    private CourtOffenseRegistrationServiceImpl registrationService;

    private final Long OFFENSE_ID = 1L;
    private CourtOffenseRegistrationRequest mockRequest;
    private CourtOffenseResponse mockResponse;

    private final String COURT_CASE_NUMBER = "C-20260216-0001";

    @BeforeEach
    void setUp() {
        mockRequest = new CourtOffenseRegistrationRequest(
            OFFENSE_ID,
            "12345678901234",
            "Tashkent, Chilanzar",
            "FP-20260216-0001",
            "I didn't see the sign",
            "Speeding in restricted zone",
            OffsetDateTime.now(),
            "Article 123.1"
        );

        mockResponse = new CourtOffenseResponse(
            100L,
            200L,
            COURT_CASE_NUMBER,
            OffsetDateTime.now()
        );
    }

    @Test
    void sendOffenseToCourt_ShouldExecuteFullWorkflowSuccessfully() {

        String ACCESS_TOKEN = "valid-token";

        // Arrange
        when(courtHelperService.getRegistrationRequest(OFFENSE_ID)).thenReturn(mockRequest);
        when(courtAuthService.getAuthToken()).thenReturn(ACCESS_TOKEN);
        when(courtAdapter.sendOffenseRegistrationRequest(ACCESS_TOKEN, mockRequest)).thenReturn(mockResponse);

        // Act
        CourtOffenseResponse result = registrationService.sendOffenseToCourt(OFFENSE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(COURT_CASE_NUMBER, result.courtCaseNumber());
        assertEquals(200L, result.externalId());

        verify(courtHelperService).getRegistrationRequest(OFFENSE_ID);
        verify(courtAuthService).getAuthToken();
        verify(courtAdapter).sendOffenseRegistrationRequest(ACCESS_TOKEN, mockRequest);
    }

    @Test
    void sendOffenseToCourt_ShouldThrowException_WhenAuthFails() {
        // Arrange
        when(courtHelperService.getRegistrationRequest(OFFENSE_ID)).thenReturn(mockRequest);
        when(courtAuthService.getAuthToken()).thenThrow(new CourtTokenNotExistsException(ErrorCode.COURT_TOKEN_NOT_EXISTS_CODE));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            registrationService.sendOffenseToCourt(OFFENSE_ID)
        );

        verifyNoInteractions(courtAdapter);
    }

}