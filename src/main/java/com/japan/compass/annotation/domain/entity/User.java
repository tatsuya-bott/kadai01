package com.japan.compass.annotation.domain.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User implements UserDetails {

    @Getter
    private final int id;
    @Getter
    @NonNull
    private final String mail;
    @NonNull
    private final String password;
    @NonNull
    private final String roles;
    @Getter
    @NonNull
    private final LocalDateTime created;
    @Getter
    @NonNull
    private final LocalDateTime lastLogined;
    private final Boolean enabled;

    public static final String RAW_USER_PASSWORD = "f17ed356-71e1-4494-b5ba-954d8d151623";
    public static final String USER_PASSWORD = "$2a$10$oCj5iwYAVJ78fm8VkrOnMOG3caMrMl0t94Wc/CM8bC0DAM867gyPK";

    // spring securityの仕様で、ROLE_のprefixをつけたカンマ区切りの文字列で登録されている必要がある
    public static String rolesString(Role ...roles) {
        return Arrays.stream(roles)
                .map(v -> "ROLE_" + v.name())
                .collect(Collectors.joining(","));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
