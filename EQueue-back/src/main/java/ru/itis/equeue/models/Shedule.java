package ru.itis.equeue.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "company_shedules")
public class Shedule {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shedule")
    private List<Event> events;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

}
