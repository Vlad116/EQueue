package ru.itis.equeue.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface GcsStorageService {

    void avatarUpload(MultipartFile avatar) throws IOException;

    //    byte []
    String documentUpload(MultipartFile document) throws IOException;

    //   ArrayList<byte []>
    String documentsUpload(MultipartFile[] document) throws IOException;

//    void photosUpload(MultipartFile[] document);

//    void avatarDelete
}