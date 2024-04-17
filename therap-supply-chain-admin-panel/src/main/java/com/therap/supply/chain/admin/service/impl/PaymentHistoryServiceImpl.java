package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.AttachmentDTO;
import com.therap.supply.chain.admin.dto.PaymentHistoryDTO;
import com.therap.supply.chain.admin.entities.PaymentHistory;
import com.therap.supply.chain.admin.entities.Requisition;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.PaymentHistoryRepository;
import com.therap.supply.chain.admin.service.PaymentHistoryService;
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
        paymentHistory.setDate(new Date());
        paymentHistory.setRequisition(requisition);
        paymentHistory.setIsApproveByAccountManager(AppConstants.accept);
        requisition.setPaidAmount(requisition.getPaidAmount() + paymentHistoryDTO.getAmount());
        if (requisition.getTotalAmountPrice() <= requisition.getPaidAmount())
            requisition.setPaid(true);
        paymentHistory = this.paymentHistoryRepository.save(paymentHistory);
        return this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class);
    }

    @Override
    public List<PaymentHistoryDTO> getAllPaymentForApprove() {
        return this.paymentHistoryRepository
                .findByIsApproveByAccountManager(AppConstants.pause)
                .stream()
                .map(paymentHistory -> this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class))
                .sorted(Comparator.comparingLong(PaymentHistoryDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentHistoryDTO getPaymentHistoryById(Long paymentHistoryId) {
        PaymentHistory paymentHistory = this.getPaymentHistory(paymentHistoryId);
        PaymentHistoryDTO paymentHistoryDTO = this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class);
        if (paymentHistory.getAttachment() != null)
            paymentHistoryDTO.setAttachmentDTO(this.modelMapper.map(paymentHistory.getAttachment(), AttachmentDTO.class));
        return paymentHistoryDTO;
    }

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
