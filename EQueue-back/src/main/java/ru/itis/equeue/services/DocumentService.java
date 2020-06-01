package ru.itis.equeue.services;

import ru.itis.equeue.dto.DocumentDto;

import java.util.List;

public interface DocumentService {
    void addDocument(String documentInfo);

    void addDocuments(List<DocumentDto> documents);
}
