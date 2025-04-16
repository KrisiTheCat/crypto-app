package com.krisi.crypto.dto;

import com.krisi.crypto.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a user's information.
 * Contains details such as the user's name, balance, transactions, and holdings.
 */
@Data
@AllArgsConstructor
public class UserDTO {

    private String name;
    private double balance;
    private List<TransactionDTO> transactions;
    private List<HoldingDTO> holdings;

    /**
     * Constructs a new {@link UserDTO} from the given {@link User} entity.
     * This constructor initializes the user's name, balance, transactions, and holdings.
     *
     * @param user the user entity containing the user's basic information.
     * @param transactions a list of {@link TransactionDTO} representing the user's transactions.
     * @param holdings a list of {@link HoldingDTO} representing the user's cryptocurrency holdings.
     */
    public UserDTO(User user, List<TransactionDTO> transactions, List<HoldingDTO> holdings){
        this.name = user.getName();
        this.balance = user.getBalance();
        this.holdings = holdings;
        this.transactions = transactions;
    }
}
