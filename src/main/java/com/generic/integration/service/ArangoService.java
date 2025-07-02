package com.generic.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.integration.model.FileRecordEntity;
import com.generic.integration.model.ProcessedFileEntity;
import com.generic.integration.repository.FileRecordRepository;
import com.generic.integration.repository.ProcessedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArangoService {

    @Autowired
    private FileRecordRepository repository;

    @Autowired
    private ProcessedFileRepository processedFileRepo;

    @Autowired
    private ObjectMapper mapper;

    public void saveRecord(String jsonData) {
        try {
            FileRecordEntity entity = mapper.readValue(jsonData,FileRecordEntity.class);
            entity.setFileProcessed(true);
            repository.save(entity);
            System.out.println("Saved in ArangoDB: " + entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public String fetchOutboundData() {
        try {
            var all = repository.findAll();
            return mapper.writeValueAsString(all);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "[]";
        }
    }

    public void saveProcessedFile(String fileName, String fileType) {
        ProcessedFileEntity processed = new ProcessedFileEntity();
        processed.setFileName(fileName);
        processed.setFileType(fileType);
        processed.setCreatedDate(LocalDateTime.now().toString());
        processed.setStatus("COMPLETED");
        processed.setCreatedBy("System");
        processedFileRepo.save(processed);
    }




}
