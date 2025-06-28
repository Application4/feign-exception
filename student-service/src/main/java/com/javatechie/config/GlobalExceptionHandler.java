package com.javatechie.config;

import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(FeignException.BadRequest.class)
//    public ResponseEntity<String> handleBadRequest(FeignException.BadRequest ex) {
//        return ResponseEntity
//                .badRequest()
//                .body(ex.contentUTF8()); // returns upstream error message
//    }
//
//    @ExceptionHandler(FeignException.InternalServerError.class)
//    public ResponseEntity<String> handleServerError(FeignException.InternalServerError ex) {
//        return ResponseEntity.internalServerError()
//                .body(ex.contentUTF8()); // returns upstream error message
//    }
//
//
    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<String> handleServiceUnavailable(FeignException.ServiceUnavailable ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Course service is currently unavailable. Please try again later.");
    }
//
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<String> handleRetryable(RetryableException ex) {
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body("Request timed out. Course service did not respond in time.");
    }
    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<String> handleSocketTimeout(SocketTimeoutException ex) {
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body("Socket timeout occurred while calling course service.");
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneralException(Exception ex) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Unexpected error occurred: " + ex.getMessage());
//    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignExceptions(FeignException ex) {
        int status = ex.status();
        String message = ex.contentUTF8(); // preserve upstream message

        return switch (status) {
            case 400 -> ResponseEntity.badRequest().body(message);
            case 500 -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
            case 503 -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Course service is unavailable.");
            default -> ResponseEntity.status(status).body("Feign error: " + message);
        };
    }
}
