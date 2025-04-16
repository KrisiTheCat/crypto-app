package com.krisi.crypto.repository;

import com.krisi.crypto.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for interacting with the {@link Crypto} entity.
 * Provides methods for performing CRUD operations and custom queries related to cryptocurrencies.
 */
public interface CryptoRepository extends JpaRepository<Crypto, Long> {

    /**
     * Finds a cryptocurrency by its unique identifier.
     * @param id The unique identifier of the cryptocurrency
     * @return An Optional containing the cryptocurrency if found, otherwise an empty Optional
     */
    Optional<Crypto> findById(Long id);

    /**
     * Finds a list of cryptocurrencies by their symbol.
     * @param symbol The symbol of the cryptocurrency
     * @return A list of cryptocurrencies that match the given symbol
     */
    List<Crypto> findBySymbol(String symbol);
}