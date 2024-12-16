package com.atlasfin.compliance.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ComplianceExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ComplianceExceptionHandler.class);

    @ExceptionHandler(ComplianceException.class)
    public ResponseEntity<String> handleComplianceException(ComplianceException e) {
        log.error("error occurred {}", e);
        return new ResponseEntity<>("Error occurred: " + e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Unknow error occurred {}", e);
        return new ResponseEntity<>("Error occurred: " + e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
