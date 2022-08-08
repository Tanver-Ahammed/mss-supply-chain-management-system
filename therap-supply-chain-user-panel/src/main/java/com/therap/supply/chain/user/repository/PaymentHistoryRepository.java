package com.therap.supply.chain.user.repository;

import com.therap.supply.chain.user.entities.PaymentHistory;
import com.therap.supply.chain.user.entities.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    List<PaymentHistory> findByRequisition(Requisition requisition);

}
