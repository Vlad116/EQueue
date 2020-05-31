package ru.itis.equeue.clients;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.itis.equeue.entries.CompaniesData;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Component
public class EqueueCompaniesDBClientImpl implements CompaniesDataClient {

    private ConnectionFactory connectionFactory;

    public EqueueCompaniesDBClientImpl() {
        ConnectionFactoryOptions options = builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, 5432)
                .option(USER, "postgres")
                .option(PASSWORD, "logistic")
                .option(DATABASE, "auto_query")
                .build();
        this.connectionFactory = ConnectionFactories.get(options);
    }

    @Override
    public Flux<CompaniesData> getAll() {
        DatabaseClient client = DatabaseClient.create(connectionFactory);
        return client.execute("select * from \"companies\"").as(CompaniesData.class).fetch().all()
                .map(record -> CompaniesData.builder()
                                .inn(record.getInn())
                                .companyName(record.getCompanyName())
//                                .companyEmail("...")
//                                .phoneNumber("...")
                                .from("Equeue DB")
                                .build()
                );
    }

}