package com.example.demo.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String phoneNumber;
    private String name;
    private String nickname;
}