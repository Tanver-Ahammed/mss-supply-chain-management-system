package com.therap.supply.chain.user.repository;

import com.therap.supply.chain.user.entities.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
