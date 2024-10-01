package com.example.demo.controller;

import com.example.demo.dto.request.TestCaseRequest;
import com.example.demo.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testCase")
@RequiredArgsConstructor
public class TestCaseController {
    private final TestCaseService testCaseService;

    @PostMapping("/create")
    private ResponseEntity<?> createTestCase(
            @RequestBody TestCaseRequest testCaseRequest) {
        return testCaseService.createTestCase(testCaseRequest);
    }

    @PatchMapping("/modify")
    private ResponseEntity<?> modifyTestCase(
            @RequestParam Long testCaseId,
            @RequestBody TestCaseRequest testCaseRequest) {
        return testCaseService.modifyTestCase(testCaseId, testCaseRequest);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<?> deleteTestCase(
            @RequestParam Long testCaseId) {
        return testCaseService.deleteTestCase(testCaseId);
    }

    @GetMapping
    private ResponseEntity<?> getTestCaseListByProblemId(
            @RequestParam Long problemId) {
        return testCaseService.getTestCaseListByProblemId(problemId);
    }
}