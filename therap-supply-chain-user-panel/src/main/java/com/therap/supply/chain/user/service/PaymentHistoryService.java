package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.PaymentHistoryDTO;

import java.util.List;

public interface PaymentHistoryService {

    PaymentHistoryDTO savePayment(Long requisitionId, PaymentHistoryDTO paymentHistoryDTO);

    List<PaymentHistoryDTO> getAllPaymentByRequisitionId(Long requisitionId);

}
