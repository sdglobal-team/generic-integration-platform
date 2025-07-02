package com.generic.integration.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.generic.integration.model.ProcessedFileEntity;

public interface ProcessedFileRepository extends ArangoRepository<ProcessedFileEntity, String> {
}
