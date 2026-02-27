package com.uzumtech.finespenalties.repository;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface LegalOffenseRepository extends JpaRepository<LegalOffenseEntity, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<LegalOffenseEntity> findByInspectorId(Long inspectorId, Pageable pageable);

    @EntityGraph(attributePaths = {"inspector", "codeArticle"})
    Page<LegalOffenseEntity> findByUserId(Long userId, Pageable pageable);

    Optional<LegalOffenseEntity> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT o FROM LegalOffenseEntity o " +
        "LEFT JOIN FETCH o.user " +
        "LEFT JOIN FETCH o.codeArticle " +
        "WHERE o.id = ?1 AND o.status = ?2")
    Optional<LegalOffenseEntity> findByIdAndStatus(Long id, OffenseStatus status);

    @Modifying
    @Query("update LegalOffenseEntity o set o.courtOffenseId = :courtOffenseId, o.courtCaseNumber = :courtCaseNumber, o.status = 'SENT_TO_COURT', o.updatedAt = CURRENT_TIMESTAMP WHERE o.id = :offenseId ")
    void updateWithCourtData(@Param("offenseId") Long offenseId, @Param("courtOffenseId") Long courtOffenseId, @Param("courtCaseNumber") String courtCaseNumber);


    @Modifying
    @Query("update LegalOffenseEntity o set o.status = :status, o.updatedAt = CURRENT_TIMESTAMP WHERE o.id = :offenseId")
    void updateStatus(@Param("offenseId") Long offenseId, @Param("status") OffenseStatus status);
}
