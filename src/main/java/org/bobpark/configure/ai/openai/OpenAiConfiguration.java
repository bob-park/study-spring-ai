package org.bobpark.configure.ai.openai;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
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
            // 예시로 질문받은 내용에 대해서 사용자가 다시 물어보면 그 안에서 대답하게 하는 것
            // advisor 를 사용하지 않을 경우, 새로운 prompt 로 요청한 것으로 인식되어 새로운 내용으로 응답을 해줌
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .build();
    }

}
