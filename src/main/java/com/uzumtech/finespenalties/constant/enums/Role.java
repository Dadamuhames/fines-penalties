package com.uzumtech.finespenalties.constant.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"), INSPECTOR("ROLE_INSPECTOR");

    final String role;

    Role(String role) {
        this.role = role;
    }
}
