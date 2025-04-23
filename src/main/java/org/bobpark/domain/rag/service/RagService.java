package org.bobpark.domain.rag.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RagService {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    private static final String PROMPT = """
        You are an assistant for question-answering tasks.
        Use the following pieces of retrieved context to answer the question.
        If you don't know the answer, just say that you don.t know.
        Answer in Korean.
        
        #Question:
        {input}
        
        #Context :
        {documents}
        
        #Answer:
        """;

    public String simplify(String question) {
        PromptTemplate template = new PromptTemplate(PROMPT);

        Map<String, Object> promptsParameters = new HashMap<>();

        promptsParameters.put("input", question);
        promptsParameters.put("documents", findSimilarData(question));

        return chatModel
            .call(template.create(promptsParameters))
            .getResult()
            .getOutput()
            .getText();
    }

    // # 5.단계 - 검색기(Retriever) 생성---|(Question)<---유사도 검색(similarity)
    // 문서에 포홤되어 있는 정보를 검색하고 생성
    private String findSimilarData(String question) {
        List<Document> documents =
            vectorStore.similaritySearch(
                SearchRequest
                    .builder()
                    .query(question)
                    .topK(2)
                    .build());

        log.debug("similar documents={}", documents);

        return documents
            .stream()
            .map(Document::getFormattedContent)
            .collect(Collectors.joining());
    }

}
