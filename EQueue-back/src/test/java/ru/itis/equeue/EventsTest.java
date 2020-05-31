package ru.itis.equeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.itis.equeue.models.Event;
import ru.itis.equeue.models.State;
import ru.itis.equeue.services.EventsService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class EventsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventsService eventsService;

    @BeforeEach
    public void setUp() {
        when(eventsService.appointment(1L)).thenReturn(assignedEvent());
    }

    @Test
    public void eventAppointmentTest() throws Exception {
        mockMvc.perform(put("/events/1/appointment")).andDo(print())
                .andExpect(status().isOk())
//                .andExpect(jsonPath())
                .andExpect(jsonPath("$.title").value(assignedEvent().getTitle()))
                .andExpect(jsonPath("$.eventDescription").value(assignedEvent().getEventDescription()))
                .andExpect(jsonPath("$.state").value(assignedEvent().getState()))
                .andExpect(jsonPath("$.eventLineNumber").value(assignedEvent().getEventLineNumber()))
                .andExpect(jsonPath("$.averageDuration").value(assignedEvent().getAverageDuration()))
                .andExpect(jsonPath("$.realDuration").value(assignedEvent().getRealDuration()))
                .andExpect(jsonPath("$.eventStartTime").value(assignedEvent().getEventStartTime()))
                .andExpect(jsonPath("$.recordingIsAvailableUntil").value(assignedEvent().getRecordingIsAvailableUntil()))
                .andDo(document("appointment_event",
                        links(halLinks(),
                                linkWithRel("appointment").description("Appointment this event"),
                                linkWithRel("self").optional().description("This event")
                        ),
                        responseFields(
                                fieldWithPath("title").description("Название мероприятия"),
                                fieldWithPath("eventDescription").description("Описание мероприятия"),
                                fieldWithPath("state").description("Состояние записи"),
                                fieldWithPath("eventLineNumber").description("Номер в очереди"),
                                fieldWithPath("averageDuration").description("Ожидаемая продолжительность приема"),
                                fieldWithPath("realDuration").description("Реальная продолжительность приема"),
                                fieldWithPath("eventStartTime").description("Время приема (начало)"),
                                fieldWithPath("recordingIsAvailableUntil").description("До какого времени место в очереди доступно для записи"),
                                fieldWithPath("_links.appointment.href").description("To event appointment link")
                        )
                ));
    }

    private Event assignedEvent() {

        GregorianCalendar gCalendarStartTime = new GregorianCalendar();
        gCalendarStartTime.set(2020, Calendar.MARCH, 28, 10, 0, 0);

        GregorianCalendar gCalendarRecordingIsAvailableUntil = new GregorianCalendar();
        gCalendarRecordingIsAvailableUntil.set(2020, Calendar.MARCH, 28, 8, 30, 0);

        gCalendarStartTime.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
        gCalendarRecordingIsAvailableUntil.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));

        LocalDateTime date = new Timestamp(gCalendarStartTime.getTimeInMillis()).toLocalDateTime();

        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.MARCH, 28, 10, 0, 0);
        int nano = localDateTime.getNano();

        System.out.println(nano);
        System.out.println("Время: " + date);
        System.out.println(date.minusNanos(nano).withSecond(0));

        return Event.builder()
                .id(1L)
                .title("Covid test 10:00")
                .eventLineNumber(1)
                .eventDescription("Test on 'corona'")
//                .eventStartTime()
//                .recordingIsAvailableUntil(new Timestamp(gCalendarRecordingIsAvailableUntil.getTimeInMillis()).toLocalDateTime())
                .averageDuration(15L)
                .realDuration(9L)
                .state(State.FREE)
                .build();
    }
}