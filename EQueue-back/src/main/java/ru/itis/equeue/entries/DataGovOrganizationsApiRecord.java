package ru.itis.equeue.entries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataGovOrganizationsApiRecord {
    @JsonProperty("id")
    private String inn;

    @JsonProperty("title")
    private String companyName;

}