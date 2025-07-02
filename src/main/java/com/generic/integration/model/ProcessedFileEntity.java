package com.generic.integration.model;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;
@Data
@Document("processed_files")
public class ProcessedFileEntity {
    @Id
    private String fileId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String createdBy;
    private String createdDate;
    private String status;
}

