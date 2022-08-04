package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.dto.AttachmentDTO;
import com.therap.supply.chain.admin.repository.AttachmentRepository;
import com.therap.supply.chain.admin.service.AttachmentService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Override
    public List<AttachmentDTO> addAttachments(MultipartFile[] multipartFiles) throws IOException {
        if (Objects.equals(multipartFiles[0].getOriginalFilename(), "")) {
            return null;
        }

        List<AttachmentDTO> attachmentDTOS = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            AttachmentDTO attachmentDTO = new AttachmentDTO();
            String originalFileName = this.fileService.uploadImage(path, multipartFile);
            attachmentDTO.setAttachmentFileName(originalFileName);

            String extension = FilenameUtils.getExtension(originalFileName);
            attachmentDTO.setAttachmentType(extension);

            attachmentDTO.setAttachmentPath(path);
            attachmentDTOS.add(attachmentDTO);
        }

        return attachmentDTOS;
    }

    @Override
    public AttachmentDTO addSingleAttachment(MultipartFile multipartFile) throws IOException {
        if (Objects.equals(multipartFile.getOriginalFilename(), ""))
            return null;
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        String originalFileName = this.fileService.uploadImage(path, multipartFile);
        attachmentDTO.setAttachmentFileName(originalFileName);

        String extension = FilenameUtils.getExtension(originalFileName);
        attachmentDTO.setAttachmentType(extension);

        attachmentDTO.setAttachmentPath(path);

        return attachmentDTO;
    }
}
