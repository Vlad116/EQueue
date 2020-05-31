package ru.itis.equeue.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.itis.equeue.security.role.Role;

import javax.persistence.*;
import java.util.List;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "service_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private String login;
    private String phoneNumber;
    private String hashPassword;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private Boolean isUserNonLocked;
    private Boolean isEmailConfirmed;

    @JsonIgnore
    private String confirmString;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Event> events;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

}