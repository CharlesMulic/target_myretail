package com.charliemulic.target.myretail.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ValidationErrorsException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ValidationErrorsException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        log.info(String.format("Processing validation errors for %s", bindingResult.getTarget().getClass().getSimpleName()));

        List<String> errors = bindingResult.getFieldErrors().stream().map(error -> {
            return String.format("Field [%s] %s", error.getField(), error.getDefaultMessage());
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("status", BAD_REQUEST);
        response.put("statusCode", BAD_REQUEST.value());
        response.put("validationErrors", errors);

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
