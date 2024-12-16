package com.atlasfin.compliance.util;

import com.atlasfin.compliance.dto.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ComplianceUtil {

    private static Logger log = LoggerFactory.getLogger((ComplianceUtil.class));

    /**
     * Convert csv to transaction object
     *
     * @param is
     * @return transactions
     */
    public static List<Transaction> csvToTransactions(InputStream is) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = CSVFormat.Builder.create()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(fileReader)) {

            for (CSVRecord csvRecord : csvParser) {
                Transaction transaction = Transaction.builder().transactionId(csvRecord.get("transactionId")).userId(csvRecord.get("userId")).timestamp(LocalDateTime.parse(csvRecord.get("timestamp"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).merchantName(csvRecord.get("merchantName")).amount(Double.parseDouble(csvRecord.get("amount"))).build();
                transactions.add(transaction);
            }
        } catch (IOException e) {
            log.error("File is not correct {}", e.getMessage());
        }
        return transactions;
    }

    public static List<String> csvToList(InputStream is) {
        List<String> userIds = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = CSVFormat.Builder.create()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(fileReader)) {

            for (CSVRecord record : csvParser) {
                userIds.add(record.get("user_id")); // Replace "user_id" with the correct column name in your CSV file
            }

        } catch (IOException e) {
            log.error("Error while reading the CSV file: {}", e.getMessage());
        }
        return userIds;
    }
}
