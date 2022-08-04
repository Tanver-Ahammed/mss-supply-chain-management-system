package com.therap.supply.chain.user.repository;

import com.therap.supply.chain.user.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

public interface ProductRepository extends JpaRepository<Product, Long> {
}
