package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.RequisitionDTO;

import java.util.List;

public interface AccountRequisitionService {

    public List<RequisitionDTO> getAllRequisitionByForAccount();

    public Boolean isApproveRequisitionStatusByAccount(Long requisitionId, String accountStatus);

}
