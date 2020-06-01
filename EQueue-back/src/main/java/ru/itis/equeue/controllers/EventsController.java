package ru.itis.equeue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.equeue.services.EventsService;

@RepositoryRestController
public class EventsController {

    @Autowired
    private EventsService eventsService;

    // id userа назначившегося на эвент, event-id - id мероприятия
    @RequestMapping(value = "/events/{event-id}/appointment/", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity<?> appointment(@PathVariable("event-id") Long eventId) {
        return ResponseEntity.ok(
                new EntityModel<>(
                        eventsService.appointment(eventId)
                )
        );
    }

    @RequestMapping(value = "/events/{event-id}/delete", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> delete(@PathVariable("event-id") Long eventId) {
        return ResponseEntity.ok(
                new EntityModel<>(
                        eventsService.delete(eventId)
                )
        );
    }

//    @RequestMapping(value = "/events/{event-id}/appointment/{user-id}", method = RequestMethod.PUT)
//    public @ResponseBody
//    ResponseEntity<?> appointment(@PathVariable("event-id") Long eventId, @PathVariable("user-id") Long userId) {
//        return ResponseEntity.ok(
//                new EntityModel<>(
//                        eventsService.appointmentToUser(eventId, userId)
//                )
//        );
//    }

}