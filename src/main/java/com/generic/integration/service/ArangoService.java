package com.generic.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.integration.model.City;
import com.generic.integration.model.FileRecordEntity;
import com.generic.integration.model.ProcessedFileEntity;
import com.generic.integration.repository.CityRepository;
import com.generic.integration.repository.FileRecordRepository;
import com.generic.integration.repository.ProcessedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArangoService {

    @Autowired
    private FileRecordRepository repository;

    @Autowired
    private ProcessedFileRepository processedFileRepo;

    @Autowired
    private CityRepository cityRepository;

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

    public void saveCities(List<String> row) {
        City city = new City();
        city.setCity(row.get(0));
        city.setState_id(row.get(2));
        city.setState_name(row.get(3));
        city.setCounty_name(row.get(5));
        city.setLat(parseDouble(row.get(6)));
        city.setLng(parseDouble(row.get(7)));
        city.setDensity(parseDouble(row.get(10)));
        city.setTimezone(row.get(13));
        city.setZips(row.get(14));
        cityRepository.save(city);

        System.out.println("Upserted city: " + city.getCity());
    }

    private Double parseDouble(String val) {
        try {
            return (val == null || val.trim().isEmpty()) ? null : Double.parseDouble(val.trim());
        } catch (Exception e) {
            return null;
        }
    }

}
