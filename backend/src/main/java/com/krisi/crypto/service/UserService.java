package com.krisi.crypto.service;

import com.krisi.crypto.enums.TransactionMethod;
import com.krisi.crypto.model.Transaction;
import com.krisi.crypto.model.User;
import com.krisi.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing users and their transactions in the crypto system.
 * This service handles user creation, information retrieval, transaction processing, and balance management.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users in the system.
     * @return List of all {@link User} objects
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates and saves a new user.
     * @param user The {@link User} object to be created
     * @return The saved {@link User} object
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves information about a specific user based on their ID.
     * @param id The ID of the user to retrieve
     * @return The {@link User} object associated with the provided ID
     * @throws UserNotFoundException If the user with the provided ID does not exist
     */
    public User getUserInfo(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " doesn't exist"));
    }

    /**
     * Processes a transaction by updating the user's balance based on the transaction method.
     * @param transaction The {@link Transaction} object containing details of the transaction
     * @return The updated {@link User} object after the transaction
     */
    public User updateBalance(Transaction transaction) {
        User user = transaction.getUser();
        double newBalance = user.getBalance();

        if (transaction.getMethod() == TransactionMethod.BUY) {
            newBalance -= transaction.getPrice();
        } else {
            newBalance += transaction.getPrice();
        }

        user.setBalance(newBalance);
        return userRepository.save(user); // Save after updating balance
    }

    /**
     * Refunds a transaction by updating the user's balance to reverse the transaction effect.
     * @param transaction The {@link Transaction} object containing details of the transaction to refund
     * @return The updated {@link User} object after the refund
     */
    public User refundTransaction(Transaction transaction) {
        User user = transaction.getUser();
        double newBalance = user.getBalance();

        if (transaction.getMethod() == TransactionMethod.BUY) {
            newBalance += transaction.getPrice();
        } else {
            newBalance -= transaction.getPrice();
        }

        user.setBalance(newBalance);
        return userRepository.save(user); // Save after updating balance
    }

    /**
     * Saves the updated user information to the repository.
     * @param user The {@link User} object to save
     * @return The saved {@link User} object
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Resets the user's balance to 10,000 and saves the updated user data.
     * @param id The ID of the user to reset
     * @return The {@link User} object after the reset, or null if no user was found
     */
    public User resetUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBalance(10000);
                    return userRepository.save(user);
                })
                .orElse(null);
    }

    /**
     * Custom exception to handle cases when a user is not found.
     */
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
