package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.AttachmentDTO;
import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.DealerDTO;
import com.therap.supply.chain.admin.email.EmailSenderService;
import com.therap.supply.chain.admin.entities.Dealer;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.DealerRepository;
import com.therap.supply.chain.admin.service.DealerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class DealerServiceImpl implements DealerService {

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private AttachmentServiceImpl attachmentService;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Override
    public DealerDTO getSingleDealerById(Long dealerId) {
        Dealer dealer = getDealer(dealerId);
        DealerDTO dealerDTO = this.modelMapper.map(dealer, DealerDTO.class);
        List<AttachmentDTO> attachmentDTOS = dealer.getAttachments()
                .stream().map(attachment -> this.modelMapper.map(attachment, AttachmentDTO.class))
                .collect(Collectors.toList());
        dealerDTO.setAttachmentDTOS(attachmentDTOS);
        return dealerDTO;
    }

    @Override
    public List<DealerDTO> getAllDealers() {
        List<DealerDTO> dealerDTOS = new ArrayList<>();
        List<Dealer> dealers = this.dealerRepository.findAll();

        for (Dealer dealer : dealers) {
            DealerDTO dealerDTO = this.modelMapper.map(dealer, DealerDTO.class);
            List<AttachmentDTO> attachmentDTOS = dealer.getAttachments()
                    .stream().map(attachment -> this.modelMapper.map(attachment, AttachmentDTO.class))
                    .collect(Collectors.toList());
            dealerDTO.setAttachmentDTOS(attachmentDTOS);
            dealerDTOS.add(dealerDTO);
        }
        return dealerDTOS
                .stream()
                .sorted(Comparator.comparingLong(DealerDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DealerDTO> getAllDealersByApproverByStatus(String activationStatus) {
        return getAllDealers()
                .stream()
                .filter(dealerDTO -> Objects.equals(dealerDTO.getIsActiveByDealerApprover(), activationStatus))
                .sorted(Comparator.comparingLong(DealerDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DealerDTO updateDealer(DealerDTO dealerDTO, Long dealerId, MultipartFile dealerImage) throws IOException {
        Dealer dealer = getDealer(dealerId);
        if (!Objects.equals(dealerImage.getOriginalFilename(), "")) {
            //
            boolean previousProductImageIsDeleted = this.fileService.deleteImage(path, dealer.getAttachments().get(0).getAttachmentFileName());
            String productImageName = this.fileService.uploadImage(path, dealerImage);
            dealer.getAttachments().get(0).setAttachmentFileName(productImageName);
        }

        dealer.setName(dealerDTO.getName());
        dealer.setContact(dealerDTO.getContact());
        dealer.setAge(dealerDTO.getAge());
        dealer.setAddress(dealerDTO.getAddress());
        dealer.setActivate(dealerDTO.isActivate());

        dealer = this.dealerRepository.save(dealer);

        return this.modelMapper.map(dealer, DealerDTO.class);
    }

    @Override
    public DealerDTO updateDealerActivityByApprover(DealerDTO dealerDTO, Long dealerId) {
        Dealer dealer = getDealer(dealerId);
        dealer.setIsActiveByDealerApprover(dealerDTO.getIsActiveByDealerApprover());
        dealer = this.dealerRepository.save(dealer);

        dealerDTO = this.modelMapper.map(dealer, DealerDTO.class);

        String siteURL = AppConstants.userPanelHost + "/verify";
        sendVerificationEmail(dealerDTO, siteURL);

        return dealerDTO;
    }

    // helper method for get dealer
    public Dealer getDealer(Long dealerId) {
        return this.dealerRepository.findById(dealerId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", dealerId));
    }

    // send email for verification
    private void sendVerificationEmail(DealerDTO dealerDTO, String siteURL) {
        siteURL += "/" + dealerDTO.getId() + "/" + dealerDTO.getVerificationCode();
        String subject;
        String emailContent = "<p><b>Dear " + dealerDTO.getName() + ",</b></p>";

        if (dealerDTO.getIsActiveByDealerApprover().equalsIgnoreCase(AppConstants.active)) {
            subject = "Please, Verify your Dealer registration";
            emailContent += "Please click the link below to verify your registration. Now you are active:<br>"
                    + "<h1><a href=\"" + siteURL + "\" target=\"_self\">VERIFY</a></h1>";
        } else {
            subject = "Your Dealer Activity Now " + dealerDTO.getIsActiveByDealerApprover();
            emailContent += "Now your activity is " + dealerDTO.getIsActiveByDealerApprover() +
                    "<br>Please, Contact with Authority.<br>";
        }

        emailContent += "Thank you,<br> " +
                "MSS - Supply Chain Management System.";
        try {
            this.emailSenderService.sendEmailWithoutAttachment(dealerDTO.getEmail(), subject, emailContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
