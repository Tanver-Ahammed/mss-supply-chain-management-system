package com.therap.supply.chain.user.controller;

import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.dto.RequisitionProductHistoryBindDTO;
import com.therap.supply.chain.user.dto.RequisitionProductHistoryDTO;
import com.therap.supply.chain.user.repository.RequisitionRepository;
import com.therap.supply.chain.user.service.impl.DealerProductsCartServiceImpl;
import com.therap.supply.chain.user.service.impl.DealerServiceImpl;
import com.therap.supply.chain.user.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));

        return "redirect:/product/all";
    }

    @GetMapping
    public String showDealerProductCart(Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);
        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));

        model.addAttribute("message", "");
        RequisitionDTO requisitionDTO = this.requisitionService.getLastRequisitionByDealer(dealer.getId());
        model.addAttribute("requisitionDTO", requisitionDTO);
        model.addAttribute("totalProduct",
                this.requisitionService.getTotalProductRequisition(requisitionDTO));
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

        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));

        return "redirect:/dealer/cart";
    }

    @GetMapping(path = "/checkout")
    public String dealerCartCheckout(Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);
        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));

        model.addAttribute("message", "");
        RequisitionDTO requisitionDTO = this.requisitionService.
                getLastRequisitionByDealer(dealer.getId());
        RequisitionProductHistoryBindDTO historyBindDTOS = new RequisitionProductHistoryBindDTO();
        for (RequisitionProductHistoryDTO productHistoryDTO : requisitionDTO.getRequisitionProductHistoryDTOS()) {
            historyBindDTOS.addRequisitionProductHistoryDTO(productHistoryDTO);
        }

        model.addAttribute("requisitionProductHistoryDTOS", historyBindDTOS);
        model.addAttribute("totalProduct", this.requisitionService.getTotalProductRequisition(requisitionDTO));
        model.addAttribute("totalPrice", requisitionDTO.getTotalAmountPrice());
        model.addAttribute("requisitionId", requisitionDTO.getId());
        return "cart/cart-checkout";
    }

    @PostMapping(path = "checkout/submit")
    public String dealerProductCartCheckoutSubmit(@ModelAttribute("requisitionDTO")
                                                  RequisitionProductHistoryBindDTO historyBindDTO,
                                                  @RequestParam("requisitionId") Long requisitionId,
                                                  Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        System.out.println(requisitionId);
        this.dealerProductsCartService.checkoutCart(historyBindDTO, requisitionId);
        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));
        return "redirect:/dealer/cart";
    }

    @GetMapping(path = "/submit/{requisitionId}")
    public String dealerCartFinalSubmit(@PathVariable("requisitionId") Long requisitionId,
                                        Model model, Principal principal) {
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        Boolean isCartSubmitted = this.dealerProductsCartService.dealerCartFinalSubmit(requisitionId);
        if (isCartSubmitted)
            model.addAttribute("message", "Your Requisition is Added Successfully...");
        return "redirect:/dealer/cart";
    }

}