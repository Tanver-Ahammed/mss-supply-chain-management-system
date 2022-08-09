package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.PaymentHistoryDTO;

import java.util.List;

public interface PaymentHistoryService {

    PaymentHistoryDTO savePayment(Long requisitionId, PaymentHistoryDTO paymentHistoryDTO);

    List<PaymentHistoryDTO> getAllPaymentForApprove();

    PaymentHistoryDTO getPaymentHistoryById(Long paymentHistoryId);

}
