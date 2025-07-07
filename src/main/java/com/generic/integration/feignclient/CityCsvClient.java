package com.generic.integration.feignclient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cityCsvClient", url = "https://simplemaps.com")
@Service
public interface CityCsvClient {

    @GetMapping(value = "/static/data/us-cities/uscities.csv", consumes = "text/csv")
    ResponseEntity<String> downloadCityCsv();
}
