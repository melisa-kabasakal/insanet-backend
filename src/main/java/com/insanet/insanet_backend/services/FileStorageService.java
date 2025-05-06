package com.insanet.insanet_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    public String saveFile(MultipartFile file) {

        return "fileUrl";
    }

    public byte[] getFile(String fileUrl) {

        return new byte[0];
    }
}

