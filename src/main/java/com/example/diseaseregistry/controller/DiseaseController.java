package com.example.diseaseregistry.controller;

import com.example.diseaseregistry.dto.request.DiseaseRequest;
import com.example.diseaseregistry.dto.response.DiseaseResponse;
import com.example.diseaseregistry.service.DiseaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diseases")
@RequiredArgsConstructor
public class DiseaseController {

    private final DiseaseService diseaseService;

    @GetMapping
    public ResponseEntity<Page<DiseaseResponse>> getAllDiseases(
            @PageableDefault(size = 20, sort = "nom", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(diseaseService.getAllDiseases(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiseaseResponse> getDiseaseById(@PathVariable String id) {
        return ResponseEntity.ok(diseaseService.getDiseaseById(id));
    }

    @PostMapping
    public ResponseEntity<DiseaseResponse> addDisease(
            @Valid @RequestBody DiseaseRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(diseaseService.addDisease(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiseaseResponse> updateDisease(
            @PathVariable String id,
            @Valid @RequestBody DiseaseRequest request
    ) {
        return ResponseEntity.ok(diseaseService.updateDisease(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisease(@PathVariable String id) {
        diseaseService.deleteDisease(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<DiseaseResponse>> getDiseasesByCategory(
            @PathVariable String categoryId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(
                diseaseService.getDiseasesByCategory(categoryId, pageable)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DiseaseResponse>> searchDiseases(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(diseaseService.searchDiseases(keyword, pageable));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archiveDisease(@PathVariable String id) {
        diseaseService.archiveDisease(id);
        return ResponseEntity.noContent().build();
    }

   
}