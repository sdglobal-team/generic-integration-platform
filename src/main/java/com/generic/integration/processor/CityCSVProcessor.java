package com.generic.integration.processor;

import com.generic.integration.service.ArangoService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CityCSVProcessor implements Processor {

    @Autowired
    private ArangoService arangoService;

    @Override
    public void process(Exchange exchange) {
        List<String> row = exchange.getIn().getBody(List.class);
        if (row == null || row.isEmpty() || "city".equalsIgnoreCase(row.get(0))) return;
        arangoService.saveCities(row);
    }

}