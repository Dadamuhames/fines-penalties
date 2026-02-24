package com.uzumtech.finespenalties.service.impl.user;

import com.uzumtech.finespenalties.component.adapter.GcpAdapter;
import com.uzumtech.finespenalties.dto.response.GcpResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.mapper.UserMapper;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.user.UserHelperService;
import com.uzumtech.finespenalties.service.intr.user.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterServiceImpl implements UserRegisterService {
    private final UserRepository userRepository;
    private final UserHelperService userHelperService;
    private final UserMapper userMapper;
    private final GcpAdapter gcpAdapter;

    @Override
    public UserEntity findUserByPinflOrRegister(String pinfl) {
        UserEntity user = userRepository.findByPinfl(pinfl).orElse(null);

        if (user == null) user = registerUserByPinfl(pinfl);

        return user;
    }

    @Override
    public UserEntity registerUserByPinfl(String pinfl) {
        GcpResponse gcpResponse = gcpAdapter.fetchUserInfoByPinfl(pinfl);

        UserEntity newUser = userMapper.gcpResponseToUserEntity(gcpResponse);

        return userHelperService.saveUser(newUser);
    }
}
