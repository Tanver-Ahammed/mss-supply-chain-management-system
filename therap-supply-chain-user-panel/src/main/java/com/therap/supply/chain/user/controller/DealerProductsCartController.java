package com.therap.supply.chain.user.controller;

import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.repository.RequisitionRepository;
import com.therap.supply.chain.user.service.impl.DealerProductsCartServiceImpl;
import com.therap.supply.chain.user.service.impl.DealerServiceImpl;
import com.therap.supply.chain.user.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/dealer/cart")
public class DealerProductsCartController {

    @Autowired
    private DealerProductsCartServiceImpl dealerProductsCartService;

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private RequisitionRepository requisitionRepository;

    @GetMapping(path = "/add/product/{productId}")
    public String addProductDealerCart(@PathVariable("productId") Long productId,
                                       Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        Boolean isProductAdd = this.dealerProductsCartService
                .addProductsDealerCart(dealer.getId(), productId);

        return "redirect:/product/all";
    }

    @GetMapping
    public String showDealerProductCart(Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        model.addAttribute("message", "");
        RequisitionDTO requisitionDTO = this.requisitionService.getLastRequisitionByDealer(dealer.getId());
        model.addAttribute("requisitionDTO", requisitionDTO);
        return "cart/dealer-cart";
    }

    @GetMapping(path = "/requisition/{requisitionId}/delete/{rphId}")
    public String deleteProductFromCart(@PathVariable("requisitionId") Long requisitionId,
                                        @PathVariable("rphId") Long rphId,
                                        Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);
        this.dealerProductsCartService.deleteProductFromDealerCart(requisitionId, rphId);
        return "redirect:/dealer/cart";
    }

    @GetMapping(path = "/checkout")
    public String dealerCartCheckout(Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);
        model.addAttribute("dealer", dealer);
        model.addAttribute("message", "");
        model.addAttribute("requisition", this.requisitionService
                .getLastRequisitionByDealer(dealer.getId()));
        return "cart/cart-checkout";
    }

}