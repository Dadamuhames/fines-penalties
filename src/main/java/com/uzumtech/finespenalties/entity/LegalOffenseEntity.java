package com.uzumtech.finespenalties.entity;

import com.uzumtech.finespenalties.constant.enums.OffenceStatus;
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
@Table(name = "legal_offenses", indexes = {@Index(columnList = "id"), @Index(columnList = "offenderPinfl"), @Index(columnList = "id, offenderPinfl")})
public class LegalOffenseEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspector_id", updatable = false, foreignKey = @ForeignKey(name = "fk_inspector"))
    private InspectorEntity inspector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_article_id", foreignKey = @ForeignKey(name = "fk_codeArticle"))
    private CodeArticleEntity codeArticle;

    @Column(nullable = false, length = 14)
    private String offenderPinfl;

    @Column(nullable = false)
    private String offenderFullName;

    @Column(nullable = false)
    private String offenseLocation;

    private String offenderExplanation;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OffenceStatus status;

    @Column(nullable = false, unique = true)
    private String protocolNumber;

    @Column(nullable = false)
    private OffsetDateTime offenseDateTime;
}
