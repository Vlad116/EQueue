package ru.itis.equeue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.itis.equeue.models.Requisite;

@RepositoryRestResource
public interface RequisiteRepository extends JpaRepository<Requisite, Long> {
}