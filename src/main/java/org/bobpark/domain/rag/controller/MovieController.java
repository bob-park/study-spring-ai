package org.bobpark.domain.rag.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.domain.rag.model.MovieRecommendResponse;
import org.bobpark.domain.rag.service.MovieRagService;

@RequiredArgsConstructor
@RestController
@RequestMapping("movie")
public class MovieController {

    private final MovieRagService movieRagService;

    @RequestMapping("recommend")
    public List<MovieRecommendResponse> recommend(String query) {
        return movieRagService.recommendMovie(query);
    }
}
