package com.example.diseaseregistry.service;

import com.example.diseaseregistry.dto.request.CategoryRequest;
import com.example.diseaseregistry.dto.response.CategoryResponse;
import com.example.diseaseregistry.enums.RecordStatus;
import com.example.diseaseregistry.exception.DuplicateResourceException;
import com.example.diseaseregistry.exception.ResourceNotFoundException;
import com.example.diseaseregistry.model.Category;
import com.example.diseaseregistry.repository.CategoryRepository;
import com.example.diseaseregistry.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DiseaseRepository diseaseRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(String id) {
        return toResponse(findCategory(id));
    }

    public CategoryResponse addCategory(CategoryRequest request) {

        if (categoryRepository.existsByCodeIgnoreCase(request.code())) {
            throw new DuplicateResourceException("Category code already exists.");
        }

        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Category name already exists.");
        }

        Category category = new Category();
        category.setCode(request.code().trim().toUpperCase());
        category.setName(request.name().trim());
        category.setDescription(request.description());
        category.setStatus(RecordStatus.ACTIVE);

        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse updateCategory(String id, CategoryRequest request) {

        Category category = findCategory(id);

        if (categoryRepository.existsByCodeIgnoreCaseAndIdNot(request.code(), id)) {
            throw new DuplicateResourceException("Category code already exists.");
        }

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new DuplicateResourceException("Category name already exists.");
        }

        category.setCode(request.code().trim().toUpperCase());
        category.setName(request.name().trim());
        category.setDescription(request.description());

        return toResponse(categoryRepository.save(category));
    }

    public void archiveCategory(String id) {
        Category category = findCategory(id);
        category.setStatus(RecordStatus.ARCHIVED);
        categoryRepository.save(category);
    }

    public void deleteCategory(String id) {

        findCategory(id);

        if (diseaseRepository.existsByCategoryId(id)) {
            throw new IllegalStateException(
                    "Cannot delete category because it contains registered diseases. Archive it instead."
            );
        }

        categoryRepository.deleteById(id);
    }

    private Category findCategory(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getDescription(),
                category.getStatus().name(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}