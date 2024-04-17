package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.PaymentHistoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PaymentHistoryService {

    PaymentHistoryDTO savePayment(Long requisitionId, PaymentHistoryDTO paymentHistoryDTO, MultipartFile paymentImage) throws IOException;

    List<PaymentHistoryDTO> getAllPaymentByRequisitionId(Long requisitionId);

}
