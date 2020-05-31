package ru.itis.equeue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.itis.equeue.models.Document;

@RepositoryRestResource
public interface DocumentRepository extends JpaRepository<Document, Long> {
}