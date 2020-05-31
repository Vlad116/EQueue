package ru.itis.equeue.clients;

import reactor.core.publisher.Flux;
import ru.itis.equeue.entries.CompaniesData;

public interface CompaniesDataClient {
    Flux<CompaniesData> getAll();
}
