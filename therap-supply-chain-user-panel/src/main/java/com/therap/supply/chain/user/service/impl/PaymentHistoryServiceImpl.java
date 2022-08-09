package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.config.AppConstants;
import com.therap.supply.chain.user.dto.PaymentHistoryDTO;
import com.therap.supply.chain.user.entities.PaymentHistory;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;
import com.therap.supply.chain.user.repository.PaymentHistoryRepository;
import com.therap.supply.chain.user.service.PaymentHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaymentHistoryDTO savePayment(Long requisitionId, PaymentHistoryDTO paymentHistoryDTO) {
        PaymentHistory paymentHistory = this.modelMapper.map(paymentHistoryDTO, PaymentHistory.class);
        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        paymentHistory.setRequisition(requisition);
        paymentHistory.setDate(new Date());
        paymentHistory.setIsApproveByAccountManager(AppConstants.pause);
        paymentHistory = this.paymentHistoryRepository.save(paymentHistory);
        return this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class);
    }

    @Override
    public List<PaymentHistoryDTO> getAllPaymentByRequisitionId(Long requisitionId) {
        return this.paymentHistoryRepository
                .findByRequisition(this.requisitionService.getRequisition(requisitionId))
                .stream()
                .map(this::paymentHistoryToPaymentHistoryDTO)
                .sorted(Comparator.comparingLong(PaymentHistoryDTO::getId))
                .collect(Collectors.toList());
    }


    // PaymentHistory to PaymentHistoryDTO
    public PaymentHistoryDTO paymentHistoryToPaymentHistoryDTO(PaymentHistory paymentHistory) {
        return this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class);
    }

}
