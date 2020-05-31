package ru.itis.equeue.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private Integer eventLineNumber;
    private String title;
    private String eventDescription;
    private LocalDateTime eventStartTime;
    private LocalDateTime recordingIsAvailableUntil;

    private Long averageDuration;
    private Long realDuration;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shedule_id")
    private Shedule shedule;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public void appointment() {
        if (this.state.equals(State.FREE)) {
            this.state = State.ASSIGNED;
        } else if (this.state.equals(State.FINISHED)) {
            throw new IllegalStateException();
        }
    }

}