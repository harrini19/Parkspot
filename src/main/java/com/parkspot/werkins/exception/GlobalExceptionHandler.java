package com.parkspot.werkins.exception;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 record ErrorResponse(LocalDateTime timestamp,int status,String error,String message,String path){}
 private ErrorResponse error(HttpStatus s,String m,String p){return new ErrorResponse(LocalDateTime.now(),s.value(),s.getReasonPhrase(),m,p);}
 @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException e, jakarta.servlet.http.HttpServletRequest r){return ResponseEntity.status(404).body(error(HttpStatus.NOT_FOUND,e.getMessage(),r.getRequestURI()));}
 @ExceptionHandler(BusinessRuleException.class) ResponseEntity<ErrorResponse> conflict(BusinessRuleException e, jakarta.servlet.http.HttpServletRequest r){return ResponseEntity.status(409).body(error(HttpStatus.CONFLICT,e.getMessage(),r.getRequestURI()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException e,jakarta.servlet.http.HttpServletRequest r){String m=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).reduce((a,b)->a+"; "+b).orElse("Validation failed");return ResponseEntity.badRequest().body(error(HttpStatus.BAD_REQUEST,m,r.getRequestURI()));}
 @ExceptionHandler(Exception.class) ResponseEntity<ErrorResponse> other(Exception e,jakarta.servlet.http.HttpServletRequest r){return ResponseEntity.status(500).body(error(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error",r.getRequestURI()));}
}
