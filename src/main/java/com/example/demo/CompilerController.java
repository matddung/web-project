package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java/api")
@RequiredArgsConstructor
public class CompilerController {
    private final JavaCompilerService javaCompilerService;

    @PostMapping("/compiler")
    public ResponseEntity<?> compiler(@RequestBody String code) {
        return javaCompilerService.compile(code);
    }
}
