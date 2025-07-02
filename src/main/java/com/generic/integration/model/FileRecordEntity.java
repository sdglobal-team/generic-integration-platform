package com.generic.integration.model;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Document("file_records")
public class FileRecordEntity {

    @Id
    private String fileId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private boolean fileProcessed;  //new
    private String createdBy;
    private String createdDate;
}


