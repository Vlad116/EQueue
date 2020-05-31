package ru.itis.equeue.entries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompaniesData {
    private String inn;
    private String companyName;
//    private String companyEmail;
//    private String phoneNumber;
    private String from;
}
