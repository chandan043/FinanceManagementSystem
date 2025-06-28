package com.gl.fms.utility;

import com.gl.fms.exception.FinanceManagementSystemException;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.lang.model.element.NestingKind;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerException {
    private static final Log LOGGER = LogFactory.getLog(ControllerException.class);

    @ExceptionHandler(FinanceManagementSystemException.class)
    public ResponseEntity<Error> customException(FinanceManagementSystemException ex){
        Error error =new Error();
        List<String> errMessages = new ArrayList<>();

        errMessages.add(ex.getMessage());
        error.setMessage(errMessages);
        error.setStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        error.setTimeStamp(LocalDateTime.now());
        LOGGER.error(ex);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> generalException(Exception ex) {
        Error error = new Error();
        List<String> errMessages = new ArrayList<>();

        errMessages.add(ex.getMessage());
        error.setMessage(errMessages);
        error.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.setTimeStamp(LocalDateTime.now());
        LOGGER.error(ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> validationException(MethodArgumentNotValidException ex) {
        Error error = new Error();

        List<String> errMessages = new ArrayList<>();

        errMessages.addAll(ex.getBindingResult().getAllErrors().stream()
                .map(e -> "RequestBody : " + e.getDefaultMessage())
                .collect(Collectors.toList()));

        error.setMessage(errMessages);
        error.setStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        error.setTimeStamp(LocalDateTime.now());

        LOGGER.error(ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> validationException(ConstraintViolationException ex) {
        Error error = new Error();

        List<String> errMessages = new ArrayList<>();

        errMessages.addAll(ex.getConstraintViolations().stream()
                .map(e -> "PathVariable : " + e.getMessage())
                .collect(Collectors.toList()));

        error.setMessage(errMessages);
        error.setStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        error.setTimeStamp(LocalDateTime.now());

        LOGGER.error(ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
