package com.krisi.crypto.dto;

import com.krisi.crypto.enums.TransactionMethod;
import com.krisi.crypto.model.Crypto;
import com.krisi.crypto.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing a cryptocurrency transaction.
 * Contains information such as the cryptocurrency name, symbol, transaction method, quantity, price, and date.
 */
@Data
@AllArgsConstructor
public class TransactionDTO {

    private String name;
    private String symbol;
    private TransactionMethod method;
    private double quantity;
    private double price;
    private Date date;

    /**
     * Constructs a new {@link TransactionDTO} from the given {@link Transaction}.
     * Extracts and sets the relevant details from the transaction.
     *
     * @param transaction the transaction entity containing information such as crypto name, symbol, and transaction details.
     */
    public TransactionDTO(Transaction transaction){
        this.name = transaction.getCrypto().getName();
        this.symbol = transaction.getCrypto().getSymbol();
        this.quantity = transaction.getQuantity();
        this.method = transaction.getMethod();
        this.price = transaction.getPrice();
        this.date = transaction.getDate();
    }
}
