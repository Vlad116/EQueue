package ru.itis.equeue.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.itis.equeue.models.Event;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface EventsRepository extends PagingAndSortingRepository<Event, Long> {

    @RestResource(path = "free", rel = "free")
    @Query("from Event event where event.state = 'FREE'")
    Page<Event> findAllFree(Pageable pageable);

    @RestResource(path = "finished", rel = "finished")
    @Query("from Event event where event.state = 'FINISHED'")
    Page<Event> findAllFinished(Pageable pageable);

    Optional<Event> findEventById(Long id);

    List<Event> findAllByTitle(String title);

    Event findByEventLineNumber(Integer eventLineNumber);

}