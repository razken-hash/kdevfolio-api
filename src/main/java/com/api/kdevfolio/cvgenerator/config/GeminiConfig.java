package com.api.kdevfolio.cvgenerator.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Bean
    public Client geminiClient(
            @Value("${gemini.api-key}") String apiKey
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Google API key is missing.");
        }

        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}