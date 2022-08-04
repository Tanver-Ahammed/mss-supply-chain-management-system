package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.entities.Authority;
import com.therap.supply.chain.admin.model.AuthorityUserDetails;
import com.therap.supply.chain.admin.repository.AuthorityRepository;
import com.therap.supply.chain.admin.service.AuthorityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@Service
public class AuthorityUserDetailsServiceImpl implements AuthorityUserDetailsService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authority authority = this.authorityRepository.findAuthoritiesByEmail(username);
        if (authority == null)
            throw new UsernameNotFoundException("Authority details not found for this user: " + username);
        return new AuthorityUserDetails(authority);
    }
}
