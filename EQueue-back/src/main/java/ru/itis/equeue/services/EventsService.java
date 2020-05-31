package ru.itis.equeue.services;

import ru.itis.equeue.models.Event;

public interface EventsService {
    Event appointment(Long eventId);
    void appointmentToUser(Long eventId, Long userId);
    void notification(Long eventId, Long userId);
}
