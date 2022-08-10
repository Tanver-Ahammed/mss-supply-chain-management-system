package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.RequisitionDTO;

import javax.swing.*;
import java.util.List;

public interface DeliveryRequisitionService {

    List<RequisitionDTO> getAllRequisitionByForDelivery();

    Boolean isApproveRequisitionDeliveryForSendMail(Long requisitionId, String inventoryStatus);

    Boolean verificationRequisitionForDelivery(Long requisitionId, String verificationCode);

}
