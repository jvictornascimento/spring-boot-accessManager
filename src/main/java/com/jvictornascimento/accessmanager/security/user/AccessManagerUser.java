package com.jvictornascimento.accessmanager.security.user;

import com.jvictornascimento.accessmanager.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class AccessManagerUser implements UserDetails {
    private Long id;
    private String email;
    private String password;

    public static AccessManagerUser buildAccessManagerUser(User user) {
        return new AccessManagerUser(
                user.getId(),
                user.getEmail(),
                user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
