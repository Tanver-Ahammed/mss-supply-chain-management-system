package com.therap.supply.chain.admin.repository;


import com.therap.supply.chain.admin.entities.Dealer;
import com.therap.supply.chain.admin.entities.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface RequisitionRepository extends JpaRepository<Requisition, Long> {

    List<Requisition> findByDealer(Dealer dealer);

    List<Requisition> findByIsSubmittedByDealerAndIsApproveByInventoryManager(boolean isSubmittedDealer, String isInventory);

    List<Requisition> findByIsApproveByInventoryManagerAndIsApproveByAccountManager(String isInventory, String isAccount);

    List<Requisition> findByIsApproveByAccountManagerAndIsDelivered(String isAccount, String isDelivered);

}
