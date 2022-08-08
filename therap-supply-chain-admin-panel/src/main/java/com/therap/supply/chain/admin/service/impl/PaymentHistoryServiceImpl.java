package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.dto.PaymentHistoryDTO;
import com.therap.supply.chain.admin.entities.PaymentHistory;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.PaymentHistoryRepository;
import com.therap.supply.chain.admin.service.PaymentHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    // get payment history
    public PaymentHistory getPaymentHistory(Long paymentHistoryId) {
        return this.paymentHistoryRepository.findById(paymentHistoryId).orElseThrow(() ->
                new ResourceNotFoundException("paymentHistoryId", "id", paymentHistoryId));
    }

    // PaymentHistory to PaymentHistoryDTO
    public PaymentHistoryDTO PaymentHistoryToPaymentHistoryDTO(PaymentHistory paymentHistory) {
        return this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class);
    }

}
