package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.entity.InspectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InspectorRepository extends JpaRepository<InspectorEntity, Long> {

    Optional<InspectorEntity> findByPersonnelNumber(String personnelNumber);

}
