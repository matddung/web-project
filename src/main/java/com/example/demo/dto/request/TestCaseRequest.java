package com.example.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestCaseRequest {
    private Long problemId;
    private String inputData;
    private String expectedOutput;
}