package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.InventoryRequisitionServiceImpl;
import com.therap.supply.chain.admin.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    @Autowired
    private InventoryRequisitionServiceImpl inventoryRequisitionService;

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @GetMapping(path = "/requisitions")
    public String getAllRequisitionForInventory(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        List<RequisitionDTO> requisitionDTOS = this.inventoryRequisitionService
                .getAllRequisitionByForInventory();
        model.addAttribute("requisitionDTOS", requisitionDTOS);
        model.addAttribute("message", "");

        return "inventory/show-all-requisition";

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

        return "inventory/show-requisition-approve";

    }

    @GetMapping(path = "requisition/{inventoryStatus}/{requisitionId}")
    public String setRequisitionStatusByInventory(@PathVariable("inventoryStatus") String inventoryStatus,
                                                  @PathVariable("requisitionId") Long requisitionId,
                                                  Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        Boolean isApproveByInventory = this.inventoryRequisitionService.
                isApproveRequisitionStatusByInventory(requisitionId, inventoryStatus);
        return "redirect:/inventory/requisitions";
    }


}
