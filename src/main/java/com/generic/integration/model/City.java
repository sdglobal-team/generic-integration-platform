package com.generic.integration.model;
import com.arangodb.springframework.annotation.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document("us_cities")
public class City {

    private String city;            // column 0
    private String state_id;        // column 2
    private String state_name;      // column 3
    private String county_name;     // column 5s
    private Double lat;             // column 6
    private Double lng;             // column 7
    private Double density;         // column 10
    private String timezone;        // column 13
    private String zips;            // column 14
}