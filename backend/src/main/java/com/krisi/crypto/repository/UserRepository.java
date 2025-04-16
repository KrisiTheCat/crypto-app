package com.krisi.crypto.repository;

import com.krisi.crypto.model.Transaction;
import com.krisi.crypto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with the {@link User} entity.
 * Provides methods for performing CRUD operations related to users.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
