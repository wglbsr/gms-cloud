package com.dyny.auth.config;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Auther: lane
 * @Date: 2019-02-28 17:36
 * @Description:
 * @Version 1.0.0
 */
public class BaseUserDetail implements UserDetails, CredentialsContainer {

    private final BaseUser baseUser;
    private final org.springframework.security.core.userdetails.User user;

    public BaseUserDetail(BaseUser baseUser, User user) {
        this.baseUser = baseUser;
        this.user = user;
    }


    @Override
    public void eraseCredentials() {
        user.eraseCredentials();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }
}
