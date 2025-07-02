package com.generic.integration.routes;

import com.generic.integration.service.ArangoService;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutboundFileRoute extends RouteBuilder {

    @Autowired
    private ArangoService arangoService;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .log(LoggingLevel.ERROR, "ERROR generating outbound file: ${exception.message}")
                .handled(true);

        from("timer:outboundTimer?period=60000")
                .routeId("outbound-file-route")
                .log("Generating outbound file from ArangoDB data")
                .process(exchange -> {
                    String data = arangoService.fetchOutboundData();
                    exchange.getIn().setBody(data);
                })
                .to("file:{{integration.outbound-folder}}?fileName=outbound.json")
                .log("Outbound file written successfully to {{integration.outbound-folder}}");
    }
}