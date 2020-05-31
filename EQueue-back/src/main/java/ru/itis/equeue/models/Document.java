package ru.itis.equeue.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "company_documents")
public class Document {
    @GeneratedValue
    @Id
    Long id;

    String documentName;
    String documentType;
    String pathToFile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;
}