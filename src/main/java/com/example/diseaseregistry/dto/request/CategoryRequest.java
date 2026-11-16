package com.example.diseaseregistry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Category code is required")
        @Size(max = 30)
        String code,

        @NotBlank(message = "Category name is required")
        @Size(max = 150)
        String name,

        @Size(max = 1000)
        String description
) {}