package ru.itis.equeue.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.equeue.dto.DocumentDto;
import ru.itis.equeue.models.Document;
import ru.itis.equeue.repositories.UsersRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
public class GcsStorageServiceImpl implements GcsStorageService {

    // Use this variation to read the Google authorization JSON from the resources directory with a path
    // and a project name.
    @Value("${credentials.location}")
    String keyPath;

    // The ID of your GCP project
    @Value("${spring.cloud.gcp.project-id}")
    String projectId;

    // The ID of your GCS bucket
    @Value("${spring.couchbase.bucket.name}")
    String bucketName;

    private Storage storage;
    private Bucket bucket;

//    @Autowired
//    private DocumentService documentService;
//
//    @Autowired
//    private UsersService usersService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void avatarUpload(MultipartFile avatar) throws IOException {

        // Use this variation to read the Google authorization JSON from the resources directory with a path
        // and a project name.
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));
        storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();

        String originalFilename = avatar.getOriginalFilename();

        System.out.println(avatar.getName());

        byte[] fileToUpload = avatar.getBytes();

        String objectName = "avatars/" + originalFilename;

        String url = "https://storage.cloud.google.com/equeque-file-storage/" + objectName + "?cloudshell=false";
        System.out.println(url);

        BlobId blobId = BlobId.of(bucketName, objectName);

        //  Туть можем добавить все метаданные (Content/type encoding и прочее)
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(avatar.getContentType())
                .build();

        storage.create(blobInfo, fileToUpload);

        System.out.println(
                "Avatar " + avatar.getOriginalFilename() + " with content type "
                        + avatar.getContentType() + " uploaded to bucket "
                        + bucketName + " as " + objectName);

        // userId " " + pathToFile - format
        Long userId = 5L;
        amqpTemplate.convertAndSend("avatar_upload", userId + " " + url);
    }

    @Override
    public String documentUpload(MultipartFile file) throws IOException {

        // Use this variation to read the Google authorization JSON from the resources directory with a path
        // and a project name.
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));
        storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        if (extension.equals("")) {
            extension = "unknown";
        }

        System.out.println(file.getName());
        DocumentDto document = DocumentDto.builder()
                .documentName(originalFilename)
                .documentType(extension)
                .build();

        byte[] fileToUpload = file.getBytes();
        String objectName = document.getDocumentType() + "/" + document.getDocumentName();

        String url = "https://storage.cloud.google.com/equeque-file-storage/" + objectName + "?cloudshell=false";

        System.out.println(url);
        document.setPathToFile(url);

        BlobId blobId = BlobId.of(bucketName, objectName);

        //  Туть можем добавить все метаданные (Content/type encoding и прочее)
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(document.getDocumentType())
                .build();

        storage.create(blobInfo, fileToUpload);

        System.out.println(
                "File " + document.getDocumentName() + " with content type "
                        + document.getDocumentType() + " uploaded to bucket "
                        + bucketName + " as " + document.getDocumentName());

        // documentName + " " + documentType + " " + pathToFile - format
        amqpTemplate.convertAndSend("document_upload", document.toString());

        return "File " + document.getDocumentName() + "with content type "
                + document.getDocumentType() + " uploaded to bucket "
                + bucketName + " as " + document.getDocumentName();
    }

    @Override
    public String documentsUpload(MultipartFile[] documents) throws IOException {

        boolean isFailed = true;

        for (MultipartFile document : documents) {
            System.out.println(document.getName());
            documentUpload(document);
            isFailed = false;
        }

        if (isFailed) {
            return "You didn't uploaded all files!";
        }

//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded all files!");
        return "You successfully uploaded all files!";
    }

//    @Override
//    public void photosUpload(MultipartFile[] document) {
//
//    }
}
