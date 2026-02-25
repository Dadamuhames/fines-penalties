package com.uzumtech.finespenalties.service.impl.user;

import com.uzumtech.finespenalties.dto.request.UserSetPasswordRequest;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.user.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setUserPassword(UserEntity user, UserSetPasswordRequest request) {

        String passwordEncoded = passwordEncoder.encode(request.password());

        user.setPassword(passwordEncoded);

        userRepository.save(user);
    }
}
