package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.config.AppConstants;
import com.therap.supply.chain.user.entities.Dealer;
import com.therap.supply.chain.user.entities.Product;
import com.therap.supply.chain.user.entities.Requisition;
import com.therap.supply.chain.user.entities.RequisitionProductHistory;
import com.therap.supply.chain.user.repository.RequisitionProductHistoryRepository;
import com.therap.supply.chain.user.repository.RequisitionRepository;
import com.therap.supply.chain.user.service.DealerProductsCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
            List<RequisitionProductHistory>rpHistories = this.requisitionProductHistoryRepository.getByIsDeleted(true);
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


    public String deleteProductFromDealerCart(Long requisitionId, Long rphId) {
        RequisitionProductHistory requisitionProductHistory = this.requisitionProductHistoryService
                .getRequisitionProductHistory(rphId);

        requisitionProductHistory.setDeleted(true);
        requisitionProductHistory = this.requisitionProductHistoryRepository.save(requisitionProductHistory);

        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        requisition.getRequisitionProductHistories().stream().map(rph -> !rph.isDeleted()).collect(Collectors.toList());

        // set total price
        Double totalPrice = this.requisitionService
                .getTotalPriceRequisition(requisition);
        requisition.setTotalAmountPrice(totalPrice);
        this.requisitionRepository.save(requisition);

        return null;
    }

}