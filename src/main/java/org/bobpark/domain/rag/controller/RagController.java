package org.bobpark.domain.rag.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.domain.rag.service.RagService;

@RequiredArgsConstructor
@RestController
@RequestMapping("rag")
public class RagController {

    private final RagService ragService;

    @GetMapping(path = "answer")
    public String simplify(String message) {
        return ragService.simplify(message);
    }

}
