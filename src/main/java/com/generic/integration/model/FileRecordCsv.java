package com.generic.integration.model;
import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;

@Data
@CsvRecord(separator = ",", skipFirstLine = true)
public class FileRecordCsv {

    @DataField(pos = 1)
    private String fileId;

    @DataField(pos = 2)
    private String fileName;

    @DataField(pos = 3)
    private String fileType;

    @DataField(pos = 4)
    private Long fileSize;

    @DataField(pos = 5)
    private String createdBy;

    @DataField(pos = 6)
    private String createdDate;

}
