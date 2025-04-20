package org.bobpark.domain.ai.openai.service.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import org.bobpark.domain.ai.openai.model.GenerateRequest;
import org.bobpark.domain.ai.openai.service.OpenAiService;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAiV1Service implements OpenAiService {

    private final ChatClient chatClient;

    @Override
    public String generate(String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    public String generateWithPlaceholder(GenerateRequest request) {
        return chatClient.prompt()
            .system(
                ps ->
                    ps.param("subject", request.subject())
                        .param("tone", request.tone()))
            .user(request.message())
            .call()
            .chatResponse()
            .getResult()
            .getOutput()
            .getText();
    }

    public ChatResponse generateResponseWithPlaceholder(GenerateRequest request){
        return chatClient.prompt()
            .system(
                ps ->
                    ps.param("subject", request.subject())
                        .param("tone", request.tone()))
            .user(request.message())
            .call()
            .chatResponse();
    }
}
