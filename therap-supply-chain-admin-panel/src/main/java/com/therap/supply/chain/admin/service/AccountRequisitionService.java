package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.PaymentHistoryDTO;
import com.therap.supply.chain.admin.dto.RequisitionDTO;

import java.util.List;

public interface AccountRequisitionService {

    List<RequisitionDTO> getAllRequisitionByForAccount();

    List<RequisitionDTO> getAllRestPaymentRequisitionByForAccount();

    Boolean isApproveRequisitionStatusByAccount(Long requisitionId, String accountStatus);

    Boolean isApprovePaymentHistoryStatusByAccount(Long paymentHistoryId, String paymentHistoryStatus);

}
