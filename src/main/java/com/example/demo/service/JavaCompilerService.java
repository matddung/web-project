package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JavaCompilerService {
    public ResponseEntity<?> compile(String code) {
        String className = extractClassName(code);

        if (className == null) {
            return ResponseEntity.status(400).body("클래스 명 오류");
        }

        if (code.startsWith("\"") && code.endsWith("\"")) {
            code = code.substring(1, code.length() - 1);
        }

        code = code.replace("\\\"", "\"");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        File file = new File(className + ".java");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(code);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to write source file: " + e.getMessage());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        int result = compiler.run(null, null, printStream, file.getPath());

        if (result == 0) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-Dfile.encoding=UTF-8", "-cp", ".", className);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                StringBuilder executionResult = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    executionResult.append(line).append("\n");
                }

                process.waitFor();

                Files.deleteIfExists(file.toPath());
                Files.deleteIfExists(new File(className + ".class").toPath());

                return ResponseEntity.ok(executionResult.toString());
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Execution failed: " + e.getMessage());
            }
        } else {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Delete failed: " + e.getMessage());
            }
            return ResponseEntity.status(500).body("Compilation failed:\n" + outputStream.toString());
        }
    }

    private String extractClassName(String code) {
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}