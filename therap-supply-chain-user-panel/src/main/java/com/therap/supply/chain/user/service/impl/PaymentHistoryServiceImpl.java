package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.dto.PaymentHistoryDTO;
import com.therap.supply.chain.user.entities.PaymentHistory;
import com.therap.supply.chain.user.repository.PaymentHistoryRepository;
import com.therap.supply.chain.user.service.PaymentHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    // PaymentHistory to PaymentHistoryDTO
    public PaymentHistoryDTO PaymentHistoryToPaymentHistoryDTO(PaymentHistory paymentHistory) {
        return this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class);
    }

}
