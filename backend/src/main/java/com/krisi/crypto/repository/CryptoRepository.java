package com.krisi.crypto.repository;

import com.krisi.crypto.model.Crypto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for performing database operations related to {@link Crypto} entities.
 * Utilizes {@link JdbcTemplate} to interact with the underlying SQL database.
 */
@Repository
public class CryptoRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@code CryptoRepository} with the specified {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the JdbcTemplate to use for database access
     */
    public CryptoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new {@link Crypto} entity into the database.
     *
     * @param crypto the Crypto entity to be created
     * @return the created Crypto entity with its generated ID set
     */
    public Crypto createCrypto(Crypto crypto) {
        String sql = "INSERT INTO crypto (symbol, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, crypto.getSymbol(), crypto.getName());

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        crypto.setId(id);
        return crypto;
    }

    /**
     * Retrieves all {@link Crypto} records from the database.
     *
     * @return a list of all Crypto entities
     */
    public List<Crypto> findAll() {
        String sql = "SELECT * FROM crypto";
        return jdbcTemplate.query(sql, new CryptoRowMapper());
    }

    /**
     * Retrieves a {@link Crypto} entity by its unique ID.
     *
     * @param id the ID of the Crypto entity to retrieve
     * @return an {@link Optional} containing the Crypto entity if found, or empty if not
     */
    public Optional<Crypto> findById(Long id) {
        String sql = "SELECT * FROM crypto WHERE id = ?";
        List<Crypto> results = jdbcTemplate.query(sql, new CryptoRowMapper(), id);

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }

    /**
     * Retrieves a {@link Crypto} entity by its symbol.
     *
     * @param symbol the symbol of the Crypto to find
     * @return an {@link Optional} containing the Crypto entity if found, or empty if not
     */
    public Optional<Crypto> findBySymbol(String symbol) {
        String sql = "SELECT * FROM crypto WHERE symbol = \"" + symbol + "\"";
        List<Crypto> results = jdbcTemplate.query(sql, new CryptoRowMapper());

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }

    /**
     * Maps rows of a ResultSet to {@link Crypto} objects.
     */
    private static class CryptoRowMapper implements RowMapper<Crypto> {
        /**
         * Maps a single row of the ResultSet to a {@link Crypto} object.
         *
         * @param rs the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return a fully populated Crypto object
         * @throws SQLException if a database access error occurs
         */
        @Override
        public Crypto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Crypto(
                    rs.getLong("id"),
                    rs.getString("symbol"),
                    rs.getString("name")
            );
        }
    }
}
