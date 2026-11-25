package com.api.kdevfolio.cvgenerator.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Gemini client is only created when GOOGLE_API_KEY is set, so the rest
 * of the API can start without it. CV generation returns 503 until then.
 */
@Configuration
public class GeminiConfig {

    @Bean
    @ConditionalOnExpression("!'${gemini.api-key:}'.isBlank()")
    public Client geminiClient(@Value("${gemini.api-key}") String apiKey) {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}
