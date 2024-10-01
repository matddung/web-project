package com.example.demo.controller;

import com.example.demo.service.CompilerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compiler")
@RequiredArgsConstructor
public class CompilerController {
    private final CompilerService compilerService;

//    @PostMapping
//    public ResponseEntity<?> compiler(@RequestBody String code) {
//        return compilerService.compile(code);
//    }
}