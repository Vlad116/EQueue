package ru.itis.equeue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.equeue.models.Document;
import ru.itis.equeue.models.User;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {

    private Long id;
    private String documentName;
    private String documentType;
    private String pathToFile;
//    private Long companyId;

    public static DocumentDto from(Document document) {
        return DocumentDto.builder()
                .id(document.getId())
                .documentName(document.getDocumentName())
                .documentType(document.getDocumentType())
                .pathToFile(document.getPathToFile())
                .build();
    }

    @Override
    public String toString() {
//        id + " " +
        return documentName + " " + documentType + " " + pathToFile;
    }
}