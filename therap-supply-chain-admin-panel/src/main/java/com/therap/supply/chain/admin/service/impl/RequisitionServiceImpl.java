package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.dto.*;
import com.therap.supply.chain.admin.entities.Requisition;
import com.therap.supply.chain.admin.entities.RequisitionProductHistory;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.RequisitionRepository;
import com.therap.supply.chain.admin.service.RequisitionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequisitionServiceImpl implements RequisitionService {

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RequisitionDTO getSingleRequisitionById(Long requisitionId) {
        return this.requisitionToRequisitionDTO(this.getRequisition(requisitionId));
    }

    // get requisition
    public Requisition getRequisition(Long requisitionId) {
        return this.requisitionRepository.findById(requisitionId).orElseThrow(() ->
                new ResourceNotFoundException("Requisition", "id", requisitionId));
    }

    // get total product a requisition
    public Long getTotalProductRequisition(RequisitionDTO requisitionDTO) {
        return requisitionDTO
                .getRequisitionProductHistoryDTOS()
                .stream()
                .filter(rphDTO -> !rphDTO.isDeleted())
                .mapToLong(RequisitionProductHistoryDTO::getQuantity)
                .sum();
    }

    // requisition to requisitionDTO
    public RequisitionDTO requisitionToRequisitionDTO(Requisition requisition) {
        requisition.setRequisitionProductHistories(requisition
                .getRequisitionProductHistories()
                .stream()
                .filter(rph -> !rph.isDeleted())
                .sorted(Comparator.comparingLong(RequisitionProductHistory::getId))
                .collect(Collectors.toList())
        );

        RequisitionDTO requisitionDTO = this.modelMapper.map(requisition, RequisitionDTO.class);
        requisitionDTO.setDealerDTO(this.modelMapper.map(requisition.getDealer(), DealerDTO.class));
        requisitionDTO.getDealerDTO().setPassword(null);
        requisitionDTO.getDealerDTO().setVerificationCode(null);

        List<RequisitionProductHistoryDTO> requisitionProductHistoryDTOS = new ArrayList<>();
        for (RequisitionProductHistory requisitionProductHistory : requisition.getRequisitionProductHistories()) {
            RequisitionProductHistoryDTO rphDTO = this.modelMapper.map(requisitionProductHistory, RequisitionProductHistoryDTO.class);
            ProductDTO productDTO = this.productService.productToProductDTO(requisitionProductHistory.getProduct());
            rphDTO.setProductDTO(productDTO);
            requisitionProductHistoryDTOS.add(rphDTO);
        }
        requisitionDTO.setRequisitionProductHistoryDTOS(requisitionProductHistoryDTOS);

        requisitionDTO.setPaymentHistoryDTOS(requisition.getPaymentHistories().stream()
                .map(paymentHistory -> this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class))
                .sorted(Comparator.comparingLong(PaymentHistoryDTO::getId))
                .collect(Collectors.toList()));
        return requisitionDTO;
    }

}
