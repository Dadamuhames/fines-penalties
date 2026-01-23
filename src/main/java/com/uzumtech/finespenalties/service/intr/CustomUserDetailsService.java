package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.exception.ApplicationException;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(final String login) throws ApplicationException;

    Role getSupportedRole();
}
