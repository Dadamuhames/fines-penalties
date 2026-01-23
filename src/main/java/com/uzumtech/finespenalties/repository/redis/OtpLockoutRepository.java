package com.uzumtech.finespenalties.repository.redis;

import com.uzumtech.finespenalties.entity.redis.OtpLockoutEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OtpLockoutRepository extends CrudRepository<OtpLockoutEntity, Long> {

    Optional<OtpLockoutEntity> findByPhone(String phone);

}
