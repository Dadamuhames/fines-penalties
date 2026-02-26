package com.uzumtech.finespenalties.entity;

import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens", indexes = {@Index(columnList = "token")})
public class RefreshTokenEntity extends BaseEntity {

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "user_role")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Role userRole;
}
