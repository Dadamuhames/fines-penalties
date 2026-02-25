package com.uzumtech.finespenalties.service.impl.user;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.dto.response.CodeArticleDto;
import com.uzumtech.finespenalties.dto.response.InspectorDto;
import com.uzumtech.finespenalties.dto.response.LegalOffenseDetailResponse;
import com.uzumtech.finespenalties.dto.response.LegalOffenseResponse;
import com.uzumtech.finespenalties.dto.response.PenaltyDetailResponse;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.exception.OffenseNotFoundException;
import com.uzumtech.finespenalties.mapper.LegalOffenseMapper;
import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserLegalOffenseServiceImplTest {

    @Mock
    private LegalOffenseRepository legalOffenseRepository;
    @Mock
    private LegalOffenseMapper legalOffenseMapper;

    @InjectMocks
    private UserLegalOffenseServiceImpl userLegalOffenseService;

    private static final Long USER_ID = 1L;
    private static final Long OFFENSE_ID = 10L;
    private static final String PROTOCOL_NUMBER = "FP-20260202-0001";

    private UserEntity user;
    private LegalOffenseEntity offenseEntity;
    private LegalOffenseResponse offenseResponse;
    private LegalOffenseDetailResponse detailResponse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize User
        user = new UserEntity();
        user.setId(USER_ID);

        // Initialize Entity
        offenseEntity = new LegalOffenseEntity();
        offenseEntity.setId(OFFENSE_ID);

        // Initialize Responses
        offenseResponse = new LegalOffenseResponse(
            OFFENSE_ID,
            PROTOCOL_NUMBER,
            OffenseStatus.NEW,
            mock(InspectorDto.class),
            mock(CodeArticleDto.class),
            "Tashkent",
            OffsetDateTime.now()
        );

        detailResponse = new LegalOffenseDetailResponse(
            OFFENSE_ID,
            PROTOCOL_NUMBER,
            mock(InspectorDto.class),
            mock(CodeArticleDto.class),
            "Description",
            "Tashkent",
            "Explanation",
            "CS-20260202-0001",
            OffenseStatus.NEW,
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            mock(PenaltyDetailResponse.class)
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void list_ShouldReturnMappedPage_WhenOffensesExist() {
        // Arrange
        LegalOffenseEntity entity = new LegalOffenseEntity();
        Page<LegalOffenseEntity> entityPage = new PageImpl<>(List.of(entity));

        when(legalOffenseRepository.findByUserId(USER_ID, pageable)).thenReturn(entityPage);
        when(legalOffenseMapper.entityToResponse(entity)).thenReturn(offenseResponse);

        // Act
        Page<LegalOffenseResponse> result = userLegalOffenseService.list(user, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(legalOffenseRepository).findByUserId(USER_ID, pageable);
        verify(legalOffenseMapper).entityToResponse(entity);
    }

    @Test
    void getOne_ShouldReturnDetailResponse_WhenOffenseExistsForUser() {
        // Arrange
        LegalOffenseEntity entity = new LegalOffenseEntity();
        entity.setId(OFFENSE_ID);

        when(legalOffenseRepository.findByIdAndUserId(OFFENSE_ID, USER_ID)).thenReturn(Optional.of(entity));
        when(legalOffenseMapper.entityToDetailResponse(entity)).thenReturn(detailResponse);

        // Act
        LegalOffenseDetailResponse result = userLegalOffenseService.getOne(OFFENSE_ID, user);

        // Assert
        assertNotNull(result);
        verify(legalOffenseRepository).findByIdAndUserId(OFFENSE_ID, USER_ID);
    }

    @Test
    void getOne_ShouldThrowOffenseNotFoundException_WhenOffenseDoesNotExistOrBelongsToOtherUser() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(USER_ID);

        when(legalOffenseRepository.findByIdAndUserId(OFFENSE_ID, USER_ID))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OffenseNotFoundException.class,
            () -> userLegalOffenseService.getOne(OFFENSE_ID, user));

        verifyNoInteractions(legalOffenseMapper);
    }
}