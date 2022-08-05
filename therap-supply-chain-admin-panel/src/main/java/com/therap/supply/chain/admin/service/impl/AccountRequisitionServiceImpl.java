package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.entities.Requisition;
import com.therap.supply.chain.admin.repository.RequisitionRepository;
import com.therap.supply.chain.admin.service.AccountRequisitionService;
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
        else if (accountStatus.equalsIgnoreCase(AppConstants.reject))
            requisition.setIsApproveByAccountManager(AppConstants.reject);
        this.requisitionRepository.save(requisition);
        return true;
    }


}
