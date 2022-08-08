package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.dto.*;
import com.therap.supply.chain.user.entities.Dealer;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;
import com.therap.supply.chain.user.exception.ResourceNotFoundException;
import com.therap.supply.chain.user.repository.RequisitionRepository;
import com.therap.supply.chain.user.service.RequisitionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class RequisitionServiceImpl implements RequisitionService {

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private ProductServiceImpl productService;

    @Override
    public List<RequisitionDTO> getAllRequisitionByDealer(Long dealerId) {
        Dealer dealer = this.dealerService.getDealer(dealerId);
        return this.requisitionRepository
                .findByDealer(dealer)
                .stream()
                .map(requisition -> this.modelMapper.map(requisition, RequisitionDTO.class))
                .filter(RequisitionDTO::isSubmittedByDealer)
                .sorted(Comparator.comparingLong(RequisitionDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public RequisitionDTO getRequisitionById(Long requisitionId) {
        return this.requisitionToRequisitionDTO(this.getRequisition(requisitionId));
    }

    @Override
    public RequisitionDTO getLastRequisitionByDealer(Long dealerId) {
        Dealer dealer = this.dealerService.getDealer(dealerId);
        Requisition requisition = null;
        requisition = this.requisitionRepository.getByDealerAndIsSubmittedByDealer(dealer, false);
        if (requisition == null) {
            requisition = new Requisition();
            requisition.setDealer(dealer);
            this.requisitionRepository.save(requisition);
        }
        return this.requisitionToRequisitionDTO(requisition);
    }

    @Override
    public Double getTotalPriceRequisition(Requisition requisition) {
        return requisition
                .getRequisitionProductHistories()
                .stream()
                .filter(rph -> !rph.isDeleted())
                .mapToDouble(rph -> rph.getQuantity() * rph.getPrice())
                .sum();
    }

    public Long getTotalProductRequisition(RequisitionDTO requisitionDTO) {
        return requisitionDTO
                .getRequisitionProductHistoryDTOS()
                .stream()
                .filter(rphDTO -> !rphDTO.isDeleted())
                .mapToLong(RequisitionProductHistoryDTO::getQuantity)
                .sum();
    }

    // get requisition
    public Requisition getRequisition(Long requisitionId) {
        return this.requisitionRepository.findById(requisitionId).orElseThrow(() ->
                new ResourceNotFoundException("Requisition", "id", requisitionId));
    }

    // requisition to requisitionDTO
    public RequisitionDTO requisitionToRequisitionDTO(Requisition requisition) {
        // sorting
        if (requisition.getRequisitionProductHistories() != null) {
            requisition.setRequisitionProductHistories(requisition
                    .getRequisitionProductHistories()
                    .stream()
                    .filter(rph -> !rph.isDeleted())
                    .sorted(Comparator.comparingLong(RequisitionProductHistory::getId))
                    .collect(Collectors.toList())
            );
        }

        RequisitionDTO requisitionDTO = this.modelMapper.map(requisition, RequisitionDTO.class);
        requisitionDTO.setDealerDTO(this.modelMapper.map(requisition.getDealer(), DealerDTO.class));
        requisitionDTO.getDealerDTO().setPassword(null);
        requisitionDTO.getDealerDTO().setVerificationCode(null);

        List<RequisitionProductHistoryDTO> requisitionProductHistoryDTOS = new ArrayList<>();
        if (requisition.getRequisitionProductHistories() != null) {
            for (RequisitionProductHistory requisitionProductHistory : requisition.getRequisitionProductHistories()) {
                RequisitionProductHistoryDTO rphDTO = this.modelMapper.map(requisitionProductHistory, RequisitionProductHistoryDTO.class);
                ProductDTO productDTO = this.productService.productToProductDTO(requisitionProductHistory.getProduct());
                rphDTO.setProductDTO(productDTO);
                requisitionProductHistoryDTOS.add(rphDTO);
            }
        }
        requisitionDTO.setRequisitionProductHistoryDTOS(requisitionProductHistoryDTOS);

        if (requisition.getPaymentHistories() != null) {
            requisitionDTO.setPaymentHistoryDTOS(requisition.getPaymentHistories().stream()
                    .map(paymentHistory -> this.modelMapper.map(paymentHistory, PaymentHistoryDTO.class))
                    .sorted(Comparator.comparingLong(PaymentHistoryDTO::getId))
                    .collect(Collectors.toList()));
        }
        return requisitionDTO;
    }


}
