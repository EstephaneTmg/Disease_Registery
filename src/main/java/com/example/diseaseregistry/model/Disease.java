package com.example.diseaseregistry.model;

import com.example.diseaseregistry.enums.DiseaseSeverity;
import com.example.diseaseregistry.enums.RecordStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "diseases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Disease extends BaseEntity {

    @Indexed(unique = true)
    private String code;

    @Indexed
    private String nom;

    private String englishName;

    private String description;

    private List<String> synonyms;

    private String icd10Code;

    private String icd11Code;

    private DiseaseSeverity severity;

    private String categoryId;

    private RecordStatus status = RecordStatus.ACTIVE;
}