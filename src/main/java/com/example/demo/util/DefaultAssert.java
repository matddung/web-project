package com.example.demo.util;

import com.example.demo.jwt.DefaultAuthenticationException;

import java.util.Optional;

public class DefaultAssert {
    public static void isTrue(boolean value, String message) {
        if (!value) {
            throw new DefaultException(ErrorCode.INVALID_CHECK, message);
        }
    }

    public static void isOptionalPresent(Optional<?> value){
        if(!value.isPresent()){
            throw new DefaultException(ErrorCode.INVALID_PARAMETER);
        }
    }

    public static void isAuthentication(boolean value){
        if(!value){
            throw new DefaultAuthenticationException(ErrorCode.INVALID_AUTHENTICATION);
        }
    }

    public static void isAuthentication(String message){
        throw new DefaultAuthenticationException(message);
    }
}