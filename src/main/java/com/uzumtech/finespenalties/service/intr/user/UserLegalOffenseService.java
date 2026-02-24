package com.uzumtech.finespenalties.service.intr.user;

import com.uzumtech.finespenalties.dto.response.LegalOffenseDetailResponse;
import com.uzumtech.finespenalties.dto.response.LegalOffenseResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserLegalOffenseService {

    Page<LegalOffenseResponse> list(UserEntity user, Pageable pageable);


    LegalOffenseDetailResponse getOne(Long id, UserEntity user);
}
