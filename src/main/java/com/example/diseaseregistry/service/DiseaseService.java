package com.example.diseaseregistry.service;

import com.example.diseaseregistry.dto.request.DiseaseRequest;
import com.example.diseaseregistry.dto.response.DiseaseResponse;
import com.example.diseaseregistry.enums.DiseaseSeverity;
import com.example.diseaseregistry.enums.RecordStatus;
import com.example.diseaseregistry.exception.DuplicateResourceException;
import com.example.diseaseregistry.exception.ResourceNotFoundException;
import com.example.diseaseregistry.model.Category;
import com.example.diseaseregistry.model.Disease;
import com.example.diseaseregistry.repository.CategoryRepository;
import com.example.diseaseregistry.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final CategoryRepository categoryRepository;

    public Page<DiseaseResponse> getAllDiseases(Pageable pageable) {
        return diseaseRepository.findAll(pageable).map(this::toResponse);
    }

    public DiseaseResponse getDiseaseById(String id) {
        return toResponse(findDisease(id));
    }

    public DiseaseResponse addDisease(DiseaseRequest request) {

        if (diseaseRepository.existsByCodeIgnoreCase(request.code())) {
            throw new DuplicateResourceException("Disease code already exists.");
        }

        Category category = findCategory(request.categoryId());

        validateCategory(category);

        Disease disease = new Disease();

        disease.setCode(request.code().trim().toUpperCase());
        disease.setNom(request.nom().trim());
        disease.setEnglishName(request.englishName());
        disease.setDescription(request.description());
        disease.setSynonyms(request.synonyms());
        disease.setIcd10Code(request.icd10Code());
        disease.setIcd11Code(request.icd11Code());
        disease.setSeverity(request.severity());
        disease.setCategoryId(category.getId());
        disease.setStatus(RecordStatus.ACTIVE);

        return toResponse(diseaseRepository.save(disease));
    }

    public DiseaseResponse updateDisease(String id, DiseaseRequest request) {

        Disease disease = findDisease(id);

        if (diseaseRepository.existsByCodeIgnoreCaseAndIdNot(request.code(), id)) {
            throw new DuplicateResourceException("Disease code already exists.");
        }

        Category category = findCategory(request.categoryId());

        validateCategory(category);

        disease.setCode(request.code().trim().toUpperCase());
        disease.setNom(request.nom().trim());
        disease.setEnglishName(request.englishName());
        disease.setDescription(request.description());
        disease.setSynonyms(request.synonyms());
        disease.setIcd10Code(request.icd10Code());
        disease.setIcd11Code(request.icd11Code());
        disease.setSeverity(request.severity());
        disease.setCategoryId(category.getId());

        return toResponse(diseaseRepository.save(disease));
    }

    public void deleteDisease(String id) {
        Disease disease = findDisease(id);
        diseaseRepository.delete(disease);
    }

    public Page<DiseaseResponse> getDiseasesByCategory(
            String categoryId,
            Pageable pageable
    ) {
        findCategory(categoryId);

        return diseaseRepository.findByCategoryId(categoryId, pageable)
                .map(this::toResponse);
    }

    public Page<DiseaseResponse> searchDiseases(String keyword, Pageable pageable) {

        return diseaseRepository
                .findByNomContainingIgnoreCaseOrCodeContainingIgnoreCase(
                        keyword, keyword, pageable
                )
                .map(this::toResponse);
    }

    public void archiveDisease(String id) {
        Disease disease = findDisease(id);
        disease.setStatus(RecordStatus.ARCHIVED);
        diseaseRepository.save(disease);
    }

    private Disease findDisease(String id) {
        return diseaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease not found: " + id));
    }

    private Category findCategory(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    private void validateCategory(Category category) {
        if (category.getStatus() != RecordStatus.ACTIVE) {
            throw new IllegalStateException("Cannot assign a disease to an inactive category.");
        }
    }

    private DiseaseResponse toResponse(Disease disease) {
    return new DiseaseResponse(
            disease.getId(),
            disease.getCode(),
            disease.getNom(),
            disease.getEnglishName(),
            disease.getDescription(),
            disease.getSynonyms(),
            disease.getIcd10Code(),
            disease.getIcd11Code(),
            disease.getSeverity() != null ? disease.getSeverity().name() : null,
            disease.getCategoryId(),
            disease.getStatus() != null ? disease.getStatus().name() : null,
            disease.getCreatedAt(),
            disease.getUpdatedAt()
        );
    }
}