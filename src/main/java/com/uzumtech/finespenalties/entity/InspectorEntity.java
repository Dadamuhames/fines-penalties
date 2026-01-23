package com.uzumtech.finespenalties.entity;

import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.entity.base.BaseDeactivatableEntity;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inspectors", indexes = @Index(columnList = "personnelNumber"))
public class InspectorEntity extends BaseDeactivatableEntity implements CustomUserDetails {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String personnelNumber;

    @Column(nullable = false, unique = true, length = 14)
    private String pinfl;

    @Column(nullable = false)
    private String password;

    private LocalDate dateOfBirth;

    @Override
    public Role getUserRole() {
        return Role.INSPECTOR;
    }

    @Override
    public String getUsername() {
        return personnelNumber;
    }
}
