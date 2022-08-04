package com.therap.supply.chain.user.repository;

import com.therap.supply.chain.user.entities.Product;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisitionProductHistoryRepository extends JpaRepository<RequisitionProductHistory, Long> {

    RequisitionProductHistory getByRequisitionAndProductAndIsDeleted(Requisition requisition, Product product, boolean isDeleted);

    List<RequisitionProductHistory> getByIsDeleted(boolean isDeleted);

}
