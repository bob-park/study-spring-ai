package org.bobpark.domain.ai.openai.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.domain.ai.openai.model.GenerateRequest;
import org.bobpark.domain.ai.openai.service.v1.OpenAiV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/ai/openai")
public class OpenAiV1Controller {

    private final OpenAiV1Service openAiService;

    @GetMapping(path = "chat")
    public String generate(String message) {
        return openAiService.generate(message);
    }

    @GetMapping(path = "chat/placeholder")
    public String generateWIthPlaceholder(GenerateRequest request) {
        return openAiService.generateWithPlaceholder(request);
    }

    @GetMapping(path = "chat/json")
    public ChatResponse generate(GenerateRequest request){
        return openAiService.generateResponseWithPlaceholder(request);
    }
}
