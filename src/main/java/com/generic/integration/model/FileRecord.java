package com.generic.integration.model;

import lombok.Data;

@Data
public class FileRecord {
    private String fileId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String createdBy;
    private String createdDate;
}

