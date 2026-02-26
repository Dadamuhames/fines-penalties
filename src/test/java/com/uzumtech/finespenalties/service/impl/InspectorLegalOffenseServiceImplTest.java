package com.uzumtech.finespenalties.service.impl;

import com.uzumtech.finespenalties.component.kafka.publisher.OffenseEventPublisher;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.dto.event.OffenseEvent;
import com.uzumtech.finespenalties.dto.request.LegalOffenceRegisterRequest;
import com.uzumtech.finespenalties.dto.response.InspectorLegalOffenseResponse;
import com.uzumtech.finespenalties.entity.CodeArticleEntity;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.exception.CodeArticleIdInvalidException;
import com.uzumtech.finespenalties.mapper.LegalOffenseMapper;
import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
import com.uzumtech.finespenalties.service.intr.CodeArticleService;
import com.uzumtech.finespenalties.service.intr.OffenseService;
import com.uzumtech.finespenalties.service.intr.user.UserRegisterService;
import com.uzumtech.finespenalties.utils.ProtocolNumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InspectorLegalOffenseServiceImplTest {


    @Mock
    private LegalOffenseRepository legalOffenseRepository;
    @Mock
    private UserRegisterService userRegisterService;
    @Mock
    private ProtocolNumberUtils protocolNumberUtils;
    @Mock
    private LegalOffenseMapper legalOffenseMapper;
    @Mock
    private OffenseEventPublisher offenseEventPublisher;
    @Mock
    private CodeArticleService codeArticleService;
    @Mock
    private OffenseService offenseService;

    @InjectMocks
    private InspectorLegalOffenseServiceImpl inspectorLegalOffenseService;

    private InspectorEntity inspector;
    private LegalOffenceRegisterRequest registerRequest;
    private UserEntity user;
    private CodeArticleEntity codeArticle;
    private LegalOffenseEntity offense;
    private InspectorLegalOffenseResponse response;

    private final String PROTOCOL_NUMBER = "FP-20260225-0001";

    @BeforeEach
    void setUp() {
        inspector = new InspectorEntity();
        inspector.setId(10L);

        user = new UserEntity();
        user.setFullName("John Doe");

        codeArticle = new CodeArticleEntity();

        registerRequest = new LegalOffenceRegisterRequest(
            1L,
            "12345678901234",
            "Tashkent, Amir Temur str.",
            "Speeding violation",
            OffsetDateTime.now(),
            "Explanation"
        );

        offense = new LegalOffenseEntity();
        offense.setId(100L);
        offense.setProtocolNumber(PROTOCOL_NUMBER);

        response = new InspectorLegalOffenseResponse(
            100L,
            "FP-20260225-0001",
            OffenseStatus.NEW,
            "Sherlock Holmes",
            "221B Baker Street",
            OffsetDateTime.now()
        );
    }

    @Test
    void findAllForInspector_ShouldReturnMappedPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<LegalOffenseEntity> entities = List.of(offense);
        Page<LegalOffenseEntity> entityPage = new PageImpl<>(entities, pageable, 1);

        when(legalOffenseRepository.findByInspectorId(inspector.getId(), pageable)).thenReturn(entityPage);
        when(legalOffenseMapper.entityToInspectorResponse(any(LegalOffenseEntity.class))).thenReturn(response);

        // Act
        Page<InspectorLegalOffenseResponse> result = inspectorLegalOffenseService.findAllForInspector(inspector, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().get(0));
        verify(legalOffenseRepository).findByInspectorId(inspector.getId(), pageable);
    }

    @Test
    void registerLegalOffense_ShouldExecuteFullWorkflowSuccessfully() {
        // Arrange
        OffenseEvent expectedEvent = new OffenseEvent(offense.getId());

        when(userRegisterService.findUserByPinflOrRegister(registerRequest.offenderPinfl())).thenReturn(user);
        when(codeArticleService.findByIdOrThrowBadRequestException(registerRequest.codeArticleId())).thenReturn(codeArticle);
        when(protocolNumberUtils.generateProtocolNumber()).thenReturn(PROTOCOL_NUMBER);
        when(legalOffenseMapper.requestToEntity(registerRequest, inspector, user, codeArticle, PROTOCOL_NUMBER)).thenReturn(offense);
        when(offenseService.saveOffense(offense)).thenReturn(offense);
        when(legalOffenseMapper.entityToInspectorResponse(offense, user.getFullName())).thenReturn(response);

        // Act
        InspectorLegalOffenseResponse result = inspectorLegalOffenseService.registerLegalOffense(inspector, registerRequest);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);
        assertEquals(PROTOCOL_NUMBER, offense.getProtocolNumber());

        verify(offenseService).saveOffense(offense);
        verify(offenseEventPublisher).publish(expectedEvent);
        verify(protocolNumberUtils).generateProtocolNumber();
    }

    @Test
    void registerLegalOffense_ShouldThrowException_WhenCodeArticleNotFound() {
        // Arrange
        when(userRegisterService.findUserByPinflOrRegister(anyString())).thenReturn(user);
        when(codeArticleService.findByIdOrThrowBadRequestException(registerRequest.codeArticleId()))
            .thenThrow(new CodeArticleIdInvalidException(ErrorCode.CODE_ARTICLE_NOT_FOUND_CODE));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            inspectorLegalOffenseService.registerLegalOffense(inspector, registerRequest)
        );

        verifyNoInteractions(protocolNumberUtils, offenseService, offenseEventPublisher);
    }

}