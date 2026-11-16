package com.example.diseaseregistry.model;

import com.example.diseaseregistry.enums.RecordStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    @Indexed(unique = true)
    private String code;

    @Indexed(unique = true)
    private String name;

    private String description;

    private RecordStatus status = RecordStatus.ACTIVE;
}