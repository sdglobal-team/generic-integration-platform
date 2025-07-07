package com.generic.integration.routes;

import com.generic.integration.feignclient.CityCsvClient;
import com.generic.integration.model.FileRecordCsv;
import com.generic.integration.processor.CityCSVProcessor;
import com.generic.integration.service.ArangoService;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InboundFileRoute extends RouteBuilder {

    @Autowired
    ArangoService arangoService;

    @Autowired
    CityCsvClient cityCsvClient;
    @Autowired
    CityCSVProcessor cityCSVProcessor;

    @Override
    public void configure() throws Exception {


        onException(Exception.class)
                .log(LoggingLevel.ERROR, "ERROR processing inbound file: ${exception.message}")
                .to("file:{{integration.error-folder}}?fileName=error-${file:name.noext}-${date:now:yyyyMMddHHmmss}.${file:ext}")
                .handled(true);


        from("file:{{integration.inbound-folder}}?move=../processed/${file:name}")
                .routeId("local-file-inbound")
                .log("Picked up LOCAL file: ${header.CamelFileName}")
                .to("direct:commonProcess");

        /*
         * FUTURE: SFTP Pickup
         */

        // from("sftp://{{integration.sftp-host}}/{{integration.sftp-folder}}?username={{integration.sftp-username}}&password={{integration.sftp-password}}&delete=true")
        //         .routeId("sftp-file-inbound")
        //         .log("Picked up SFTP file: ${header.CamelFileName}")
        //         .to("direct:commonProcess");

        /*
         * FUTURE: AWS S3 Pickup
         */

        // from("aws-s3://{{integration.s3-bucket}}?amazonS3Client=#s3Client&deleteAfterRead=true")
        //         .routeId("s3-file-inbound")
        //         .log("Picked up S3 file: ${header.CamelFileName}")
        //         .to("direct:commonProcess");

        from("aws2-s3://file-event?amazonS3Client=#s3Client&deleteAfterRead=true&includeBody=true")
                .routeId("s3-filebase-inbound")
                .log("Picked up file from Filebase S3: ${header.CamelAwsS3Key}")
                .process(exchange -> {
                    String s3Key = exchange.getIn().getHeader("CamelAwsS3Key", String.class);
                    if (s3Key != null) {
                        exchange.getIn().setHeader("CamelFileName", s3Key);
                    }
                })
                .to("direct:commonProcess");


        /*
         * Common Process route
         */
        from("direct:commonProcess")
                .log("Processing inbound file: ${header.CamelFileName}")
                .choice()
                .when(simple("${file:ext} == 'csv'"))
                .to("direct:processCsv")
                .when(simple("${file:ext} == 'json'"))
                .to("direct:processJson")
                .when(simple("${file:ext} == 'xml'"))
                .to("direct:processXml")
                .otherwise()
                .log("Unsupported file type: ${file:ext}")
                .to("file:{{integration.error-folder}}")
                .end();

        /*
         * CSV Processing
         */
        from("direct:processCsv")
                .log("Processing CSV: ${file:name}")
                .unmarshal().bindy(BindyType.Csv, FileRecordCsv.class)
                .split(body())
                .marshal().json()
                .to("kafka:{{integration.kafka-topic}}?brokers={{integration.kafka-brokers}}")
                .end()
                .process(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    arangoService.saveProcessedFile(fileName, "csv");
                })
                .log("CSV processing complete");

        /*
         * JSON Processing
         */
        from("direct:processJson")
                .log("Processing JSON: ${file:name}")
                .split().jsonpath("$[*]")
                .marshal().json()
                .to("kafka:{{integration.kafka-topic}}?brokers={{integration.kafka-brokers}}")
                .end()
                .process(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    arangoService.saveProcessedFile(fileName, "json");
                })
                .log("JSON processing complete");

        /*
         * XML Processing
         */
        from("direct:processXml")
                .log("Processing XML: ${file:name}")
                .unmarshal().jaxb("com.generic.integration.model")
                .marshal().json()
                .to("kafka:{{integration.kafka-topic}}?brokers={{integration.kafka-brokers}}")
                .process(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    arangoService.saveProcessedFile(fileName, "xml");
                })
                .log("XML processing complete");

        from("direct:fetch-and-store-cities")
                .routeId("city-data-route")
                .log("🚀 Fetching city CSV...")

                .process(exchange -> {
                    String csv = cityCsvClient.downloadCityCsv().getBody();
                    exchange.getIn().setBody(csv);
                })

                .unmarshal().csv()
                .split(body()).streaming()
                .process(cityCSVProcessor)

                .log("✅ City data processing complete.");
    }

}
