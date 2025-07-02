package com.generic.integration.repository;

import com.generic.integration.model.FileRecordEntity;
import com.arangodb.springframework.repository.ArangoRepository;

public interface FileRecordRepository extends ArangoRepository<FileRecordEntity, String> {
}
