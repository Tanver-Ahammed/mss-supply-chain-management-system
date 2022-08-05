package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.entities.Dealer;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;

import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface RequisitionService {

    List<RequisitionDTO> getAllRequisitionByDealer(Long dealerId);

    RequisitionDTO getRequisitionById(Long requisitionId);

    RequisitionDTO getLastRequisitionByDealer(Long dealerId);

    Double getTotalPriceRequisition(Requisition requisition);

}
