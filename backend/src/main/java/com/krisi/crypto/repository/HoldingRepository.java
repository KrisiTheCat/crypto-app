package com.krisi.crypto.repository;

import com.krisi.crypto.model.Crypto;
import com.krisi.crypto.model.Holding;
import com.krisi.crypto.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing {@link Holding} entities in the database.
 * Provides methods for creating, updating, retrieving, and deleting holdings.
 */
@Repository
public class HoldingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptoRepository cryptoRepository;

    /**
     * Constructs a new {@code HoldingRepository} with the given {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the JdbcTemplate used for SQL operations
     */
    public HoldingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Creates a new {@link Holding} in the database.
     *
     * @param holding the holding to be persisted
     * @return the saved holding with the generated ID
     */
    public Holding createHolding(Holding holding) {
        String sql = "INSERT INTO holding (user_id, crypto_id, quantity, investment) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                holding.getUser().getId(),
                holding.getCrypto().getId(),
                holding.getQuantity(),
                holding.getInvestment());

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        holding.setId(id);
        return holding;
    }

    /**
     * Updates an existing {@link Holding} in the database.
     *
     * @param holding the holding to be updated
     * @return the updated holding
     * @throws RuntimeException if no record is found to update
     */
    public Holding updateHolding(Holding holding) {
        String sql = "UPDATE holding SET user_id = ?, crypto_id = ?, quantity = ?, investment = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                holding.getUser().getId(),
                holding.getCrypto().getId(),
                holding.getQuantity(),
                holding.getInvestment(),
                holding.getId());

        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update holding with id=" + holding.getId());
        }

        return holding;
    }

    /**
     * Retrieves all holdings from the database.
     *
     * @return a list of all {@link Holding} records
     */
    public List<Holding> getAllHoldings() {
        String sql = "SELECT * FROM holding";
        return jdbcTemplate.query(sql, new HoldingRowMapper());
    }

    /**
     * Retrieves a {@link Holding} by its ID.
     *
     * @param id the ID of the holding
     * @return an {@link Optional} containing the holding if found, or empty otherwise
     */
    public Optional<Holding> getById(Long id) {
        String sql = "SELECT * FROM holding WHERE id = ?";
        List<Holding> result = jdbcTemplate.query(sql, new HoldingRowMapper(), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * Retrieves a {@link Holding} by user ID and crypto ID.
     *
     * @param userId the ID of the user
     * @param cryptoId the ID of the crypto
     * @return an {@link Optional} containing the holding if found, or empty otherwise
     */
    public Optional<Holding> getByUserIdAndCryptoId(Long userId, Long cryptoId) {
        String sql = "SELECT * FROM holding WHERE user_id = ? AND crypto_id = ?";
        List<Holding> result = jdbcTemplate.query(sql, new HoldingRowMapper(), userId, cryptoId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * Retrieves all holdings associated with a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link Holding} objects owned by the user
     */
    public List<Holding> getAllByUser(Long userId) {
        String sql = "SELECT * FROM holding WHERE user_id = ?";
        return jdbcTemplate.query(sql, new HoldingRowMapper(), userId);
    }

    /**
     * Deletes a holding by its ID.
     *
     * @param id the ID of the holding to delete
     * @throws RuntimeException if no holding is found with the given ID
     */
    public void deleteHolding(Long id) {
        String sql = "DELETE FROM holding WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new RuntimeException("No holding found with id=" + id);
        }
    }

    /**
     * Deletes all holdings associated with a specific user.
     *
     * @param userId the ID of the user
     */
    public void deleteAllForUser(Long userId) {
        String sql = "DELETE FROM holding WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    /**
     * RowMapper implementation for mapping SQL rows to {@link Holding} objects.
     */
    private class HoldingRowMapper implements RowMapper<Holding> {

        /**
         * Maps a single row of a ResultSet to a {@link Holding} object.
         *
         * @param rs the ResultSet positioned at the current row
         * @param rowNum the number of the current row
         * @return a fully initialized Holding object
         * @throws SQLException if a SQL error occurs or related entities are not found
         */
        @Override
        public Holding mapRow(ResultSet rs, int rowNum) throws SQLException {
            Holding holding = new Holding();
            holding.setId(rs.getLong("id"));

            Optional<User> userOptional = userRepository.getById(rs.getLong("user_id"));
            if (userOptional.isEmpty())
                throw new RuntimeException("No user with id=" + rs.getLong("user_id") + " found");
            else
                holding.setUser(userOptional.get());

            Optional<Crypto> cryptoOptional = cryptoRepository.findById(rs.getLong("crypto_id"));
            if (cryptoOptional.isEmpty())
                throw new RuntimeException("No crypto with id=" + rs.getLong("crypto_id") + " found");
            else
                holding.setCrypto(cryptoOptional.get());

            holding.setQuantity(rs.getDouble("quantity"));
            holding.setInvestment(rs.getDouble("investment"));

            return holding;
        }
    }
}
