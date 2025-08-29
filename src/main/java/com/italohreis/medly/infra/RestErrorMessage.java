package com.italohreis.medly.infra;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestErrorMessage(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
    public RestErrorMessage(HttpStatus status, String message, String path) {
        this(Instant.now(), status.value(), status.getReasonPhrase(), message, path, null);
    }

    public RestErrorMessage(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        this(Instant.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors);
    }
}