package com.example.student_system.exception;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    public CommonResponse<ResponseCode> handleException(Exception e) {
        logger.error("<UNK>", e);
        return null;
    }
}
