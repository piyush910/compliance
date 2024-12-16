package com.atlasfin.compliance.service;

import com.atlasfin.compliance.constant.ComplianceConstant;
import com.atlasfin.compliance.dto.Transaction;
import com.atlasfin.compliance.exception.ComplianceException;
import com.atlasfin.compliance.util.ComplianceUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
public class ComplianceService {

    private List<String> sanctionsList;
    private final Logger log = LoggerFactory.getLogger((ComplianceService.class));

    @PostConstruct
    public void loadSanctionsList() throws IOException {
        ClassPathResource resource = new ClassPathResource("sanctions_list.csv");
        sanctionsList = ComplianceUtil.csvToList(resource.getInputStream());
        log.info("Sanctions list loaded successfully. Total users in list {}", sanctionsList.size());
    }

    public Map<String, List<String>> detectFraud(MultipartFile file) throws ComplianceException {
        log.debug("Inside detectFraud service");
        Map<String, List<String>> fraudulentTransactions = new HashMap<>();
        try {
            List<Transaction> transactions = ComplianceUtil.csvToTransactions(file.getInputStream());
            for (Transaction transaction : transactions) {
                isHighFrequency(transaction, transactions, fraudulentTransactions);
                isHighValue(transaction, fraudulentTransactions);
                isOddHour(transaction, fraudulentTransactions);
                isUnusualMerchant(transaction, transactions, fraudulentTransactions);
                isUserInSanctionList(transaction, fraudulentTransactions);
            }
        } catch (Exception e) {
            throw new ComplianceException("Error occurred during processing!");
        }
        log.debug("Finished detectFraud service");
        return fraudulentTransactions;
    }

    private void isHighFrequency(Transaction transaction, List<Transaction> transactions, Map<String, List<String>> fraudulentTransactions) {
        long count = transactions.stream()
                .filter(t -> t.getUserId().equals(transaction.getUserId()))
                .filter(t -> Math.abs(Duration.between(t.getTimestamp(), transaction.getTimestamp()).toSeconds()) <= ComplianceConstant.MAX_FREQUENCY_TIME_LIMIT_IN_SECONDS)
                .count();
        if (count > ComplianceConstant.MAX_FREQUENCY) {
            fraudulentTransactions.computeIfAbsent(ComplianceConstant.HIGH_FREQUENCY, k -> new ArrayList<>())
                    .add(transaction.getTransactionId());
        }
    }

    private void isHighValue(Transaction transaction, Map<String, List<String>> fraudulentTransactions) {
        if (transaction.getAmount() > ComplianceConstant.TRANSACTION_UPPER_LIMIT) {
            fraudulentTransactions.computeIfAbsent(ComplianceConstant.HIGH_VALUE, k -> new ArrayList<>())
                    .add(transaction.getTransactionId());
        }
    }

    private void isOddHour(Transaction transaction, Map<String, List<String>> fraudulentTransactions) {
        if (transaction.getTimestamp().getHour() < ComplianceConstant.ODD_HOUR_TIME) {
            fraudulentTransactions.computeIfAbsent(ComplianceConstant.ODD_HOUR, k -> new ArrayList<>())
                    .add(transaction.getTransactionId());
        }
    }

    private void isUnusualMerchant(Transaction transaction, List<Transaction> transactions, Map<String, List<String>> fraudulentTransactions) {
        if (transactions.stream()
                .filter(t -> !t.equals(transaction))
                .noneMatch(t -> t.getMerchantName().equals(transaction.getMerchantName()))) {
            fraudulentTransactions.computeIfAbsent(ComplianceConstant.UNUSUAL_MERCHANT, k -> new ArrayList<>())
                    .add(transaction.getTransactionId());
        }
    }

    private void isUserInSanctionList(Transaction transaction, Map<String, List<String>> fraudulentTransactions) {
        if (sanctionsList.stream().anyMatch(userId -> transaction.getUserId().equals(userId))) {
            fraudulentTransactions.computeIfAbsent(ComplianceConstant.SANCTION_LIST, k -> new ArrayList<>())
                    .add(transaction.getTransactionId());
        }
    }
}
