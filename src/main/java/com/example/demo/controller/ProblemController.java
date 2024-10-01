package com.example.demo.controller;

import com.example.demo.dto.request.ProblemRequest;
import com.example.demo.service.ProblemService;
import com.example.demo.util.CurrentUser;
import com.example.demo.util.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping("/create")
    private ResponseEntity<?> createProblem(
            @RequestBody ProblemRequest problemRequest,
            @CurrentUser UserPrincipal userPrincipal) {
        return problemService.createProblem(problemRequest, userPrincipal);
    }

    @PatchMapping("/modify")
    private ResponseEntity<?> modifyProblem(
            @RequestParam Long problemId,
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ProblemRequest problemRequest) {
        return problemService.modifyProblem(problemId, userPrincipal, problemRequest);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<?> deleteProblem(
            @RequestParam Long problemId,
            @CurrentUser UserPrincipal userPrincipal) {
        return problemService.deleteProblem(problemId, userPrincipal);
    }

    @GetMapping("/difficulty")
    private ResponseEntity<?> getProblemListWithDifficulty(
            @RequestParam String difficulty) {
        return problemService.getProblemListWithDifficulty(difficulty);
    }

    @GetMapping
    private ResponseEntity<?> getProblemDetail(
            @RequestParam Long problemId) {
        return problemService.getProblemDetail(problemId);
    }
}