package com.uzumtech.finespenalties.service.impl.auth.userdetails;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.exception.UserNotFoundException;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailService implements CustomUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UserNotFoundException {
        return userRepository.findByPhone(login).orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_INVALID_CODE));
    }

    @Override
    public Role getSupportedRole() {
        return Role.USER;
    }
}
