package com.uzumtech.finespenalties.service.impl.user;

import com.uzumtech.finespenalties.dto.request.UserSetPasswordRequest;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setUserPassword(final UserEntity user, final UserSetPasswordRequest request) {

        String passwordEncoded = passwordEncoder.encode(request.password());

        user.setPassword(passwordEncoded);

        userRepository.save(user);
    }
}
