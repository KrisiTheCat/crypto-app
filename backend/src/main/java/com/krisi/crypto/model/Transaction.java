package com.krisi.crypto.model;

import com.krisi.crypto.enums.TransactionMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * Entity representing a transaction made by a user buying/selling a cryptocurrency.
 * Contains details about the user, cryptocurrency, transaction method, quantity, price, and date of the transaction.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    private Crypto crypto;
    private TransactionMethod method;
    private double quantity;
    private double price;
    private Date date;
}
