package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.RequisitionDTO;

import java.util.List;

public interface RequisitionService {

    RequisitionDTO getSingleRequisitionById(Long requisitionId);

    List<RequisitionDTO> getAllRequisitionByDealerId(Long dealerId);

}
