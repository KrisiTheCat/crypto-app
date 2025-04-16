package com.krisi.crypto.repository;

import com.krisi.crypto.model.Crypto;
import com.krisi.crypto.model.Holding;
import com.krisi.crypto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for interacting with the {@link Holding} entity.
 * Provides methods for performing CRUD operations related to holdings.
 */
public interface HoldingRepository extends JpaRepository<Holding, Long> { }
