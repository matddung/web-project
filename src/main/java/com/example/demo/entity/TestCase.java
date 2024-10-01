package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@Setter
@Entity
@NoArgsConstructor
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long problemId;

    @Lob
    private String inputData;

    @Lob
    private String expectedOutput;

    @Builder
    public TestCase(Long problemId, String inputData, String expectedOutput) {
        this.problemId = problemId;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
    }
}