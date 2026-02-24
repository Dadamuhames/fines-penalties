package com.uzumtech.finespenalties.entity;

import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.entity.base.BaseDeactivatableEntity;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", indexes = {@Index(columnList = "phone"), @Index(columnList = "pinfl")})
public class UserEntity extends BaseDeactivatableEntity implements CustomUserDetails {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true, length = 14)
    private String pinfl;

    private String password;

    @Override
    public Role getUserRole() {
        return Role.USER;
    }

    @Override
    public String getUsername() {
        return phone;
    }
}
