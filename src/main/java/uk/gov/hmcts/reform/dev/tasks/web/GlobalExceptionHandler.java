package uk.gov.hmcts.reform.dev.tasks.web;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uk.gov.hmcts.reform.dev.tasks.TaskNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<?> notFound(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("Not Found", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> badRequest(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(err("Validation failed", ex.getBindingResult().toString()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> badJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(err("Malformed request", ex.getMostSpecificCause().getMessage()));
    }

    private static Map<String, Object> err(String message, String detail) {
        return Map.of("timestamp", OffsetDateTime.now().toString(), "message", message, "detail", detail);
    }

}
