package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.config.AppConstants;
import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.dto.RequisitionProductHistoryBindDTO;
import com.therap.supply.chain.user.dto.RequisitionProductHistoryDTO;
import com.therap.supply.chain.user.entities.Dealer;
import com.therap.supply.chain.user.entities.Product;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;
import com.therap.supply.chain.user.repository.RequisitionProductHistoryRepository;
import com.therap.supply.chain.user.repository.RequisitionRepository;
import com.therap.supply.chain.user.service.DealerProductsCartService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class DealerProductsCartServiceImpl implements DealerProductsCartService {

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private RequisitionProductHistoryServiceImpl requisitionProductHistoryService;

    @Autowired
    private RequisitionProductHistoryRepository requisitionProductHistoryRepository;

    @Override
    public Boolean addProductsDealerCart(Long dealerId, Long productId) {
        Dealer dealer = this.dealerService.getDealer(dealerId);
        Product product = this.productService.getProduct(productId);
        Requisition requisition = this.requisitionRepository
                .getByDealerAndIsSubmittedByDealer(dealer, false);
        if (requisition == null) {
            requisition = new Requisition();
            requisition.setDealer(dealer);
            this.requisitionRepository.save(requisition);
        }
        requisition.setDate(new Date());

        RequisitionProductHistory requisitionProductHistory =
                this.requisitionProductHistoryRepository.getByRequisitionAndProductAndIsDeleted(requisition, product, false);
        if (requisitionProductHistory == null) {
            List<RequisitionProductHistory> rpHistories = this.requisitionProductHistoryRepository.getByIsDeleted(true);
            if (rpHistories.size() > 0) {
                requisitionProductHistory = rpHistories.get(0);
                requisitionProductHistory.setQuantity(0L);
            }
        }

        if (requisitionProductHistory == null) {
            requisitionProductHistory = new RequisitionProductHistory();
            requisitionProductHistory.setQuantity(0L);
        }
        requisitionProductHistory.setDeleted(false);
        requisitionProductHistory.setProduct(product);
        requisitionProductHistory.setRequisition(requisition);
        requisitionProductHistory.setQuantity(requisitionProductHistory.getQuantity() + 1);
        requisitionProductHistory.setPrice(product.getDiscountPrice());

        this.requisitionProductHistoryRepository.save(requisitionProductHistory);


        // set total price
        Double totalPrice = this.requisitionService
                .getTotalPriceRequisition(requisition);
        requisition.setTotalAmountPrice(totalPrice);

        this.requisitionRepository.save(requisition);

        return null;
    }


    @Override
    public Boolean deleteProductFromDealerCart(Long requisitionId, Long rphId) {
        RequisitionProductHistory requisitionProductHistory = this.requisitionProductHistoryService
                .getRequisitionProductHistory(rphId);

        requisitionProductHistory.setDeleted(true);
        requisitionProductHistory.setRequisition(null);
        requisitionProductHistory = this.requisitionProductHistoryRepository.save(requisitionProductHistory);

        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        requisition.getRequisitionProductHistories().stream().map(rph -> !rph.isDeleted()).collect(Collectors.toList());

        // set total price
        Double totalPrice = this.requisitionService
                .getTotalPriceRequisition(requisition);
        requisition.setTotalAmountPrice(totalPrice);
        this.requisitionRepository.save(requisition);

        return true;
    }

    @Override
    public Boolean checkoutCart(RequisitionProductHistoryBindDTO historyBindDTO, Long requisitionId) {
        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        List<RequisitionProductHistory> requisitionProductHistories =
                requisition
                        .getRequisitionProductHistories()
                        .stream()
                        .filter(rph -> !rph.isDeleted())
                        .sorted(Comparator.comparingLong(RequisitionProductHistory::getId))
                        .collect(Collectors.toList());
        for (int i = 0; i < historyBindDTO.getRphDTO().size(); i++) {
            RequisitionProductHistory rph = requisitionProductHistories.get(i);
            RequisitionProductHistoryDTO rphDTO = historyBindDTO.getRphDTO().get(i);
            if (rph.getProduct().getStock() > rphDTO.getQuantity())
                rph.setQuantity(historyBindDTO.getRphDTO().get(i).getQuantity());
            else
                rph.setQuantity(rph.getProduct().getStock());
        }
        // set total price
        Double totalPrice = this.requisitionService
                .getTotalPriceRequisition(requisition);
        requisition.setTotalAmountPrice(totalPrice);
        this.requisitionRepository.save(requisition);
        return true;
    }

    @Override
    public Boolean dealerCartFinalSubmit(Long requisitionId) {
        Requisition requisition = this.requisitionService.getRequisition(requisitionId);

        // control stock
        List<RequisitionProductHistory> requisitionProductHistories =
                requisition.getRequisitionProductHistories()
                        .stream()
                        .filter(rph -> !rph.isDeleted())
                        .sorted(Comparator.comparingLong(RequisitionProductHistory::getId))
                        .collect(Collectors.toList());
        for (RequisitionProductHistory requisitionProductHistory : requisitionProductHistories) {
            Long productStock = requisitionProductHistory.getProduct().getStock();
            Long productQuantityOfDemand = requisitionProductHistory.getQuantity();
            if (productStock > productQuantityOfDemand)
                requisitionProductHistory.getProduct().setStock(productStock - productQuantityOfDemand);
            else {
                requisitionProductHistory.getProduct().setStock(0L);
                requisitionProductHistory.setQuantity(productStock);
            }
        }

        // requisition set value
        requisition.setDate(new Date());
        requisition.setVerificationCode(RandomString.make(6));
        // set total price
        Double totalPrice = this.requisitionService
                .getTotalPriceRequisition(requisition);
        requisition.setTotalAmountPrice(totalPrice);
        requisition.setPaidAmount(0.0);
        requisition.setSubmittedByDealer(true);
        requisition.setIsApproveByInventoryManager(AppConstants.pause);
        requisition.setIsApproveByAccountManager(AppConstants.pause);
        requisition.setIsDelivered(AppConstants.pause);
        this.requisitionRepository.save(requisition);
        return true;
    }

    @Override
    public Integer getTotalItemProduct(Long dealerId) {
        RequisitionDTO requisitionDTO = this.requisitionService.getLastRequisitionByDealer(dealerId);

        System.out.println(requisitionDTO);

        if (requisitionDTO == null)
            return 0;
        else
            return requisitionDTO.getRequisitionProductHistoryDTOS().size();
    }

}