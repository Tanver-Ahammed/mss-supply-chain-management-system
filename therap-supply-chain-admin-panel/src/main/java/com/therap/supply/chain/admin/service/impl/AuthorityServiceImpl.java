package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.AttachmentDTO;
import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.RoleDTO;
import com.therap.supply.chain.admin.email.EmailSenderService;
import com.therap.supply.chain.admin.entities.Attachment;
import com.therap.supply.chain.admin.entities.Authority;
import com.therap.supply.chain.admin.entities.Role;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.AuthorityRepository;
import com.therap.supply.chain.admin.repository.RoleRepository;
import com.therap.supply.chain.admin.service.AuthorityService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AttachmentServiceImpl attachmentService;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${project.image}")
    private String path;

    // registration authority
    @Override
    public AuthorityDTO registrationAuthority(AuthorityDTO authorityDTO, Long[] userRoles, MultipartFile authorityImage)
            throws IOException {
        if (Objects.equals(authorityImage.getOriginalFilename(), "")) {
            return null;
        }

        Attachment attachment = this.modelMapper
                .map(this.attachmentService.addSingleAttachment(authorityImage), Attachment.class);
        attachment.setAttachmentName("Image");

        Authority authority = this.modelMapper.map(authorityDTO, Authority.class);

        authority.setActivity(AppConstants.active);
        authority.setIsEnable(false);
        String verificationCode = RandomString.make(64);
        authority.setVerificationCode(verificationCode);

        Set<Role> roleSet = new HashSet<>();
        for (Long roleId : userRoles) {
            Role role = this.roleRepository.findById(roleId).orElseThrow(() ->
                    new ResourceNotFoundException("role", "id", roleId));
            roleSet.add(role);
        }

        authority.setRoles(roleSet);
        authority.setAttachment(attachment);

        authorityDTO = this.modelMapper.map(this.authorityRepository.save(authority), AuthorityDTO.class);

        String siteURL = AppConstants.adminPanelHost + "/auth/verify";
        sendVerificationEmail(authorityDTO, siteURL);

        return authorityDTO;
    }

    @Override
    public AuthorityDTO verifyingAuthority(Long authorityId, String verificationCode) {
        Authority authority = getAuthority(authorityId);
        if (authority.getVerificationCode().equals(verificationCode)) {
            authority.setIsEnable(true);
            authority.setVerificationCode(RandomString.make(64));
            return this.modelMapper
                    .map(this.authorityRepository.save(authority), AuthorityDTO.class);
        }
        return null;
    }

    @Override
    public Boolean forgetAuthorityPassword(String email) {
        Authority authority = this.authorityRepository.findAuthoritiesByEmail(email);
        if (authority == null)
            return false;
        else {
            AuthorityDTO dealerDTO = this.modelMapper.map(authority, AuthorityDTO.class);
            String siteURL = AppConstants.adminPanelHost + "/forget/password";
            sendVerificationEmail(dealerDTO, siteURL);
            return true;
        }
    }

    @Override
    public Boolean setPassword(Long authorityId, AuthorityDTO authorityDTO) {
        Authority authority = getAuthority(authorityId);
        if (authority.getVerificationCode().equals(authorityDTO.getVerificationCode())) {
            authority.setPassword(this.passwordEncoder.encode(authorityDTO.getPassword()));
            authority.setVerificationCode(RandomString.make(64));
            authority = this.authorityRepository.save(authority);
            System.out.println(authority);
            return true;
        }
        return false;
    }

    @Override
    public AuthorityDTO getSingleAuthorityById(Long authorityId) {
        Authority authority = getAuthority(authorityId);
        return authorityToAuthorityDTO(authority);
    }

    @Override
    public Boolean isDuplicateAuthorityByEmailOrContact(String email, String contact) {
        Authority authority = this.authorityRepository.findAuthoritiesByEmailOrContact(email, contact);
        return authority != null;
    }

    @Override
    public AuthorityDTO getSingleAuthorityByEmail(String email) {
        return authorityToAuthorityDTO(this.authorityRepository.findAuthoritiesByEmail(email));
    }

    @Override
    public List<AuthorityDTO> getAllAuthorities() {
        List<AuthorityDTO> authorityDTOS = new ArrayList<>();
        List<Authority> authorities = this.authorityRepository.findAll();

        for (Authority authority : authorities) {
            AuthorityDTO authorityDTO = authorityToAuthorityDTO(authority);
            authorityDTOS.add(authorityDTO);
        }
        return authorityDTOS;
    }

    @Override
    public List<AuthorityDTO> getAllAuthoritiesByStatus(String activationStatus) {
        return getAllAuthorities()
                .stream()
                .filter(dealerDTO -> Objects.equals(dealerDTO.getActivity(), activationStatus))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorityDTO> getAllAuthoritiesByRole(Long roleId) {
        Role role = this.roleRepository.findById(roleId).orElseThrow(() ->
                new ResourceNotFoundException("Role", "id", roleId));
        RoleDTO roleDTO = this.modelMapper.map(role, RoleDTO.class);
        List<AuthorityDTO> authorityDTOS = new ArrayList<>();
        for (AuthorityDTO authorityDTO : getAllAuthorities()) {
            for (RoleDTO roleDTO1 : authorityDTO.getRoleDTOS()) {
                if (roleDTO1.getRole().equals(role.getRole())) {
                    authorityDTOS.add(authorityDTO);
                    break;
                }
            }
        }
        return authorityDTOS;
    }

    @Override
    public AuthorityDTO updateAuthority(Long authorityId, AuthorityDTO authorityDTO, Long[] userRoles,
                                        MultipartFile authorityImage) throws IOException {
        Authority authority = getAuthority(authorityId);
        if (authorityImage != null) {
            if (!Objects.equals(authorityImage.getOriginalFilename(), "")) {
                //
                boolean previousProductImageIsDeleted = this.fileService.deleteImage(path, authority.getAttachment().getAttachmentFileName());
                String productImageName = this.fileService.uploadImage(path, authorityImage);
                authority.getAttachment().setAttachmentFileName(productImageName);
            }
        }

        Set<Role> roleSet = new HashSet<>();
        if (userRoles != null && userRoles.length > 0) {
            for (Long roleId : userRoles) {
                Role role = this.roleRepository.findById(roleId).orElseThrow(() ->
                        new ResourceNotFoundException("role", "id", roleId));
                roleSet.add(role);
            }
        } else {
            roleSet = authority.getRoles();
        }

        authority.setRoles(roleSet);
        authority.setName(authorityDTO.getName());
        authority.setContact(authorityDTO.getContact());
        authority.setAge(authorityDTO.getAge());
        authority.setBloodGroup(authorityDTO.getBloodGroup());
        authority.setAddress(authorityDTO.getAddress());

        authority = this.authorityRepository.save(authority);

        return this.modelMapper.map(authority, AuthorityDTO.class);
    }

    @Override
    public AuthorityDTO updateAuthority(Long authorityId, AuthorityDTO authorityDTO) {
        Authority authority = getAuthority(authorityId);
        authority.setName(authorityDTO.getName());
        authority.setContact(authorityDTO.getContact());
        authority.setEmail(authorityDTO.getEmail());
        authority.setVerificationCode(RandomString.make(64));
        authority.setBloodGroup(authorityDTO.getBloodGroup());
        authority.setAddress(authorityDTO.getAddress());
        return this.modelMapper.map(this.authorityRepository.save(authority), AuthorityDTO.class);
    }

    // helper method for get authority
    public Authority getAuthority(Long authorityId) {
        return this.authorityRepository.findById(authorityId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", authorityId));
    }

    // send email for verification
    private void sendVerificationEmail(AuthorityDTO authorityDTO, String siteURL) {
        String subject = "Please, Verify your Authority registration";
        siteURL += "/" + authorityDTO.getId() + "/" + authorityDTO.getVerificationCode();
        String emailContent = "<p><b>Dear " + authorityDTO.getName() + ",</b></p>"
                + "Please click the link below to verify your registration and Set Password:<br>"
                + "<h1><a href=\"" + siteURL + "\" target=\"_self\">VERIFY</a></h1>"
                + "Thank you,<br>"
                + "MSS - Supply Chain Management System.";
        try {
            this.emailSenderService.sendEmailWithoutAttachment(authorityDTO.getEmail(), subject, emailContent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // get logged-in username
    public AuthorityDTO getAuthorityDTOIfLoggedIn(Principal principal) {
        String username = principal.getName();
        AuthorityDTO authorityDTO = this.getSingleAuthorityByEmail(username);
        if (authorityDTO.getName().length() > 10)
            authorityDTO.setName(authorityDTO.getName().substring(0, 10));
        authorityDTO.setPassword(null);
        return authorityDTO;
    }

    // authority to authorityDTO
    public AuthorityDTO authorityToAuthorityDTO(Authority authority) {
        AuthorityDTO authorityDTO = this.modelMapper.map(authority, AuthorityDTO.class);
        AttachmentDTO attachmentDTO = this.modelMapper.map(authority.getAttachment(), AttachmentDTO.class);
        authorityDTO.setAttachmentDTO(attachmentDTO);
        Set<RoleDTO> roleDTOS = authority
                .getRoles()
                .stream().map(role -> this.modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toCollection(TreeSet::new));
        authorityDTO.setRoleDTOS(roleDTOS);
        return authorityDTO;
    }

}
