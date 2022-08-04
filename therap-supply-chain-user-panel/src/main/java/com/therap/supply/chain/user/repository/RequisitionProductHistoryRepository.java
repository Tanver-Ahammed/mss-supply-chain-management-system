package com.therap.supply.chain.user.repository;

import com.therap.supply.chain.user.entities.Product;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequisitionProductHistoryRepository extends JpaRepository<RequisitionProductHistory, Long> {

    RequisitionProductHistory getByRequisitionAndProduct(Requisition requisition, Product product);

}
