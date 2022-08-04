package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.AttachmentDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@Service
public interface AttachmentService {

    List<AttachmentDTO> addAttachments(MultipartFile[] multipartFiles) throws IOException;

}
