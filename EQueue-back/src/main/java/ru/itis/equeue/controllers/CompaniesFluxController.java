package ru.itis.equeue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.itis.equeue.entries.CompaniesData;
import ru.itis.equeue.services.CompaniesFluxService;


@RestController
public class CompaniesFluxController {

    @Autowired
    private CompaniesFluxService companiesService;

    @RequestMapping(value = "/companies",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            method = RequestMethod.GET)
    public Flux<CompaniesData> getAll() {
        return companiesService.getAll();
    }

}
