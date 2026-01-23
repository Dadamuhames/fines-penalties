package com.uzumtech.finespenalties.entity;

import com.uzumtech.finespenalties.constant.enums.OffenceStatus;
import com.uzumtech.finespenalties.constant.enums.PenaltyStatus;
import com.uzumtech.finespenalties.constant.enums.PenaltyType;
import com.uzumtech.finespenalties.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "penalties", indexes = {@Index(columnList = "offense_id")})
public class PenaltyEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offense_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private LegalOffenseEntity offense;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PenaltyType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PenaltyStatus status;

    @Column(nullable = false)
    private Long bhmAmountAtTime;

    @Column(nullable = false)
    private BigDecimal bhmMultiplier;

    @Column(nullable = false)
    private OffsetDateTime dueDate;

    @Column(nullable = false)
    private String courtDecisionText;

    @Column(nullable = false)
    private OffsetDateTime courtDecisionDate;

    @Column(nullable = false)
    private String courtCaseNumber;
}
