package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.DealerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public interface DealerService {

    DealerDTO registrationDealer(DealerDTO dealerDTO, MultipartFile[] files) throws IOException;

    DealerDTO verifyingDealer(Long dealerId, String verificationCode);

    Boolean forgetDealerPassword(String email);

    Boolean setForgetPassword(Long dealerId, DealerDTO dealerDTO);

    DealerDTO getSingleDealerById(Long dealerId);

    DealerDTO getSingleDealerByEmail(String email);

    Boolean isDuplicateDealerByEmailOrContact(String email, String contact);

    DealerDTO updateDealer(DealerDTO dealerDTO, Long dealerId, MultipartFile dealerImage) throws IOException;

}
