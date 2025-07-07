package com.generic.integration.scheduler;

import org.apache.camel.ProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CityCsvScheduler {

    private final ProducerTemplate producerTemplate;

    public CityCsvScheduler(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

//    @Scheduled(cron = "0 */2 * * * *") // every 5 minutes
    public void triggerCamelRoute() {
        producerTemplate.sendBody("direct:fetch-and-store-cities", null);
    }
}
