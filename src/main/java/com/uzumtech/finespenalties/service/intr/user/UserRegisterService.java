package com.uzumtech.finespenalties.service.intr.user;

import com.uzumtech.finespenalties.entity.UserEntity;

public interface UserRegisterService {

    UserEntity findUserByPinflOrRegister(String pinfl);

    UserEntity registerUserByPinfl(String pinfl);

}
