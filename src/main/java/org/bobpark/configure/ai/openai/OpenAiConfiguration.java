package org.bobpark.configure.ai.openai;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class OpenAiConfiguration {

    @Bean
    public ChatClient openAiChatClient(ChatClient.Builder builder){
        return builder
            // default system message 를 추가할 수 있음 - 역할 부여
            //.defaultSystem("당신은 교육 튜터입니다. 개념을 명확하고 간단하게 설명하세요.")
            // default system message 를 placeholder 로 만들 수 있음
            .defaultSystem("""
                You are an AI assistant that specializes in {subject}.
                You respond in a {tone} voice with detailed explanations.
                """)
            .build();
    }
}
