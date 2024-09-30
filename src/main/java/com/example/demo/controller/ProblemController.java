package com.example.demo.controller;

import com.example.demo.dto.request.ProblemRequest;
import com.example.demo.service.ProblemService;
import com.example.demo.util.CurrentUser;
import com.example.demo.util.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    private ResponseEntity<?> createProblem(
            @RequestBody ProblemRequest problemRequest,
            @CurrentUser UserPrincipal userPrincipal) {
        return problemService.createProblem(problemRequest, userPrincipal);
    }

    private ResponseEntity<?> modifyProblem(
            @RequestParam Long problemId,
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ProblemRequest problemRequest) {
        return problemService.modifyProblem(problemId, userPrincipal, problemRequest);
    }

    private ResponseEntity<?> deleteProblem(
            @RequestParam Long problemId,
            @CurrentUser UserPrincipal userPrincipal) {
        return problemService.deleteProblem(problemId, userPrincipal);
    }

    private ResponseEntity<?> getProblemListWithDifficulty(
            @RequestParam String difficulty) {
        return problemService.getProblemListWithDifficulty(difficulty);
    }

    private ResponseEntity<?> getProblemDetail(
            @RequestParam Long problemId) {
        return problemService.getProblemDetail(problemId);
    }
}