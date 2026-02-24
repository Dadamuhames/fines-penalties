package com.uzumtech.finespenalties.service.impl.user;

import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.user.UserHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserHelperServiceImpl implements UserHelperService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
