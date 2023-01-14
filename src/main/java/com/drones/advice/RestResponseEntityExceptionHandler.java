package com.drones.advice;

import com.drones.controller.dto.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {DuplicateKeyException.class, TransactionSystemException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDuplicateKeyConflict(
            RuntimeException ex, WebRequest request) {
        ErrorMessage bodyOfResponse = new ErrorMessage();

        if (ex.getClass() == TransactionSystemException.class) {
            String message = ((TransactionSystemException) ex).getRootCause().getMessage();
            bodyOfResponse.setMessage(message);
        } else {
            bodyOfResponse = new ErrorMessage(ex.getMessage());
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}