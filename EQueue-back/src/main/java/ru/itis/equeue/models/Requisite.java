package ru.itis.equeue.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "company_requisites")
public class Requisite {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

// Адрес регистрации
    String legalAddress;
// Фактический адрес
    String actualAddress;
// ИНН
    String TIN;
// КПП
    String KPP;
// ОГ
    String PSRN;
// ОКВЭД - основной вид деятельности (код)
    String OKVED;
// Реквизиты счета
}
