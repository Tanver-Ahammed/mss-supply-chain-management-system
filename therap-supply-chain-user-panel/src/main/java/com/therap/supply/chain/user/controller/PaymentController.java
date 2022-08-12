package com.therap.supply.chain.user.controller;

import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.dto.PaymentHistoryDTO;
import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.service.impl.DealerProductsCartServiceImpl;
import com.therap.supply.chain.user.service.impl.DealerServiceImpl;
import com.therap.supply.chain.user.service.impl.PaymentHistoryServiceImpl;
import com.therap.supply.chain.user.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/dealer/auth/payment")
public class PaymentController {

    @Autowired
    private PaymentHistoryServiceImpl paymentHistoryService;

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private DealerProductsCartServiceImpl dealerProductsCartService;

    @GetMapping(path = "/requisition/{requisitionId}")
    public String addPaymentByRequisition(@PathVariable("requisitionId") Long requisitionId,
                                          Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        model.addAttribute("paymentHistoryDTO", new PaymentHistoryDTO());
        model.addAttribute("message", "");
        model.addAttribute("requisitionId", requisitionId);
        RequisitionDTO requisitionDTO = this.requisitionService.getRequisitionById(requisitionId);
        // total amount of rest
        Double totalAmountOfRest = requisitionDTO.getTotalAmountPrice() - requisitionDTO.getPaidAmount();
        model.addAttribute("totalAmountOfRest", totalAmountOfRest);

        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));
        return "payment/add-payment";
    }

    @PostMapping(path = "/save/{requisitionId}")
    public String savePayment(@Valid @ModelAttribute("paymentHistoryDTO") PaymentHistoryDTO paymentHistoryDTO,
                              BindingResult result,
                              @PathVariable("requisitionId") Long requisitionId,
                              Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        model.addAttribute("paymentHistoryDTO", new PaymentHistoryDTO());
        if (result.hasErrors()) {
            return "payment/add-payment";
        }

        paymentHistoryDTO = this.paymentHistoryService.savePayment(requisitionId, paymentHistoryDTO);
        if (paymentHistoryDTO != null)
            model.addAttribute("message", "Your Payment Submit Successfully...");
        return "payment/add-payment";

    }

    @GetMapping(path = "/requisition/by/{requisitionId}")
    public String getAllPaymentHistoryById(@PathVariable("requisitionId") Long requisitionId,
                                           Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        List<PaymentHistoryDTO> paymentHistoryDTOS = this.paymentHistoryService
                .getAllPaymentByRequisitionId(requisitionId);
        model.addAttribute("message", "");
        model.addAttribute("paymentHistoryDTOS", paymentHistoryDTOS);
        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));

        return "payment/all-payment-by-requisition";
    }

}
