package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface AuthorityService {

    // registration authority
    AuthorityDTO registrationAuthority(AuthorityDTO authorityDTO, Long[] userRoles, MultipartFile authorityImage) throws IOException;

    AuthorityDTO verifyingAuthority(Long authorityId, String verificationCode);

    Boolean forgetAuthorityPassword(String email);

    Boolean setPassword(Long authorityId, AuthorityDTO authorityDTO);

    AuthorityDTO getSingleAuthorityById(Long authorityId);

    AuthorityDTO getSingleAuthorityByEmail(String email);

    Boolean isDuplicateAuthorityByEmailOrContact(String email, String contact);

    List<AuthorityDTO> getAllAuthorities();

    List<AuthorityDTO> getAllAuthoritiesByStatus(String activationStatus);

    List<AuthorityDTO> getAllAuthoritiesByRole(Long role);

    AuthorityDTO updateAuthority(Long authorityId, AuthorityDTO authorityDTO, Long[] userRoles,
                                 MultipartFile authorityImage) throws IOException;

    AuthorityDTO updateAuthority(Long authorityId, AuthorityDTO authorityDTO);

}
