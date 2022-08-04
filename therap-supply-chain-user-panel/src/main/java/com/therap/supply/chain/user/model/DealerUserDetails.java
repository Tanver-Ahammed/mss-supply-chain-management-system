package com.therap.supply.chain.user.model;

import com.therap.supply.chain.user.config.AppConstants;
import com.therap.supply.chain.user.entities.Dealer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DealerUserDetails implements UserDetails {

    private final Dealer dealer;

    public DealerUserDetails(Dealer dealer) {
        this.dealer = dealer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.dealer.getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.dealer.getPassword();
    }

    @Override
    public String getUsername() {
        return this.dealer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(this.dealer.getIsActiveByDealerApprover(), AppConstants.active);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.dealer.isActivate();
    }

}
