package com.therap.supply.chain.user.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

public interface FileService {

    // upload file
    String uploadImage(String path, MultipartFile multipartFile) throws IOException;

    // delete file
    boolean deleteImage(String path, String imageName) throws IOException;

    // get file
    InputStream getResource(String path, String fileName) throws FileNotFoundException;

}
