package ru.itis.equeue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import ru.itis.equeue.controllers.EventsController;
import ru.itis.equeue.models.Event;
import ru.itis.equeue.models.State;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EventsRepresentationProcessor implements RepresentationModelProcessor<EntityModel<Event>> {

    @Autowired
    private RepositoryEntityLinks links;

    @Override
    public EntityModel<Event> process(EntityModel<Event> model) {

        Event event = model.getContent();

        if (event != null && event.getState().equals(State.FREE)) {
            model.add(linkTo(methodOn(EventsController.class).appointment(event.getId())).withRel("appointment"));
        }

        if (event != null && event.getState().equals(State.ASSIGNED)) {
            model.add(links.linkToItemResource(Event.class, event.getId()).withRel("delete"));
        }

        return model;
    }
}