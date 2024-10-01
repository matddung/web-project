package com.example.demo.service;

import com.example.demo.dto.request.TestCaseRequest;
import com.example.demo.entity.TestCase;
import com.example.demo.repository.TestCaseRepository;
import com.example.demo.util.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;

    public ResponseEntity<?> createTestCase(TestCaseRequest testCaseRequest) {
        TestCase testCase = TestCase.builder()
                .problemId(testCaseRequest.getProblemId())
                .inputData(testCaseRequest.getInputData())
                .expectedOutput(testCaseRequest.getExpectedOutput())
                .build();

        testCaseRepository.save(testCase);

        return ResponseEntity.ok(testCase);
    }

    public ResponseEntity<?> modifyTestCase(Long testCaseId, TestCaseRequest testCaseRequest) {
        TestCase testCase = validTestCase(testCaseId);

        if (testCaseRequest.getInputData() != null) {
            testCase.setInputData(testCaseRequest.getInputData());
        }
        if (testCaseRequest.getExpectedOutput() != null) {
            testCase.setExpectedOutput(testCaseRequest.getExpectedOutput());
        }

        testCaseRepository.save(testCase);

        return ResponseEntity.ok(testCase);
    }

    public ResponseEntity<?> deleteTestCase(Long testCaseId) {
        TestCase testCase = validTestCase(testCaseId);
        testCaseRepository.delete(testCase);
        return ResponseEntity.ok("테스트 케이스가 삭제되었습니다.");
    }

    public ResponseEntity<?> getTestCaseListByProblemId(Long problemId) {
        return ResponseEntity.ok(testCaseRepository.findByProblemId(problemId));
    }

    private TestCase validTestCase(Long testCaseId) {
        Optional<TestCase> testCaseOptional = testCaseRepository.findById(testCaseId);
        DefaultAssert.isOptionalPresent(testCaseOptional);

        return testCaseOptional.get();
    }
}