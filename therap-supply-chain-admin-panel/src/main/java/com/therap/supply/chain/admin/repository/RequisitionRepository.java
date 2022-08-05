package com.therap.supply.chain.admin.repository;


import com.therap.supply.chain.admin.entities.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface RequisitionRepository extends JpaRepository<Requisition, Long> {

    List<Requisition> findByIsSubmittedByDealerAndIsApproveByInventoryManager(boolean isSubmittedDealer, String isInventory);

    List<Requisition> findByIsApproveByInventoryManagerAndIsApproveByAccountManager(String isInventory, String isAccount);

}
