package org.bobpark.domain.rag.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import org.bobpark.domain.rag.model.MovieRecommendResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieRagService {

    private static final Pattern CONTENT_PATTERN = Pattern.compile(
        "(?<index>\\d+): (?<contents>.+) \\((?<title>.+)\\)");

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public List<MovieRecommendResponse> recommendMovie(String query) {

        List<MovieRecommendResponse> result = new ArrayList<>();

        List<Document> movies =
            vectorStore.similaritySearch(
                SearchRequest
                    .builder()
                    .query(query)
                    .similarityThreshold(0.8) // 수치가 낮으면 정확도 down, 개수 up
                    .topK(2) // 추출 개수
                    .build());

        if (movies == null) {
            return result;
        }

        for (Document movie : movies) {
            String content = movie.getFormattedContent();

            Matcher matcher = CONTENT_PATTERN.matcher(content);

            if (!matcher.find()) {
                continue;
            }

            String title = matcher.group("title");

            result.add(
                MovieRecommendResponse.builder()
                    .title(title)
                    .build());
        }

        return result;
    }

}
