package com.therap.supply.chain.admin.repository;

import com.therap.supply.chain.admin.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findAuthoritiesByEmail(String email);

    Authority findAuthoritiesByContact(String contact);

    Authority findAuthoritiesByEmailOrContact(String email, String contact);

}
