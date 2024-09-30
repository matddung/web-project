package com.example.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProblemRequest {
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String difficulty;
}