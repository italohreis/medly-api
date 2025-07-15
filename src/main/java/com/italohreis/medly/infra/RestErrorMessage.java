package com.italohreis.medly.infra;

import org.springframework.http.HttpStatus;

public record RestErrorMessage(HttpStatus status, String message) {
}
