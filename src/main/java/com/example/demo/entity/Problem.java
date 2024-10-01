package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@DynamicUpdate
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String difficulty;
    @CreatedDate
    private LocalDateTime createdAt;
    private String createBy;

    @Builder
    public Problem(String title, String description, String inputFormat, String outputFormat, String difficulty, String createBy) {
        this.title = title;
        this.description = description;
        this.inputFormat = inputFormat;
        this.outputFormat = outputFormat;
        this.difficulty = difficulty;
        this.createBy = createBy;
    }
}