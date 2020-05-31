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
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue
    private Long id;

    private String inn;
    private String companyName;
    private String companyEmail;
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Shedule> shedules;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Requisite> requisites;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Document> documents;

}
