package com.example.demo.controller;

import com.example.demo.service.SubmissionService;
import com.example.demo.util.CurrentUser;
import com.example.demo.util.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submission")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping("/create")
    private ResponseEntity<?> createSubmission(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam Long problemId) {
        return submissionService.createSubmission(userPrincipal, problemId);
    }

    @GetMapping
    private ResponseEntity<?> getSubmissionList(
            @CurrentUser UserPrincipal userPrincipal) {
        return submissionService.getSubmissionList(userPrincipal);
    }

    @PostMapping
    private ResponseEntity<?> submitAndJudge(
            @RequestParam String code,
            @RequestParam Long problemId,
            @CurrentUser UserPrincipal userPrincipal) {
        return submissionService.submitAndJudge(code, problemId, userPrincipal);
    }
}