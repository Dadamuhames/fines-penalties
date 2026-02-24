package com.uzumtech.finespenalties.service.impl.auth.userdetails;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.exception.InspectorNotFoundException;
import com.uzumtech.finespenalties.repository.InspectorRepository;
import com.uzumtech.finespenalties.service.intr.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InspectorDetailService implements CustomUserDetailsService {
    private final InspectorRepository inspectorRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws InspectorNotFoundException {
        return inspectorRepository
            .findByPersonnelNumber(login)
            .orElseThrow(() -> new InspectorNotFoundException(ErrorCode.INSPECTOR_INVALID_CODE));
    }

    @Override
    public Role getSupportedRole() {
        return Role.INSPECTOR;
    }
}
