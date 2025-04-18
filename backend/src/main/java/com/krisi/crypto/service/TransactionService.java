package com.krisi.crypto.service;

import com.krisi.crypto.model.Transaction;
import com.krisi.crypto.model.User;
import com.krisi.crypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing cryptocurrency transactions of users.
 * This service handles creating, retrieving, deleting, and resetting transactions for users.
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Retrieves all the transactions in the system.
     * @return List of all {@link Transaction} objects
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

    /**
     * Retrieves all transactions for a specific user based on their ID.
     * @param id The ID of the user whose transactions are to be retrieved
     * @return List of {@link Transaction} objects associated with the given user
     */
    public List<Transaction> getAllTransactionsByUser(Long id) {
        return transactionRepository.getAllByUser(id);
    }

    /**
     * Creates and saves a new transaction.
     * @param transaction The {@link Transaction} object to be created
     * @return The saved {@link Transaction} object
     */
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.createTransaction(transaction);
    }

    /**
     * Deletes a transaction based on its ID.
     * @param id The ID of the transaction to be deleted
     */
    public void deleteTransaction(Long id) {
        transactionRepository.deleteTransaction(id);
    }

    /**
     * Resets all transactions for a given user by deleting them from the repository.
     * @param user The {@link User} whose transactions should be deleted
     */
    public void resetUser(User user) {
        transactionRepository.deleteAllForUser(user.getId());
    }
}
