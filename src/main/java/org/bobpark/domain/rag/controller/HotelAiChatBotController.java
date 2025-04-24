package org.bobpark.domain.rag.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.domain.rag.service.HotelAiChatBotService;

@RequiredArgsConstructor
@RestController
@RequestMapping("ai/chatbot")
public class HotelAiChatBotController {

    private final HotelAiChatBotService hotelAiChatBotService;

    @RequestMapping("chat")
    public String chat(String message) {
        return hotelAiChatBotService.chat(message);
    }

}
