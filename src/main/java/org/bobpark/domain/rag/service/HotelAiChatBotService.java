package org.bobpark.domain.rag.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Service
public class HotelAiChatBotService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    private final String template = """
        당신은 어느 호텔 직원입니다. 문맥에 따라 고객의 질문에 정중하게 답변해 주십시오.
        컨텍스트가 질문에 대답할 수 없는 경우 '모르겠습니다'라고 대답하세요.
        
        컨텍스트:
        {context}
        질문:
        {question}
        
        답변:
        """;

    public String chat(String message) {
        List<Document> answers =
            vectorStore.similaritySearch(
                SearchRequest
                    .builder()
                    .query(message)
                    .similarityThreshold(0.5)
                    .topK(1)
                    .build());

        return chatClient.prompt()
            .user(
                pu ->
                    pu.text(template)
                        .param("context", answers)
                        .param("question", message))
            .call()
            .content();
    }
}
