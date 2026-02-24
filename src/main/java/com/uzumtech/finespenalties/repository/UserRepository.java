package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByPhone(String phone);

    Optional<UserEntity> findByPinfl(String pinfl);
}
