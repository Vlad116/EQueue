package ru.itis.equeue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import ru.itis.equeue.clients.CompaniesDataClient;
import ru.itis.equeue.entries.CompaniesData;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompaniesFluxServiceImpl implements CompaniesFluxService {

    @Autowired
    private List<CompaniesDataClient> clients;

    @Override
    public Flux<CompaniesData> getAll() {
        List<Flux<CompaniesData>> fluxes =  clients.stream().map(this::getAll).collect(Collectors.toList());
        return Flux.merge(fluxes);
    }

    private Flux<CompaniesData> getAll(CompaniesDataClient client) {
        return client.getAll().subscribeOn(Schedulers.elastic());
    }
}
