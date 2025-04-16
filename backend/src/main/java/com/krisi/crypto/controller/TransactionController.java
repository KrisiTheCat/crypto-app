package com.krisi.crypto.controller;

import com.krisi.crypto.dto.TransactionDTO;
import com.krisi.crypto.model.*;
import com.krisi.crypto.service.TransactionOrchestrator;
import com.krisi.crypto.service.TransactionService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing cryptocurrency transactions.
 * Provides endpoints to fetch all transactions and to create new ones.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionOrchestrator transactionOrchestrator;

    /**
     * Retrieves all transactions made in the system.
     *
     * @return a {@link ResponseEntity} containing a list of {@link Transaction} objects.
     */
    @GetMapping("/api/transactions")
    public ResponseEntity getAllTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    /**
     * Creates a new transaction based on the provided request data.
     *
     * @param transactionRequest a {@link TransactionRequest} containing the data for the new transaction.
     * @return a {@link ResponseEntity} containing the created {@link TransactionDTO}, or 400 BAD REQUEST on failure.
     */
    @PostMapping("/api/transactions")
    public ResponseEntity createTransaction(@RequestBody TransactionRequest transactionRequest){
        try{
            Transaction transaction = transactionOrchestrator.execute(transactionRequest);
            return ResponseEntity.ok(new TransactionDTO(transaction));
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            logger.error("Error in createTransaction({}): {}",transactionRequest, stacktrace);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
