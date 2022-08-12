package com.therap.supply.chain.user.controller;

import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.dto.RequisitionDTO;
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
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/dealer/auth/requisition")
public class RequisitionController {

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private DealerProductsCartServiceImpl dealerProductsCartService;

    @GetMapping(path = "/all")
    public String getAllRequisitionByDealer(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        List<RequisitionDTO> requisitionDTOS = this.requisitionService.getAllRequisitionByDealer(dealer.getId());
        model.addAttribute("requisitionDTOS", requisitionDTOS);

        model.addAttribute("message", "");
        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));

        return "requisition/my-all-requisition";
    }

    @GetMapping(path = "/{requisitionId}")
    public String getRequisitionByDealer(@PathVariable("requisitionId") Long requisitionId,
                                         Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        RequisitionDTO requisitionDTO = this.requisitionService.getRequisitionById(requisitionId);
        model.addAttribute("requisitionDTO", requisitionDTO);

        model.addAttribute("message", "");
        model.addAttribute("totalProduct",
                this.requisitionService.getTotalProductRequisition(requisitionDTO));
        model.addAttribute("totalItemProduct", this.dealerProductsCartService
                .getTotalItemProduct(dealer.getId()));

        return "requisition/my-requisition";
    }


}
