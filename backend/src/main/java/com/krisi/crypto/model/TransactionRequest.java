package com.krisi.crypto.model;

import com.krisi.crypto.enums.TransactionMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents a request to create a transaction.
 * Contains the necessary information for processing a cryptocurrency transaction, including user ID, cryptocurrency ID, transaction method, quantity, and date.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private Long userId;
    private Long cryptoId;
    private String method;
    private Double quantity;
    private Date date;
}
