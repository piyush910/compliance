package com.atlasfin.compliance.controller;

import com.atlasfin.compliance.exception.ComplianceException;
import com.atlasfin.compliance.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/fraud-detection")
@RequiredArgsConstructor
public class ComplianceController {

    private final Logger log = LoggerFactory.getLogger((ComplianceController.class));

    private final ComplianceService complianceService;


    @PostMapping("/upload")
    public ResponseEntity<Map<String, List<String>>> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) throws ComplianceException {
        log.info("Upload file started");
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Map<String, List<String>> suspiciousTransactions = complianceService.detectFraud(file);
        log.info("Upload file ended");
        return ResponseEntity.ok(suspiciousTransactions);
    }
}
