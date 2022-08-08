package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.PaymentHistoryDTO;
import com.therap.supply.chain.admin.dto.RequisitionDTO;

import java.util.List;

public interface AccountRequisitionService {

    List<RequisitionDTO> getAllRequisitionByForAccount();

    Boolean isApproveRequisitionStatusByAccount(Long requisitionId, String accountStatus);

    List<PaymentHistoryDTO> getAllPaymentForApprove();

    PaymentHistoryDTO getPaymentHistoryById(Long paymentHistoryId);

    Boolean isApprovePaymentHistoryStatusByAccount(Long paymentHistoryId, String paymentHistoryStatus);

}
