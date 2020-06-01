package ru.itis.equeue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import ru.itis.equeue.config.RabbitConfiguration;
import ru.itis.equeue.dto.UserDto;
import ru.itis.equeue.models.*;
import ru.itis.equeue.repositories.CompaniesRepository;
import ru.itis.equeue.repositories.EventsRepository;
import ru.itis.equeue.repositories.ShedulesRepository;
import ru.itis.equeue.repositories.UsersRepository;
import ru.itis.equeue.services.EventsService;
import ru.itis.equeue.services.UsersService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Arrays.asList;


@SpringBootApplication
@ComponentScan("ru.itis")
@EnableJpaRepositories(basePackages = "ru.itis.equeue.repositories")
@EntityScan(basePackages = "ru.itis.equeue.models", basePackageClasses = Jsr310JpaConverters.class)
@Import(RabbitConfiguration.class)
public class EQueueHateoasRestServiceApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(EQueueHateoasRestServiceApplication.class, args);

        UsersRepository usersRepository = context.getBean(UsersRepository.class);
        CompaniesRepository companiesRepository = context.getBean(CompaniesRepository.class);
        EventsRepository eventsRepository = context.getBean(EventsRepository.class);
        ShedulesRepository shedulesRepository = context.getBean(ShedulesRepository.class);
        UsersService usersService = context.getBean(UsersService.class);
        EventsService eventsService = context.getBean(EventsService.class);

        Company clinic = Company.builder()
                .companyName("Инфекционная больница")
                .companyEmail("infection@gmail.ru")
                .phoneNumber("89871233216")
                .users(Collections.emptyList())
                .shedules(Collections.emptyList())
                .build();

        companiesRepository.save(clinic);


        GregorianCalendar gCalendarStartTime = new GregorianCalendar();
        gCalendarStartTime.set(2020, Calendar.MARCH, 28, 10, 0, 0);

        GregorianCalendar gCalendarRecordingIsAvailableUntil = new GregorianCalendar();
        gCalendarRecordingIsAvailableUntil.set(2020, Calendar.MARCH, 28, 8, 30, 0);

        gCalendarStartTime.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
        gCalendarRecordingIsAvailableUntil.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));

        LocalDateTime date = new Timestamp(gCalendarStartTime.getTimeInMillis()).toLocalDateTime();
        int nano = date.getNano();

        System.out.println(nano);
        System.out.println("Время: " + date);

        System.out.println(gCalendarRecordingIsAvailableUntil.toString());
        System.out.println(new Timestamp(gCalendarRecordingIsAvailableUntil.getTimeInMillis()));

        Event firstCOVIDTest = Event.builder()
                .title("Covid test 10:00")
                .eventLineNumber(1)
                .eventDescription("Тест на 'корону'")
                .eventStartTime(date.minusNanos(nano))
                .recordingIsAvailableUntil(new Timestamp(gCalendarRecordingIsAvailableUntil.getTimeInMillis()).toLocalDateTime())
                .averageDuration(15L)
                .realDuration(9L)
                .state(State.FREE)
                .build();

        gCalendarStartTime.set(2020, Calendar.MARCH, 28, 12, 0, 0);
        gCalendarRecordingIsAvailableUntil.set(2020, Calendar.MARCH, 28, 10, 30, 0);

        System.out.println("New: " + new Timestamp(gCalendarStartTime.getTimeInMillis()));

        Event secondCOVIDTest = Event.builder()
                .title("Covid test 12:00")
                .eventLineNumber(2)
                .eventDescription("Тест на 'корону', второй по записи")
                .eventStartTime(new Timestamp(gCalendarStartTime.getTimeInMillis()).toLocalDateTime())
                .recordingIsAvailableUntil(new Timestamp(gCalendarRecordingIsAvailableUntil.getTimeInMillis()).toLocalDateTime())
                .averageDuration(15L)
                .realDuration(8L)
                .state(State.FREE)
                .build();

        gCalendarStartTime.set(2020, Calendar.MARCH, 28, 14, 0, 0);
        gCalendarRecordingIsAvailableUntil.set(2020, Calendar.MARCH, 28, 12, 30, 0);

        Event thirdCOVIDTest = Event.builder()
                .title("Covid test 14:00")
                .eventLineNumber(3)
                .eventDescription("Запись на тест на 'корону'")
                .eventStartTime(new Timestamp(gCalendarStartTime.getTimeInMillis()).toLocalDateTime())
                .recordingIsAvailableUntil(new Timestamp(gCalendarRecordingIsAvailableUntil.getTimeInMillis()).toLocalDateTime())
                .averageDuration(15L)
                .realDuration(5L)
                .state(State.FREE)
                .build();

        gCalendarStartTime.set(2020, Calendar.APRIL, 1, 8, 0, 0);
        gCalendarRecordingIsAvailableUntil.set(2020, Calendar.MARCH, 31, 24, 0, 0);

        Event firstFLUETest = Event.builder()
                .title("Flu shot")
                .eventLineNumber(1)
                .eventDescription("Прививка от гриппа, плановая")
                .eventStartTime(new Timestamp(gCalendarStartTime.getTimeInMillis()).toLocalDateTime())
                .recordingIsAvailableUntil(new Timestamp(gCalendarRecordingIsAvailableUntil.getTimeInMillis()).toLocalDateTime())
                .averageDuration(15L)
                .realDuration(9L)
                .state(State.FREE)
                .build();

        eventsRepository.saveAll(asList(firstCOVIDTest,
                firstFLUETest,
                secondCOVIDTest,
                thirdCOVIDTest));

        Shedule covidTestShedule = Shedule.builder()
                .description("Запись на сдачу анализа на наличие COVID-19")
                .title("Запись на анализ COVID-19")
                .build();

        Shedule flue = Shedule.builder()
                .description("Плановые прививки от ОРВИ")
                .title("Прививки от ОРВИ")
                .build();

        shedulesRepository.saveAll(asList(
                covidTestShedule, flue
        ));

        clinic.setShedules(asList(covidTestShedule, flue));

        covidTestShedule.setCompany(clinic);
        flue.setCompany(clinic);


        firstCOVIDTest.setShedule(covidTestShedule);
        secondCOVIDTest.setShedule(covidTestShedule);
        thirdCOVIDTest.setShedule(covidTestShedule);

        firstFLUETest.setShedule(flue);

        eventsRepository.saveAll(asList(firstCOVIDTest, secondCOVIDTest, thirdCOVIDTest, firstFLUETest));

        covidTestShedule.setEvents(asList(firstCOVIDTest,
                secondCOVIDTest,
                thirdCOVIDTest));

        covidTestShedule.setEvents(Collections.singletonList(firstFLUETest));

        shedulesRepository.saveAll(asList(
                covidTestShedule, flue
        ));

        User vlad = User.builder()
                .firstName("Vladislav")
                .lastName("Alekseev")
                .email("vladalekseev9@yandex.ru")
                .login("Vlad16")
                .phoneNumber("89543322876")
                .hashPassword("pswd123")
                .events(asList(firstFLUETest, secondCOVIDTest))
                .build();


//        Optional<User> vlad = usersRepository.findUserById(192L);
//        Optional<User> daria = usersRepository.findUserById(337L);
//        UserDto testuser = UserDto.from(vlad.get());
//        UserDto testuser2 = UserDto.from(daria.get());

        usersService.registration(UserDto.from(vlad));

        Long userIdForTestAppointment = usersRepository.getByEmailIgnoreCase("vladalekseev9@yandex.ru").get().getId();

        eventsService.appointmentToUser(firstFLUETest.getId(), userIdForTestAppointment);
        eventsService.appointmentToUser(firstCOVIDTest.getId(), userIdForTestAppointment);
        eventsService.appointmentToUser(secondCOVIDTest.getId(), userIdForTestAppointment);
    }
}
