package com.example.diseaseregistry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

import com.example.diseaseregistry.enums.DiseaseSeverity;

public record DiseaseRequest(

        @NotBlank(message = "Disease code is required")
        @Size(max = 50)
        String code,

        @NotBlank(message = "Disease name is required")
        @Size(max = 200)
        String nom,

        String englishName,

        @Size(max = 2000)
        String description,

        List<String> synonyms,

        String icd10Code,

        String icd11Code,

        @NotNull(message = "Disease severity is required")
        DiseaseSeverity severity,

        @NotBlank(message = "Category ID is required")
        String categoryId
) {}