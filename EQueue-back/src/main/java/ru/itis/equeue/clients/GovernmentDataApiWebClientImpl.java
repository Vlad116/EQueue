package ru.itis.equeue.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.itis.equeue.entries.CompaniesData;
import ru.itis.equeue.entries.DataGovOrganizationsApiRecord;

import java.util.Arrays;

@Component
public class GovernmentDataApiWebClientImpl implements CompaniesDataClient {

    // реактивная штука для отправки запросов на другие сервисы
    private WebClient client;

    public GovernmentDataApiWebClientImpl(@Value("${governmentdata.url}") String url) {
        client = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
                        .build())
                .baseUrl(url)
                .build();
    }

    @Override
    public Flux<CompaniesData> getAll() {
        return client.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(DataGovOrganizationsApiRecord[].class)) // преобразуем данные со стороннего сервреа в Publisher
                .flatMapIterable(Arrays::asList) // выполняем конвертацию данных с другого сервера в наши, возвращаем мы набор Publisher-ов(?) каждый из которых возвращает объект CovidStatistic
                .map(record ->
                        CompaniesData.builder()
                                .inn(record.getInn())
                                .companyName(record.getCompanyName())
//                                .companyEmail("...")
//                                .phoneNumber("...")
                                .from("https://data.gov.ru/api/json/organization")
                                .build());
    }
}