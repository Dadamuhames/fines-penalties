package com.uzumtech.finespenalties.entity.base;

import com.uzumtech.finespenalties.constant.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public interface CustomUserDetails extends UserDetails {
    Role getUserRole();

    default Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getUserRole().getRole()));
    }
}
