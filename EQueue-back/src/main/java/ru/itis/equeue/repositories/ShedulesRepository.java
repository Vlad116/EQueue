package ru.itis.equeue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.equeue.models.Shedule;

public interface ShedulesRepository extends JpaRepository<Shedule, Long> {
}