package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface LegalOffenceRepository extends JpaRepository<LegalOffenseEntity, Long> {

    Page<LegalOffenseEntity> findByInspectorId(Long inspectorId, Pageable pageable);

    @EntityGraph(attributePaths = {"inspector", "codeArticle"})
    Page<LegalOffenseEntity> findByOffenderPinfl(String pinfl, Pageable pageable);

    Optional<LegalOffenseEntity> findByIdAndOffenderPinfl(Long id, String pinfl);

    int countByCreatedAt(OffsetDateTime dateTime);
}
