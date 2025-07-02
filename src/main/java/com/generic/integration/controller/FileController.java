package com.generic.integration.controller;

import com.generic.integration.model.FileRecordEntity;
import com.generic.integration.model.ProcessedFileEntity;
import com.generic.integration.repository.FileRecordRepository;
import com.generic.integration.repository.ProcessedFileRepository;
import com.generic.integration.service.ArangoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileRecordRepository repository;

    @Autowired
    private ProcessedFileRepository processedFileRepository;
    @Autowired
    private ArangoService arangoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile[] files) {
        try {
            String inboundPath = "data/inbound";
            for (MultipartFile file : files) {
                File targetFile = new File(inboundPath + File.separator + file.getOriginalFilename());
                FileCopyUtils.copy(file.getBytes(), targetFile);
            }
            return ResponseEntity.ok("All files uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            long processedFiles = processedFileRepository.count();
            long processedRecords = repository.count();
            File inboundDir = new File("data/inbound");
            long pending = inboundDir.listFiles() == null ? 0 : inboundDir.listFiles().length;
            File errorDir = new File("data/error");
            long failed = errorDir.listFiles() == null ? 0 : errorDir.listFiles().length;

            stats.put("processedFiles", processedFiles);
            stats.put("processedRecords", processedRecords);
            stats.put("pending", pending);
            stats.put("failed", failed);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(stats);
        }
    }

    @GetMapping("/outbound")
    public Iterable<FileRecordEntity>  outbound() {
        try {
           return repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/processed-files")
    public Iterable<ProcessedFileEntity>   processedFiles() {
        try {
            return processedFileRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
