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
            // user message 도 동적으로 만들 수 있다
            .user(
                pu ->
                    pu.text(request.message())
                        .param("subject", request.subject())
                        .param("tone", request.tone()))
            .call().
            content();
    }

    public ChatResponse generateResponseWithPlaceholder(GenerateRequest request) {
        return chatClient.prompt()
            .system(
                ps ->
                    ps.param("subject", request.subject())
                        .param("tone", request.tone()))
            .user(request.message())
            .call()
            // entity 를 만들어서 구조화된 응답으로 받을 수 있음
            // .entity(new ParameterizedTypeReference<CustomResponse>() {
            // })
            .chatResponse();
    }
}
