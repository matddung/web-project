package com.example.demo.service;

import com.example.demo.entity.Submission;
import com.example.demo.entity.TestCase;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.util.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final CompilerService compilerService;
    private final TestCaseService testCaseService;

    public ResponseEntity<?> createSubmission(UserPrincipal userPrincipal, Long problemId) {
        Submission submission = Submission.builder()
                .userId(userPrincipal.getId())
                .problemId(problemId)
                .build();

        submissionRepository.save(submission);

        return ResponseEntity.ok(submission);
    }

    public ResponseEntity<?> getSubmissionList(UserPrincipal userPrincipal) {
        List<Submission> submissionList = submissionRepository.findByUserId(userPrincipal.getId());
        return ResponseEntity.ok(submissionList);
    }

    public ResponseEntity<?> submitAndJudge(String code, Long problemId, UserPrincipal userPrincipal) {
        List<TestCase> testCaseList = (List<TestCase>) testCaseService.getTestCaseListByProblemId(problemId);
        boolean allPassed = true;

        for (TestCase testCase : testCaseList) {
            String input = testCase.getInputData();
            String expectedOutput = testCase.getExpectedOutput();

            ResponseEntity<?> response = compilerService.compileWithInput(code, input);

            if (response.getStatusCode().is2xxSuccessful()) {
                String actualOutput = response.getBody().toString().trim();
                if (!actualOutput.equals(expectedOutput.trim())) {
                    allPassed = false;
                    break;
                }
            } else {
                allPassed = false;
                break;
            }
        }

        Submission submission = Submission.builder()
                .userId(userPrincipal.getId())
                .problemId(problemId)
                .build();

        submissionRepository.save(submission);

        return ResponseEntity.ok(submission);
    }
}