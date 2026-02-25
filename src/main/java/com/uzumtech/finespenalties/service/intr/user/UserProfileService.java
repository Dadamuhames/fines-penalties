package com.uzumtech.finespenalties.service.intr.user;

import com.uzumtech.finespenalties.dto.request.UserSetPasswordRequest;
import com.uzumtech.finespenalties.entity.UserEntity;

public interface UserProfileService {
    void setUserPassword(UserEntity user, final UserSetPasswordRequest request);
}
