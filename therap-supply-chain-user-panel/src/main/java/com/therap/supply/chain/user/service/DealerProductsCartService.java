package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.RequisitionProductHistoryBindDTO;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface DealerProductsCartService {

    Boolean addProductsDealerCart(Long dealerId, Long productId);

    Boolean deleteProductFromDealerCart(Long requisitionId, Long rphId);

    Boolean checkoutCart(RequisitionProductHistoryBindDTO requisitionProductHistoryBindDTO, Long requisitionId);

    Boolean dealerCartFinalSubmit(Long requisitionId);

}
