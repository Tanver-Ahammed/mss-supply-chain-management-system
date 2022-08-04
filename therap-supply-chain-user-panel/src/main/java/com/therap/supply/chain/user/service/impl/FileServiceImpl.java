package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@Service
public class FileServiceImpl implements FileService {

    // upload file
    @Override
    public String uploadImage(String path, MultipartFile multipartFile) throws IOException {

        // file name
        String name = multipartFile.getOriginalFilename();

        // random name generate file name
        String randomId = UUID.randomUUID().toString();
        assert name != null;
        String fileName = name + randomId.concat(name.substring(name.lastIndexOf(".")));

        // full path
        String filePath = path + File.separator + fileName;

        // generate folder if you are not created
        File file = new File(path);

        if (!file.exists())
            file.mkdir();

        // file copy
        Files.copy(multipartFile.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    // delete file
    @Override
    public boolean deleteImage(String path, String imageName) throws IOException {
        String imagePath = path + File.separator + imageName;
        File file = new File(imagePath);
        return file.delete();
    }

    // get file
    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        return new FileInputStream(fullPath);
    }
}
