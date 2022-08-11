package com.therap.supply.chain.admin.repository;

import com.therap.supply.chain.admin.entities.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
}
