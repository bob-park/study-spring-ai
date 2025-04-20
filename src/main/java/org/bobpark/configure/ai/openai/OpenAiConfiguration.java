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
        return builder.build();
    }
}
