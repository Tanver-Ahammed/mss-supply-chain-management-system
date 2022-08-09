package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.PaymentHistoryDTO;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.entities.PaymentHistory;
import com.therap.supply.chain.admin.entities.Requisition;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.PaymentHistoryRepository;
import com.therap.supply.chain.admin.repository.RequisitionRepository;
import com.therap.supply.chain.admin.service.AccountRequisitionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountRequisitionServiceImpl implements AccountRequisitionService {

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private PaymentHistoryServiceImpl paymentHistoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<RequisitionDTO> getAllRequisitionByForAccount() {
        return this.requisitionRepository
                .findByIsApproveByInventoryManagerAndIsApproveByAccountManager(AppConstants.accept, AppConstants.pause)
                .stream()
                .map(requisition -> this.requisitionService.requisitionToRequisitionDTO(requisition))
                .sorted(Comparator.comparingLong(RequisitionDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isApproveRequisitionStatusByAccount(Long requisitionId, String accountStatus) {
        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        if (accountStatus.equalsIgnoreCase(AppConstants.accept))
            requisition.setIsApproveByAccountManager(AppConstants.accept);
        else if (accountStatus.equalsIgnoreCase(AppConstants.reject)) {
            requisition.setIsApproveByAccountManager(AppConstants.reject);
            requisition = this.requisitionService.recoveryStockFromRequisition(requisition);
        }
        this.requisitionRepository.save(requisition);
        return true;
    }

    @Override
    public Boolean isApprovePaymentHistoryStatusByAccount(Long paymentHistoryId, String paymentHistoryStatus) {
        PaymentHistory paymentHistory = this.paymentHistoryService.getPaymentHistory(paymentHistoryId);
        Requisition requisition = paymentHistory.getRequisition();
        if (paymentHistory.getIsApproveByAccountManager().equalsIgnoreCase(AppConstants.accept))
            return true;
        if (paymentHistoryStatus.equalsIgnoreCase(AppConstants.accept)) {
            paymentHistory.setIsApproveByAccountManager(AppConstants.accept);
            requisition.setPaidAmount(requisition.getPaidAmount() + paymentHistory.getAmount());
            if (requisition.getTotalAmountPrice() <= requisition.getPaidAmount())
                requisition.setPaid(true);
        } else if (paymentHistoryStatus.equalsIgnoreCase(AppConstants.reject))
            paymentHistory.setIsApproveByAccountManager(AppConstants.reject);
        this.requisitionRepository.save(requisition);
        return true;
    }

}
