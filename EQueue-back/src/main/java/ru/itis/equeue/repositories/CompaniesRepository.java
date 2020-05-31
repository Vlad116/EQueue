package ru.itis.equeue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.equeue.models.Company;

public interface CompaniesRepository extends JpaRepository<Company, Long> {
}