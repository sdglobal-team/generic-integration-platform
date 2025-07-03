package com.generic.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class FilebaseS3ClientConfig {

    @Bean(name = "s3Client")
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create("https://s3.filebase.com"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        "C03F74221E7A3F812DA1",
                                        "7gnKRNpZoYfRFXy6ZyXBHdw5HRwdb5qPksL31yzi"
                                )
                        )
                )
                .build();
    }
}
