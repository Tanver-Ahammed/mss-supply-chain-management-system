package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;

import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface RequisitionService {

    RequisitionDTO getLastRequisitionByDealer(Long dealerId);

    Double getTotalPriceRequisition(Requisition requisition);

}
