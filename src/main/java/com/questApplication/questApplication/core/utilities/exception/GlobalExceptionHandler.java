package com.questApplication.questApplication.core.utilities.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBeanType().getSimpleName();
        String requestDescription = request.getDescription(false);

        log.error("ResourceNotFoundException in {}.{}(): {}", className, methodName, ex.getMessage());
        log.error("Request Info: {}", requestDescription);

        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("NOT_FOUND");
        error.setErrorMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBeanType().getSimpleName();
        String requestDescription = request.getDescription(false);

        log.error("UnauthorizedException in {}.{}(): {}", className, methodName, ex.getMessage());
        log.error("Request Info: {}", requestDescription);

        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("UNAUTHORIZED");
        error.setErrorMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBeanType().getSimpleName();
        String requestDescription = request.getDescription(false);

        log.error("ValidationException in {}.{}(): {}", className, methodName, ex.getMessage());
        log.error("Request Info: {}", requestDescription);

        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("VALIDATION_ERROR");
        error.setErrorMessage("Validation failed");
        error.setValidationErrors(validationErrors);
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBeanType().getSimpleName();
        String requestDescription = request.getDescription(false);

        log.error("Exception in {}.{}(): {}", className, methodName, ex.getMessage(), ex);
        log.error("Request Info: {}", requestDescription);

        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("INTERNAL_SERVER_ERROR");
        error.setErrorMessage("Beklenmeyen bir hata oluştu.");
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode("RUNTIME_EXCEPTION");
        error.setErrorMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
