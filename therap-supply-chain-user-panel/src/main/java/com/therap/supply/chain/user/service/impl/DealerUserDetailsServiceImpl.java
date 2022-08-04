package com.therap.supply.chain.user.service.impl;


import com.therap.supply.chain.user.entities.Dealer;
import com.therap.supply.chain.user.model.DealerUserDetails;
import com.therap.supply.chain.user.repository.DealerRepository;
import com.therap.supply.chain.user.service.DealerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class DealerUserDetailsServiceImpl implements DealerUserDetailsService {

    @Autowired
    private DealerRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Dealer dealer = this.authorityRepository.findDealerByEmail(username);
        if (dealer == null)
            throw new UsernameNotFoundException("Authority details not found for this user: " + username);
        return new DealerUserDetails(dealer);
    }
}
