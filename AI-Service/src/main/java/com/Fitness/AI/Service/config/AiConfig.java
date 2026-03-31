package com.Fitness.AI.Service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class AiConfig {

    @Value("classpath:promttemplates/userpromt.st")
    private Resource systemPromptResource;

    /**
     * Configures the ChatClient with system prompt from file
     * The system prompt defines the character and behavior of the AI fitness coach
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        try {
            // Read the system prompt from the template file
            String systemPrompt = new String(
                    systemPromptResource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );
            
            log.info("Loaded system prompt from resource successfully");
            
            return builder
                    .defaultSystem(systemPrompt)
                    .build();
        } catch (IOException e) {
            log.error("Failed to load system prompt template", e);
            // Fallback system message
            return builder
                    .defaultSystem("You are an expert fitness coach providing personalized workout recommendations.")
                    .build();
        }
    }

    /**
     * Provides ObjectMapper bean for JSON parsing
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
