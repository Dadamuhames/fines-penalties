package com.uzumtech.finespenalties.service.impl.auth.userdetails;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.exception.RoleNotSupportedException;
import com.uzumtech.finespenalties.service.intr.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDetailDispatcher {
    private final Map<Role, CustomUserDetailsService> customUserDetailServices;

    public UserDetails loadUserByLoginAndRole(String login, Role role)
        throws RoleNotSupportedException {

        CustomUserDetailsService userDetailsService = customUserDetailServices.get(role);

        if (userDetailsService == null) {
            throw new RoleNotSupportedException(ErrorCode.ROLE_NOT_SUPPORTED_CODE);
        }

        return userDetailsService.loadUserByUsername(login);
    }
}
