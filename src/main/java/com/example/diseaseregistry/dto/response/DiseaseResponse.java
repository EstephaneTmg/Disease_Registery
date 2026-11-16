package com.example.diseaseregistry.dto.response;

import java.time.Instant;
import java.util.List;

public record DiseaseResponse(
        String id,
        String code,
        String nom,
        String englishName,
        String description,
        List<String> synonyms,
        String icd10Code,
        String icd11Code,
        String severity,
        String categoryId,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}