package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.DealerDTO;
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

    List<DealerDTO> getAllDealers();

    List<DealerDTO> getAllDealersByApproverByStatus(String activationStatus);

    DealerDTO getSingleDealerById(Long dealerId);

    DealerDTO updateDealer(DealerDTO dealerDTO, Long dealerId, MultipartFile dealerImage) throws IOException;

    DealerDTO updateDealerActivityByApprover(DealerDTO dealerDTO, Long dealerId);

}
