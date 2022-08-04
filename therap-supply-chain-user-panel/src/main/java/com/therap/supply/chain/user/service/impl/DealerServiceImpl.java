package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.config.AppConstants;
import com.therap.supply.chain.user.dto.AttachmentDTO;
import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.dto.RequisitionDTO;
import com.therap.supply.chain.user.email.EmailSenderService;
import com.therap.supply.chain.user.entities.Attachment;
import com.therap.supply.chain.user.entities.Dealer;
import com.therap.supply.chain.user.exception.ResourceNotFoundException;
import com.therap.supply.chain.user.repository.DealerRepository;
import com.therap.supply.chain.user.service.DealerService;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
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
    public DealerDTO registrationDealer(DealerDTO dealerDTO, MultipartFile[] files) throws IOException {
        if (files.length < 3) {
            return null;
        }

        List<Attachment> attachments = this.attachmentService
                .addAttachments(files)
                .stream()
                .map(attachmentDTO -> this.modelMapper.map(attachmentDTO, Attachment.class))
                .collect(Collectors.toList());
        attachments.get(0).setAttachmentName("Image");
        attachments.get(1).setAttachmentName("NID");
        attachments.get(2).setAttachmentName("TIN");

        dealerDTO.setActivate(false);

        Dealer dealer = this.modelMapper.map(dealerDTO, Dealer.class);

        dealer.setAttachments(attachments);
        dealer.setPassword(passwordEncoder.encode(dealer.getPassword()));
        // verification code generate
        String verificationCode = RandomString.make(64);
        dealer.setVerificationCode(verificationCode);
        dealer.setRole("DEALER");
        dealer.setActivate(false);
        dealer.setIsActiveByDealerApprover(AppConstants.inactive);

        dealer = this.dealerRepository.save(dealer);

        String siteURL = AppConstants.host + "/dealer/verify";
//        sendVerificationEmail(dealerDTO, siteURL);

        return this.modelMapper.map(dealer, DealerDTO.class);
    }

    @Override
    public DealerDTO verifyingDealer(Long dealerId, String verificationCode) {
        Dealer dealer = getDealer(dealerId);
        if (dealer.getVerificationCode().equals(verificationCode)) {
            dealer.setActivate(true);
            dealer.setVerificationCode(RandomString.make(64));
            return this.modelMapper
                    .map(this.dealerRepository.save(dealer), DealerDTO.class);
        }
        return null;
    }

    @Override
    public Boolean forgetDealerPassword(String email) {
        Dealer dealer = this.dealerRepository.findDealerByEmail(email);
        if (dealer == null)
            return false;
        else {
            DealerDTO dealerDTO = this.modelMapper.map(dealer, DealerDTO.class);
            String siteURL = AppConstants.host + "/forget/password";
            sendVerificationEmail(dealerDTO, siteURL);
            return true;
        }
    }

    @Override
    public Boolean setForgetPassword(Long dealerId, DealerDTO dealerDTO) {
        Dealer dealer = getDealer(dealerId);
        if (dealer.getVerificationCode().equals(dealerDTO.getVerificationCode())) {
            dealer.setPassword(this.passwordEncoder.encode(dealerDTO.getPassword()));
            dealer.setVerificationCode(RandomString.make(64));
            dealer = this.dealerRepository.save(dealer);
            System.out.println(dealer);
            return true;
        }
        return false;
    }

    @Override
    public DealerDTO getSingleDealerById(Long dealerId) {
        Dealer dealer = getDealer(dealerId);
        DealerDTO dealerDTO = this.modelMapper.map(dealer, DealerDTO.class);
        List<AttachmentDTO> attachmentDTOS = dealer.getAttachments()
                .stream().map(attachment -> this.modelMapper.map(attachment, AttachmentDTO.class))
                .collect(Collectors.toList());
        dealerDTO.setAttachmentDTOS(attachmentDTOS);
        dealerDTO.setPassword(null);
        return dealerDTO;
    }

    @Override
    public Boolean isDuplicateDealerByEmailOrContact(String email, String contact) {
        Dealer dealer = this.dealerRepository.findDealerByEmailOrContact(email, contact);
        return dealer != null;
    }

    @Override
    public DealerDTO getSingleDealerByEmail(String email) {
        return this.dealerToDealerDTO(this.dealerRepository.findDealerByEmail(email));
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

        if (dealerDTO.getPassword() != null)
            dealer.setPassword(passwordEncoder.encode(dealerDTO.getPassword()));

        dealer.setName(dealerDTO.getName());
        dealer.setContact(dealerDTO.getContact());
        dealer.setEmail(dealer.getEmail());
        dealer.setAge(dealerDTO.getAge());
        dealer.setAddress(dealerDTO.getAddress());

        dealer = this.dealerRepository.save(dealer);

        return this.modelMapper.map(dealer, DealerDTO.class);
    }

    // get logged-in username
    public DealerDTO getDealerDTOIfLoggedIn(Principal principal) {
        String username = principal.getName();
        DealerDTO dealerDTO = this.getSingleDealerByEmail(username);
        if (dealerDTO.getName().length() > 10)
            dealerDTO.setName(dealerDTO.getName().substring(0, 10));
        dealerDTO.setPassword(null);
        return dealerDTO;
    }

    // helper method for get dealer
    public Dealer getDealer(Long dealerId) {
        return this.dealerRepository.findById(dealerId)
                .orElseThrow(() -> new ResourceNotFoundException("Dealer", "Id", dealerId));
    }

    // dealer to dealerDTO
    public DealerDTO dealerToDealerDTO(Dealer dealer) {
        DealerDTO dealerDTO = this.modelMapper.map(dealer, DealerDTO.class);
        dealerDTO.setAttachmentDTOS(dealer.getAttachments().stream()
                .map(attachment -> this.attachmentService.attachmentToAttachmentDTO(attachment))
                .collect(Collectors.toList()));
        dealerDTO.setRequisitionDTOS(dealer
                .getRequisitions()
                .stream()
                .map(requisition -> this.modelMapper.map(requisition, RequisitionDTO.class))
                .collect(Collectors.toList()));
        dealerDTO.setPassword(null);
        dealerDTO.setVerificationCode(null);
        return dealerDTO;
    }

    // send email for verification
    private void sendVerificationEmail(DealerDTO dealerDTO, String siteURL) {
        String subject = "Please, Go to the link and set the password";
        siteURL += "/" + dealerDTO.getId() + "/" + dealerDTO.getVerificationCode();
        String emailContent = "<p><b>Dear " + dealerDTO.getName() + ",</b></p>"
                + "Please click the link below to set password:<br>"
                + "<h1><a href=\"" + siteURL + "\" target=\"_self\">SET PASSWORD</a></h1>"
                + "Thank you,<br>"
                + "MSS - Supply Chain Management System.";
        System.err.println(siteURL);
        try {
            this.emailSenderService.sendEmailWithoutAttachment(dealerDTO.getEmail(), subject, emailContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
