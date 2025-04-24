package org.bobpark.domain.rag.loader;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class HotelLoader {

    private final VectorStore vectorStore;
    private final JdbcClient jdbcClient;

    @Value("classpath:data.txt")
    private Resource resource;

    @PostConstruct
    public void init() throws Exception {
        Integer count =
            jdbcClient.sql("select count(*) from hotel_vector")
                .query(Integer.class)
                .single();

        log.debug("No of Records in the PG Vector Store={}", count);

        if (count > 0) {
            return;
        }

        List<Document> documents =
            Files.lines(resource.getFile().toPath())
                .map(Document::new)
                .toList();

        TextSplitter textSplitter = new TokenTextSplitter();

        for (Document document : documents) {

            List<Document> splitteddocs = textSplitter.split(document);

            log.debug("before adding document: {}", document.getFormattedContent());

            vectorStore.add(splitteddocs); //임베딩

            log.debug("Added document: {}", document.getFormattedContent());

            Thread.sleep(1000); // 1초
        }
    }
}
