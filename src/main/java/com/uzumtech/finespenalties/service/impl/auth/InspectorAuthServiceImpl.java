package com.uzumtech.finespenalties.service.impl.auth;


import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.InspectorLoginRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.exception.InspectorNotFoundException;
import com.uzumtech.finespenalties.exception.PasswordInvalidException;
import com.uzumtech.finespenalties.repository.InspectorRepository;
import com.uzumtech.finespenalties.service.intr.InspectorAuthService;
import com.uzumtech.finespenalties.service.intr.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InspectorAuthServiceImpl implements InspectorAuthService {
    private final InspectorRepository inspectorRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public TokenResponse login(final InspectorLoginRequest request) {
        InspectorEntity agency = inspectorRepository.findByPersonnelNumber(request.personnelNumber()).orElseThrow(() -> new InspectorNotFoundException(ErrorCode.LOGIN_INVALID_CODE));

        if (!passwordEncoder.matches(request.password(), agency.getPassword())) {
            throw new PasswordInvalidException(ErrorCode.PASSWORD_INVALID_CODE);
        }

        return tokenService.createPair(agency);
    }
}
