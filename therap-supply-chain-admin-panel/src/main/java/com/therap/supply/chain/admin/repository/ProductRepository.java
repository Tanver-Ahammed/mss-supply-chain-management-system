package com.therap.supply.chain.admin.repository;

import com.therap.supply.chain.admin.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

public interface ProductRepository extends JpaRepository<Product, Long> {
}
