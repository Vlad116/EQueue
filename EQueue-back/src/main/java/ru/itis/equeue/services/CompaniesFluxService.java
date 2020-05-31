package ru.itis.equeue.services;

import reactor.core.publisher.Flux;
import ru.itis.equeue.entries.CompaniesData;

public interface CompaniesFluxService {
    Flux<CompaniesData> getAll();
}
