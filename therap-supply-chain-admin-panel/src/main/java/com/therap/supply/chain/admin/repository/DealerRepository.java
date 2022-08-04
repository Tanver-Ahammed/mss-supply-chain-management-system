package com.therap.supply.chain.admin.repository;

import com.therap.supply.chain.admin.entities.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface DealerRepository extends JpaRepository<Dealer, Long> {

    Dealer findDealerByEmail(String email);

    Dealer findDealerByContact(String contact);

    Dealer findDealerByEmailOrContact(String email, String contact);

}
