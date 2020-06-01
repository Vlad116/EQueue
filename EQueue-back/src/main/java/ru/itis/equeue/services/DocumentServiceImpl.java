package ru.itis.equeue.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.equeue.dto.DocumentDto;
import ru.itis.equeue.models.Document;
import ru.itis.equeue.repositories.DocumentRepository;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @RabbitListener(queues = "document_upload")
    @Override
    public void addDocument(String documentInfo) {
        System.out.println("Recieve docInfo in DocumentService: " + documentInfo);
        // documentName + " " + documentType + " " + pathToFile - format
        String[] infoForNewDoc = documentInfo.split(" ");

        String documentName = infoForNewDoc[0];
        String documentType = infoForNewDoc[1];
        String pathToFile = infoForNewDoc[2];

        Document document = documentRepository.save(Document.builder()
                .documentName(documentName)
                .documentType(documentType)
                .pathToFile(pathToFile)
                .build());
        System.out.println("Document with name " + documentName + " save to DB");
    }

    @Override
    public void addDocuments(List<DocumentDto> documents) {

    }
}
