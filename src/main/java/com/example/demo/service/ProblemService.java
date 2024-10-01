package com.example.demo.service;

import com.example.demo.dto.request.ProblemRequest;
import com.example.demo.entity.Problem;
import com.example.demo.entity.User;
import com.example.demo.repository.ProblemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.DefaultAssert;
import com.example.demo.util.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> createProblem(ProblemRequest problemRequest, UserPrincipal userPrincipal) {
        Problem problem = Problem.builder()
                .title(problemRequest.getTitle())
                .description(problemRequest.getDescription())
                .inputFormat(problemRequest.getInputFormat())
                .outputFormat(problemRequest.getOutputFormat())
                .createBy(userPrincipal.getName())
                .build();

        problemRepository.save(problem);

        return ResponseEntity.ok("Problem create success\n" + problem);
    }

    public ResponseEntity<?> modifyProblem(Long problemId, UserPrincipal userPrincipal, ProblemRequest problemRequest) {
        Optional<Problem> problemOptional = problemRepository.findById(problemId);
        DefaultAssert.isOptionalPresent(problemOptional);

        Problem problem = problemOptional.get();

        Optional<User> userOptional = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(userOptional);

        DefaultAssert.isTrue(problem.getCreateBy().equals(userOptional.get().getName()), "아이디와 문제 작성자가 일치하지 않습니다.");

        if (problemRequest.getTitle() != null) {
            problem.setTitle(problemRequest.getTitle());
        }
        if (problemRequest.getDescription() != null) {
            problem.setDescription(problemRequest.getDescription());
        }
        if (problemRequest.getInputFormat() != null) {
            problem.setInputFormat(problemRequest.getInputFormat());
        }
        if (problemRequest.getOutputFormat() != null) {
            problem.setOutputFormat(problemRequest.getOutputFormat());
        }
        if (problemRequest.getDifficulty() != null) {
            problem.setDifficulty(problemRequest.getDifficulty());
        }

        problemRepository.save(problem);

        return ResponseEntity.ok(problem);
    }

    public ResponseEntity<?> deleteProblem(Long problemId, UserPrincipal userPrincipal) {
        Optional<User> userOptional = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(userOptional);

        User user = userOptional.get();

        Optional<Problem> problemOptional = problemRepository.findById(problemId);
        DefaultAssert.isOptionalPresent(problemOptional);

        Problem problem = problemOptional.get();

        if (user.getRole().equals("ADMIN") || problem.getCreateBy().equals(user.getName())) {
            problemRepository.delete(problem);

            return ResponseEntity.ok("문제가 삭제되었습니다.");
        }

        return ResponseEntity.badRequest().body("권한이 없습니다.");
    }

    public ResponseEntity<?> getProblemListWithDifficulty(String difficulty) {
        return ResponseEntity.ok(problemRepository.findByDifficulty(difficulty));
    }

    public ResponseEntity<?> getProblemDetail(Long problemId) {
        return ResponseEntity.ok(problemRepository.findById(problemId));
    }
}