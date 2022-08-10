package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.PaymentHistoryDTO;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.AccountRequisitionServiceImpl;
import com.therap.supply.chain.admin.service.impl.PaymentHistoryServiceImpl;
import com.therap.supply.chain.admin.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/account")
public class AccountController {

    @Autowired
    private AccountRequisitionServiceImpl accountRequisitionService;

    @Autowired
    private PaymentHistoryServiceImpl paymentHistoryService;

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @GetMapping(path = "/payment/requisition/{requisitionId}")
    public String addPaymentByRequisition(@PathVariable("requisitionId") Long requisitionId,
                                          Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        model.addAttribute("paymentHistoryDTO", new PaymentHistoryDTO());
        model.addAttribute("message", "");
        model.addAttribute("requisitionId", requisitionId);

        return "account/add-payment";
    }

    @PostMapping(path = "/payment/requisition/{requisitionId}")
    public String savePaymentByRequisition(@Valid @ModelAttribute("paymentHistoryDTO") PaymentHistoryDTO paymentHistoryDTO,
                                           BindingResult result,
                                           @PathVariable("requisitionId") Long requisitionId,
                                           Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        if (result.hasErrors()) {
            return "account/add-payment";
        }
        paymentHistoryDTO.setMedium("Account Manager");
        paymentHistoryDTO.setAccountNo(authority.getName());
        paymentHistoryDTO.setTransactionId("AUTH-" + authority.getId().toString());

        paymentHistoryDTO = this.paymentHistoryService.savePayment(requisitionId, paymentHistoryDTO);
        if (paymentHistoryDTO != null)
            model.addAttribute("message", "Your Payment Submit Successfully...");
        model.addAttribute("paymentHistoryDTO", new PaymentHistoryDTO());
        return "account/add-payment";
    }


    @GetMapping(path = "/requisitions")
    public String getAllRequisitionForAccount(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        List<RequisitionDTO> requisitionDTOS = this.accountRequisitionService
                .getAllRequisitionByForAccount();
        model.addAttribute("requisitionDTOS", requisitionDTOS);
        model.addAttribute("message", "");

        return "account/show-all-requisition";

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

        return "account/show-requisition-approve";

    }

    @GetMapping(path = "requisition/{accountStatus}/{requisitionId}")
    public String setRequisitionStatusByAccount(@PathVariable("accountStatus") String accountStatus,
                                                @PathVariable("requisitionId") Long requisitionId,
                                                Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        Boolean isApproveByAccount = this.accountRequisitionService.
                isApproveRequisitionStatusByAccount(requisitionId, accountStatus);
        return "redirect:/account/requisitions";
    }

    @GetMapping(path = "/payment/histories")
    public String getAllPaymentForApprove(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);
        model.addAttribute("message", "");
        List<PaymentHistoryDTO> paymentHistoryDTOS = this.paymentHistoryService.getAllPaymentForApprove();
        model.addAttribute("paymentHistoryDTOS", paymentHistoryDTOS);
        return "account/all-payment-by-no-approve";

    }

    @GetMapping(path = "/payment/history/{paymentHistoryId}")
    public String getPaymentById(@PathVariable("paymentHistoryId") Long paymentHistoryId,
                                 Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);
        model.addAttribute("message", "");
        PaymentHistoryDTO paymentHistoryDTO = this.paymentHistoryService.getPaymentHistoryById(paymentHistoryId);
        model.addAttribute("paymentHistoryDTO", paymentHistoryDTO);
        return "account/accept-reject-payment-history";

    }

    @GetMapping(path = "/payment/history/{paymentHistoryStatus}/{paymentHistoryId}")
    public String setPaymentHistoryStatusByAccount(@PathVariable("paymentHistoryStatus") String paymentHistoryStatus,
                                                   @PathVariable("paymentHistoryId") Long paymentHistoryId,
                                                   Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        Boolean isApproveByAccount = this.accountRequisitionService.
                isApprovePaymentHistoryStatusByAccount(paymentHistoryId, paymentHistoryStatus);
        return "redirect:/account/requisitions";
    }

}
