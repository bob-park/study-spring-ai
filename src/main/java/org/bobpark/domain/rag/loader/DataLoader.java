package org.bobpark.domain.rag.loader;

import java.util.List;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader {

    private final VectorStore vectorStore;
    private final JdbcClient jdbcClient;

    @Value("classpath:/SPRi AI Brief_11월호_산업동향_F.pdf")
    private Resource pdfResource;

    @PostConstruct
    public void init() {
        Integer count =
            jdbcClient.sql("select count(*) from vector_store")
                .query(Integer.class)
                .single();

        log.debug("No of Records in the PG Vector Store={}", count);

        // 여러번 실행 방지
        if (count == 0) {
            PdfDocumentReaderConfig config =
                PdfDocumentReaderConfig.builder()
                    .withPageTopMargin(0)
                    .withPageExtractedTextFormatter(
                        ExtractedTextFormatter.builder()
                            .withNumberOfTopTextLinesToDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();

            // 1. load documents
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, config);
            List<Document> documents = pdfReader.get();

            log.debug("document contents: \n {}", documents.toString());

            // chunk 1000 글자 단위로 자름
            TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
            List<Document> splitDocuments = splitter.apply(documents);

            log.debug("split documents size={}", splitDocuments.size());
            log.debug("split documents={}", splitDocuments.get(0));

            // # 3.단계 : 임베딩(Embedding) -> 4.단계 : DB에 저장(백터스토어 생성)
            vectorStore.accept(splitDocuments); // OpenAI 임베딩을 거친다.
            log.debug("Application is ready to Serve the Requests");
        }

    }

}
