package com.therap.supply.chain.user.repository;

import com.therap.supply.chain.user.entities.Dealer;
import com.therap.supply.chain.user.entities.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface RequisitionRepository extends JpaRepository<Requisition, Long> {

    List<Requisition> findByDealer(Dealer dealer);

    Requisition getByDealerAndIsSubmittedByDealer(Dealer dealer, Boolean isSubmittedDealer);

    Requisition getByDealerAndIsApproveByInventoryManager(Dealer dealer, String isApproveInventory);

}
