package com.ephirious.handler;

import com.ephirious.dto.response.ExceptionDto;
import com.ephirious.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionDto> handleApiException(ApiException exception) {
        ExceptionDto dto = new ExceptionDto(exception.getClientMessage());
        return ResponseEntity.status(exception.getStatus()).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleValidationException(MethodArgumentNotValidException exception) {
        ObjectError error = exception.getBindingResult().getAllErrors().getFirst();

        String message = error.getDefaultMessage() != null
                ? error.getDefaultMessage()
                : "Validation error";

        ExceptionDto dto = new ExceptionDto(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleValidationException(Exception exception) {
        ExceptionDto dto = new ExceptionDto("Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }
}
