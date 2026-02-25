package com.uzumtech.finespenalties.service.impl.user;


import com.uzumtech.finespenalties.component.adapter.GcpAdapter;
import com.uzumtech.finespenalties.dto.response.GcpResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.mapper.UserMapper;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.user.UserHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserHelperService userHelperService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private GcpAdapter gcpAdapter;

    @InjectMocks
    private UserRegisterServiceImpl userRegisterService;

    private static final String TEST_PINFL = "12345678901234";
    private static final String TEST_PHONE = "998901234567";

    private UserEntity existingUser;
    private GcpResponse gcpResponse;

    @BeforeEach
    void setUp() {
        existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setPinfl(TEST_PINFL);
        existingUser.setPhone(TEST_PHONE);

        gcpResponse = mock(GcpResponse.class);
    }

    @Test
    void findUserByPinflOrRegister_ShouldReturnExistingUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByPinfl(TEST_PINFL)).thenReturn(Optional.of(existingUser));

        // Act
        UserEntity result = userRegisterService.findUserByPinflOrRegister(TEST_PINFL);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_PINFL, result.getPinfl());

        verify(userRepository).findByPinfl(TEST_PINFL);
        verifyNoInteractions(gcpAdapter);
    }

    @Test
    void findUserByPinflOrRegister_ShouldRegisterUser_WhenUserDoesNotExist() {
        // Arrange
        UserEntity newUser = new UserEntity();
        newUser.setPinfl(TEST_PINFL);

        when(userRepository.findByPinfl(TEST_PINFL)).thenReturn(Optional.empty());
        when(gcpAdapter.fetchUserInfoByPinfl(TEST_PINFL)).thenReturn(gcpResponse);
        when(userMapper.gcpResponseToUserEntity(gcpResponse)).thenReturn(newUser);
        when(userHelperService.saveUser(newUser)).thenReturn(newUser);

        // Act
        UserEntity result = userRegisterService.findUserByPinflOrRegister(TEST_PINFL);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_PINFL, result.getPinfl());
        verify(gcpAdapter).fetchUserInfoByPinfl(TEST_PINFL);
        verify(userHelperService).saveUser(newUser);
    }

    @Test
    void registerUserByPinfl_ShouldFetchMapAndSave_Always() {
        // Arrange
        UserEntity newUser = new UserEntity();
        newUser.setPinfl(TEST_PINFL);

        when(gcpAdapter.fetchUserInfoByPinfl(TEST_PINFL)).thenReturn(gcpResponse);
        when(userMapper.gcpResponseToUserEntity(gcpResponse)).thenReturn(newUser);
        when(userHelperService.saveUser(newUser)).thenReturn(newUser);

        // Act
        UserEntity result = userRegisterService.registerUserByPinfl(TEST_PINFL);

        // Assert
        assertNotNull(result);
        verify(gcpAdapter).fetchUserInfoByPinfl(TEST_PINFL);
        verify(userMapper).gcpResponseToUserEntity(gcpResponse);
        verify(userHelperService).saveUser(newUser);
    }
}