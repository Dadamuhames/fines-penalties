package com.uzumtech.finespenalties.entity;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "legal_offenses", indexes = {@Index(columnList = "inspector_id"), @Index(columnList = "user_id"), @Index(columnList = "id, user_id"), @Index(columnList = "id, status")})
public class LegalOffenseEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspector_id", updatable = false, foreignKey = @ForeignKey(name = "fk_legal_offense_inspector"), nullable = false)
    private InspectorEntity inspector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_article_id", foreignKey = @ForeignKey(name = "fk_legal_offense_code_article"), nullable = false)
    private CodeArticleEntity codeArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, foreignKey = @ForeignKey(name = "fk_legal_offense_user"), nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String offenseLocation;

    private String offenderExplanation;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OffenseStatus status;

    @Column(nullable = false, unique = true)
    private String protocolNumber;

    // external values
    private Long courtOffenseId;

    private String courtCaseNumber;

    @Column(nullable = false)
    private OffsetDateTime offenseDateTime;

    @OneToOne(mappedBy = "offense", fetch = FetchType.LAZY)
    private PenaltyEntity penalty;
}
