package com.therap.supply.chain.admin.service.impl;

import com.sun.mail.util.MailConnectException;
import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.email.EmailSenderService;
import com.therap.supply.chain.admin.entities.Requisition;
import com.therap.supply.chain.admin.repository.RequisitionRepository;
import com.therap.supply.chain.admin.service.DeliveryRequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryRequisitionServiceImpl implements DeliveryRequisitionService {

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public List<RequisitionDTO> getAllRequisitionByForDelivery() {
        return this.requisitionRepository
                .findByIsApproveByAccountManagerAndIsDelivered(AppConstants.accept, AppConstants.pause)
                .stream()
                .map(requisition -> this.requisitionService.requisitionToRequisitionDTO(requisition))
                .sorted(Comparator.comparingLong(RequisitionDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isApproveRequisitionDeliveryForSendMail(Long requisitionId, String deliveryStatus) {
        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        try {
            if (deliveryStatus.equalsIgnoreCase(AppConstants.accept))
                this.sendVerificationEmail(requisition, AppConstants.accept);
            else if (deliveryStatus.equalsIgnoreCase(AppConstants.reject)) {
                requisition.setIsDelivered(AppConstants.reject);
                this.requisitionRepository.save(requisition);
                this.sendVerificationEmail(requisition, AppConstants.reject);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean verificationRequisitionForDelivery(Long requisitionId, String verificationCode) {
        Requisition requisition = this.requisitionService.getRequisition(requisitionId);
        if (verificationCode.equals(requisition.getVerificationCode())) {
            requisition.setIsDelivered(AppConstants.accept);
            this.requisitionRepository.save(requisition);
            return true;
        }
        return false;
    }

    // send email for verification
    private void sendVerificationEmail(Requisition requisition, String deliveryStatus) {
        String subject;
        String emailContent = "<p><b>Dear " + requisition.getDealer().getName() + ",</b></p>";

        if (deliveryStatus.equalsIgnoreCase(AppConstants.accept)) {
            subject = "Welcome, Your Requisition is Successfully Deliver Your products";
            emailContent += "Please, Share your Verification Code on Delivery Manager:<br>"
                    + "<h1><a>" + requisition.getVerificationCode() + "</a></h1>";
        } else {
            subject = "Sorry, Your Requisition is Rejected by Delivery Department";
            emailContent += "Your Requisition is Unfortunately Rejected by Delivery.<br>" +
                    "Please, Contact with Delivery Department.<br>";
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
