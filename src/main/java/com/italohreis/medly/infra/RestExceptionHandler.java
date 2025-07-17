package com.italohreis.medly.infra;

import com.italohreis.medly.exceptions.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<RestErrorMessage> handleEmailExists(EmailAlreadyExistsException ex) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<RestErrorMessage> handleInvalidCredentials(InvalidCredentialsException ex) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestErrorMessage> handleResourceNotFound(ResourceNotFoundException ex) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<RestErrorMessage> handleBusinessRuleException(BusinessRuleException ex) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<RestErrorMessage> handleForbiddenException(ForbiddenException ex) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }
}
