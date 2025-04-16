package com.krisi.crypto.repository;

import com.krisi.crypto.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for interacting with the {@link Transaction} entity.
 * Provides methods for performing CRUD operations related to transactions.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds a list of {@link Transaction}s by the given user ID.
     * @param userId The ID of the {@link Transaction} user
     * @return A list of {@link Transaction}s associated with the specified user ID
     */
    List<Transaction> findByUserId(Long userId);
}