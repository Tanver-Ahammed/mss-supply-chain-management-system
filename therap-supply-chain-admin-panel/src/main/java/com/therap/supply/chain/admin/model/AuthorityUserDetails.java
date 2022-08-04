package com.therap.supply.chain.admin.model;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.entities.Authority;
import com.therap.supply.chain.admin.entities.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AuthorityUserDetails implements UserDetails {

    private final Authority authority;

    public AuthorityUserDetails(Authority authority) {
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // if a user has multiple role
        for (Role role : this.authority.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
//        authorities.add(new SimpleGrantedAuthority(this.authority.getRole()));
        System.err.println(authorities);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.authority.getPassword();
    }

    @Override
    public String getUsername() {
        return this.authority.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(this.authority.getActivity(), AppConstants.active);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.authority.getIsEnable();
    }
}
