package ru.itis.equeue.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.equeue.models.Event;
import ru.itis.equeue.models.User;
import ru.itis.equeue.repositories.EventsRepository;
import ru.itis.equeue.repositories.UsersRepository;

import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void appointmentToUser(Long eventId, Long userId) {
        Event event = eventsRepository.findById(eventId).orElseThrow(IllegalArgumentException::new);
        User user = usersRepository.findUserById(userId).orElseThrow(IllegalArgumentException::new);
        List<Event> userEvents = user.getEvents();
        userEvents.add(event);
        user.setEvents(userEvents);
        usersRepository.save(user);

        event.setUser(user);
        event.appointment();
        eventsRepository.save(event);
        notification(eventId, userId);
    }

    @Override
    public Event appointment(Long eventId) {
        Event event = eventsRepository.findById(eventId).orElseThrow(IllegalArgumentException::new);
        event.appointment();
        eventsRepository.save(event);
        return event;
    }

    @Override
    public void notification(Long eventId, Long userId) {
        amqpTemplate.convertAndSend("event_notification",eventId + " " + userId);
    }


}
