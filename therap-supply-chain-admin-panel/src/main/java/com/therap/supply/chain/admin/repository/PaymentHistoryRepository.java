package com.therap.supply.chain.admin.repository;

import com.therap.supply.chain.admin.entities.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
