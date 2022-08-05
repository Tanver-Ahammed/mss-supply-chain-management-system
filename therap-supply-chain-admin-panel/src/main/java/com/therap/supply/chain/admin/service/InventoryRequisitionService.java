package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.RequisitionDTO;

import java.util.List;

public interface InventoryRequisitionService {

    List<RequisitionDTO> getAllRequisitionByForInventory();

    Boolean isApproveRequisitionStatusByInventory(Long requisitionId, String inventoryStatus);
}
