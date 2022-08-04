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
        Requisition requisition = null;
        requisition = this.requisitionRepository
                .getByDealerAndIsSubmittedByDealer(dealer, false);
        if (requisition == null) {
            requisition = new Requisition();
            requisition.setDealer(dealer);
            this.requisitionRepository.save(requisition);
        }

        RequisitionProductHistory requisitionProductHistory =
                this.requisitionProductHistoryRepository.getByRequisitionAndProduct(requisition, product);

        if (requisitionProductHistory == null) {
            requisitionProductHistory = new RequisitionProductHistory();
            requisitionProductHistory.setProduct(product);
            requisitionProductHistory.setRequisition(requisition);
            requisitionProductHistory.setQuantity(0L);
        }

        requisitionProductHistory.setQuantity(requisitionProductHistory.getQuantity() + 1);
        requisitionProductHistory.setReqProductName(product.getName());
        requisitionProductHistory.setPrice(product.getDiscountPrice());

        this.requisitionProductHistoryRepository.save(requisitionProductHistory);


        // set total price
        Double totalPrice = this.requisitionService
                .getTotalPriceRequisition(requisition.getRequisitionProductHistories());
        requisition.setTotalAmountPrice(totalPrice);

        this.requisitionRepository.save(requisition);

        return null;
    }


    @Transactional
    public String deleteProductFromDealerCart(Long requisitionId, Long rphId) {
        RequisitionProductHistory requisitionProductHistory = this.requisitionProductHistoryService
                .getRequisitionProductHistory(rphId);

        this.requisitionProductHistoryRepository.delete(requisitionProductHistory);

        Requisition requisition = this.requisitionService.getRequisition(requisitionId);

        // set total price
        Double totalPrice = this.requisitionService
                .getTotalPriceRequisition(requisition.getRequisitionProductHistories());
        requisition.setTotalAmountPrice(totalPrice);
        this.requisitionRepository.save(requisition);

        return null;
    }

}