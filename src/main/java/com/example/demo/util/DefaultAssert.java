package com.example.demo.util;

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
}