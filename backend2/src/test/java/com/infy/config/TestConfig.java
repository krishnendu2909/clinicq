package com.infy.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.test.web.client.MockRestTemplateServerHttpRequestInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Test configuration class for setting up test beans and configurations.
 * This class provides test-specific configurations that are only loaded during test execution.
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Creates a test-specific ObjectMapper with Java 8 time module support.
     * This ObjectMapper is configured for proper serialization/deserialization of date/time objects.
     * 
     * @return ObjectMapper configured for testing
     */
    @Bean
    @Primary
    public ObjectMapper testObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    /**
     * Creates a test data builder for creating test entities.
     * This utility helps in creating consistent test data across all test classes.
     * 
     * @return TestDataBuilder instance
     */
    @Bean
    public TestDataBuilder testDataBuilder() {
        return new TestDataBuilder();
    }

    /**
     * Creates a test utility class for common test operations.
     * 
     * @return TestUtilities instance
     */
    @Bean
    public TestUtilities testUtilities() {
        return new TestUtilities();
    }

    /**
     * Creates a mock service configuration for testing service layer components.
     * 
     * @return MockServiceConfig instance
     */
    @Bean
    public MockServiceConfig mockServiceConfig() {
        return new MockServiceConfig();
    }

    /**
     * Creates a test database configuration for testing repository layer components.
     * 
     * @return TestDatabaseConfig instance
     */
    @Bean
    public TestDatabaseConfig testDatabaseConfig() {
        return new TestDatabaseConfig();
    }
}
