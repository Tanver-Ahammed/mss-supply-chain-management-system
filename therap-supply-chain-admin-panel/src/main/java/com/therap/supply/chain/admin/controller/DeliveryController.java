package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.DeliveryRequisitionServiceImpl;
import com.therap.supply.chain.admin.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/delivery")
public class DeliveryController {

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private DeliveryRequisitionServiceImpl deliveryRequisitionService;

    @GetMapping(path = "/requisitions")
    public String getAllRequisitionForDelivery(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        List<RequisitionDTO> requisitionDTOS = this.deliveryRequisitionService
                .getAllRequisitionByForDelivery();
        model.addAttribute("requisitionDTOS", requisitionDTOS);
        model.addAttribute("message", "");

        return "delivery/show-all-requisition";

    }

    @GetMapping(path = "requisition/{requisitionId}")
    public String getSingleRequisitionById(@PathVariable("requisitionId") Long requisitionId,
                                           Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        RequisitionDTO requisitionDTO = this.requisitionService.getSingleRequisitionById(requisitionId);

        model.addAttribute("requisitionDTO", requisitionDTO);
        model.addAttribute("message", "");
        model.addAttribute("totalProduct",
                this.requisitionService.getTotalProductRequisition(requisitionDTO));

        return "delivery/show-requisition-approve";

    }

    @GetMapping(path = "requisition/{deliveryStatus}/{requisitionId}")
    public String setRequisitionStatusByDelivery(@PathVariable("deliveryStatus") String deliveryStatus,
                                                 @PathVariable("requisitionId") Long requisitionId,
                                                 Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        Boolean isApproveByDelivery = this.deliveryRequisitionService.
                isApproveRequisitionDeliveryForSendMail(requisitionId, deliveryStatus);
        model.addAttribute("message", "");
        model.addAttribute("dangerMessage", "");
        model.addAttribute("requisitionId", requisitionId);
        if (isApproveByDelivery) {
            return "delivery/add-delivery-verification";
        }

        RequisitionDTO requisitionDTO = this.requisitionService.getSingleRequisitionById(requisitionId);
        model.addAttribute("requisitionDTO", requisitionDTO);
        model.addAttribute("dangerMessage", "REQ-" + requisitionDTO.getId() + " No. is Rejected!!!");
        return "delivery/show-requisition-approve";
    }

    @PostMapping(path = "/requisition/verify/{requisitionId}")
    public String setRequisitionStatusByDeliveryVerify(@PathVariable("requisitionId") Long requisitionId,
                                                       @RequestParam("verificationCode") String verificationCode,
                                                       Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        Boolean isVerified = this.deliveryRequisitionService
                .verificationRequisitionForDelivery(requisitionId, verificationCode);

        model.addAttribute("requisitionId", requisitionId);

        if (isVerified) {
            model.addAttribute("message", "Your Requisition is successfully Delivered...");
            model.addAttribute("dangerMessage", "");
            RequisitionDTO requisitionDTO = this.requisitionService.getSingleRequisitionById(requisitionId);
            model.addAttribute("requisitionDTO", requisitionDTO);
            return "delivery/show-requisition-approve";
        }

        model.addAttribute("message", "");
        model.addAttribute("dangerMessage", "Your verification code is wrong.Please, Try Again...");

        return "delivery/add-delivery-verification";
    }


}
