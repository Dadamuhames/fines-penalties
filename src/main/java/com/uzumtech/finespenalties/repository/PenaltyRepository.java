package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.entity.PenaltyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<PenaltyEntity, Long> {
}
