package com.krisi.crypto.repository;

import com.krisi.crypto.enums.TransactionMethod;
import com.krisi.crypto.model.Crypto;
import com.krisi.crypto.model.Transaction;
import com.krisi.crypto.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing {@link Transaction} entities.
 * Provides methods to perform CRUD operations and custom queries
 * on the transaction table using {@link JdbcTemplate}.
 */
@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CryptoRepository cryptoRepository;

    /**
     * Constructs a {@code TransactionRepository} with a given {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the JdbcTemplate used for database operations
     */
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new transaction into the database.
     *
     * @param transaction the transaction to be inserted
     * @return the transaction with the generated ID
     */
    public Transaction createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transaction (user_id, crypto_id, method, quantity, price, date) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                transaction.getUser().getId(),
                transaction.getCrypto().getId(),
                transaction.getMethod().name(),
                transaction.getQuantity(),
                transaction.getPrice(),
                new Timestamp(transaction.getDate().getTime()));

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        transaction.setId(id);
        return transaction;
    }

    /**
     * Retrieves all transactions from the database.
     *
     * @return a list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transaction";
        return jdbcTemplate.query(sql, new TransactionRowMapper());
    }

    /**
     * Retrieves all transactions for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of transactions for the user
     */
    public List<Transaction> getAllByUser(Long userId) {
        String sql = "SELECT * FROM transaction WHERE user_id = ?";
        return jdbcTemplate.query(sql, new TransactionRowMapper(), userId);
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id the ID of the transaction
     * @return an Optional containing the transaction if found, otherwise empty
     */
    public Optional<Transaction> getTransactionById(Long id) {
        String sql = "SELECT * FROM transaction WHERE id = ?";
        List<Transaction> result = jdbcTemplate.query(sql, new TransactionRowMapper(), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id the ID of the transaction to delete
     * @throws RuntimeException if no transaction is found with the given ID
     */
    public void deleteTransaction(Long id) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new RuntimeException("No transaction found with id=" + id);
        }
    }

    /**
     * Deletes all transactions for a specific user.
     *
     * @param userId the ID of the user
     */
    public void deleteAllForUser(Long userId) {
        String sql = "DELETE FROM transaction WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    /**
     * Maps rows from the transaction table to {@link Transaction} objects.
     */
    private class TransactionRowMapper implements RowMapper<Transaction> {

        /**
         * Maps a single row from the ResultSet to a {@link Transaction} object.
         *
         * @param rs the ResultSet
         * @param rowNum the current row number
         * @return a mapped {@link Transaction} object
         * @throws SQLException if a database access error occurs
         */
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getLong("id"));

            Optional<User> userOptional = userRepository.getById(rs.getLong("user_id"));
            if (userOptional.isEmpty())
                throw new RuntimeException("No user with id=" + rs.getLong("user_id") + " found");
            transaction.setUser(userOptional.get());

            Optional<Crypto> cryptoOptional = cryptoRepository.findById(rs.getLong("crypto_id"));
            if (cryptoOptional.isEmpty())
                throw new RuntimeException("No crypto with id=" + rs.getLong("crypto_id") + " found");
            transaction.setCrypto(cryptoOptional.get());

            transaction.setMethod(TransactionMethod.valueOf(rs.getString("method")));
            transaction.setQuantity(rs.getDouble("quantity"));
            transaction.setPrice(rs.getDouble("price"));
            transaction.setDate(rs.getTimestamp("date"));
            return transaction;
        }
    }
}
