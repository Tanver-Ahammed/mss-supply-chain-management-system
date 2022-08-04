package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.dto.ProductDTO;
import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.dto.RequisitionProductHistoryDTO;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;
import com.therap.supply.chain.user.exception.ResourceNotFoundException;
import com.therap.supply.chain.user.repository.RequisitionProductHistoryRepository;
import com.therap.supply.chain.user.service.RequisitionProductHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class RequisitionProductHistoryServiceImpl implements RequisitionProductHistoryService {

    @Autowired
    private RequisitionProductHistoryRepository requisitionProductHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    // get RequisitionProductHistory
    public RequisitionProductHistory getRequisitionProductHistory(Long rphId) {
        return this.requisitionProductHistoryRepository.findById(rphId).orElseThrow(() ->
                new ResourceNotFoundException("RequisitionProductHistory", "id", rphId));
    }

    // RequisitionProductHistory toRequisitionProductHistoryDTO
    public RequisitionProductHistoryDTO requisitionProductHistoryToDTO(RequisitionProductHistory requisitionProductHistory) {
        RequisitionProductHistoryDTO requisitionProductHistoryDTO = this.modelMapper
                .map(requisitionProductHistory, RequisitionProductHistoryDTO.class);
        requisitionProductHistoryDTO.setRequisitionDTO(this.modelMapper
                .map(requisitionProductHistory.getRequisition(), RequisitionDTO.class));
        requisitionProductHistoryDTO.setProductDTO(this.modelMapper
                .map(requisitionProductHistory.getProduct(), ProductDTO.class));
        return requisitionProductHistoryDTO;
    }

}
