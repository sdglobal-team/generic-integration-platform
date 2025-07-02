package com.generic.integration.routes;

import com.generic.integration.service.ArangoService;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaToArangoRoute extends RouteBuilder {

    @Autowired
    private ArangoService arangoService;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .log(LoggingLevel.ERROR, "ERROR saving to Arango: ${exception.message}")
                .handled(true);

        from("kafka:{{integration.kafka-topic}}?brokers={{integration.kafka-brokers}}")
                .routeId("kafka-to-arango-route")
                .log("Received message from Kafka: ${body}")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    arangoService.saveRecord(body);
                })
                .log("Record saved to Arango successfully");
    }
}