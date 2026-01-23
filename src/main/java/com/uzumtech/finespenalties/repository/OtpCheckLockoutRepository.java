package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.entity.redis.OtpCheckLockoutEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OtpCheckLockoutRepository extends CrudRepository<OtpCheckLockoutEntity, Long> {

    Optional<OtpCheckLockoutEntity> findByPhone(String phone);

}
