package com.example.demo;

import com.example.demo.service.JavaCompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private JavaCompilerService javaCompilerService;

}