package com.atlasfin.compliance.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Transaction {
    private String transactionId;
    private String userId;
    private LocalDateTime timestamp;
    private String merchantName;
    private double amount;
}
