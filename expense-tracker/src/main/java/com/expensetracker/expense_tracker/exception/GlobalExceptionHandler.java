package com.expensetracker.expense_tracker.exception;

import com.expensetracker.expense_tracker.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e){
        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, e.getMessage()));
    }
}
