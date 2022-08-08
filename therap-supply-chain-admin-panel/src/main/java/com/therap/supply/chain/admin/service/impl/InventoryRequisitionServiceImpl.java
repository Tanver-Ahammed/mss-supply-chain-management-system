package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.email.EmailSenderService;
import com.therap.supply.chain.admin.entities.Requisition;
import com.therap.supply.chain.admin.repository.RequisitionRepository;
import com.therap.supply.chain.admin.service.InventoryRequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryRequisitionServiceImpl implements InventoryRequisitionService {

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public List<RequisitionDTO> getAllRequisitionByForInventory() {
        return this.requisitionRepository
                .findByIsSubmittedByDealerAndIsApproveByInventoryManager(true, AppConstants.pause)
                .stream()
                .map(requisition -> this.requisitionService.requisitionToRequisitionDTO(requisition))
                .sorted(Comparator.comparingLong(RequisitionDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isApproveRequisitionStatusByInventory(Long requisitionId, String inventoryStatus) {
        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        if (inventoryStatus.equalsIgnoreCase(AppConstants.accept))
            requisition.setIsApproveByInventoryManager(AppConstants.accept);
        else if (inventoryStatus.equalsIgnoreCase(AppConstants.reject)) {
            requisition.setIsApproveByInventoryManager(AppConstants.reject);
            requisition = this.requisitionService.recoveryStockFromRequisition(requisition);
        }
        this.requisitionRepository.save(requisition);
        this.sendVerificationEmail(requisition, AppConstants.userPanelHost);
        return true;
    }

    // send email for verification
    private void sendVerificationEmail(Requisition requisition, String siteURL) {
        siteURL += "/dealer/payment/requisition/" + requisition.getId();
        String subject;
        String emailContent = "<p><b>Dear " + requisition.getDealer().getName() + ",</b></p>";

        if (requisition.getIsApproveByInventoryManager().equalsIgnoreCase(AppConstants.accept)) {
            subject = "Welcome, Your Requisition is Successfully Accepted by Inventory";
            emailContent += "Please, Login and click below the link for Payment:<br>"
                    + "<h1><a href=\"" + siteURL + "\" target=\"_self\">Payment</a></h1>";
        } else {
            subject = "Sorry, Your Requisition is Rejected by Inventory";
            emailContent += "Your Requisition is Unfortunately Rejected by Inventory.<br>" +
                    "Please, Contact with Inventory Department.<br>";
        }

        emailContent += "Thank you,<br> " +
                "MSS - Supply Chain Management System.";
        try {
            this.emailSenderService.sendEmailWithoutAttachment(requisition.getDealer().getEmail(), subject, emailContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
