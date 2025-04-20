package org.bobpark.domain.ai.openai.model;

public record GenerateRequest(String subject,
                              String tone,
                              String message) {
}
