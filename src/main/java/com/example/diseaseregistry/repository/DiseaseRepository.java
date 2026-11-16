package com.example.diseaseregistry.repository;

import com.example.diseaseregistry.model.Disease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiseaseRepository extends MongoRepository<Disease, String> {

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, String id);

    boolean existsByCategoryId(String categoryId);

    Page<Disease> findByCategoryId(String categoryId, Pageable pageable);

    Page<Disease> findByStatus(
            com.example.diseaseregistry.enums.RecordStatus status,
            Pageable pageable
    );

    Page<Disease> findByNomContainingIgnoreCaseOrCodeContainingIgnoreCase(
            String nom,
            String code,
            Pageable pageable
    );
}