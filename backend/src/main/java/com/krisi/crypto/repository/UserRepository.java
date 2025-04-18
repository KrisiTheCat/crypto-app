package com.krisi.crypto.repository;

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
 * Repository class for managing {@link User} entities.
 * Provides methods for creating, retrieving, and updating user data using {@link JdbcTemplate}.
 */
@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a {@code UserRepository} with the specified {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the JdbcTemplate used for database operations
     */
    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user the user to be created
     * @return the created user with the generated ID
     */
    public User createUser(User user) {
        String sql = "INSERT INTO user (name, balance, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getBalance(), user.getPassword());

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        user.setId(id);
        return user;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return an Optional containing the user if found, otherwise empty
     */
    public Optional<User> getById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        List<User> result = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * Updates the balance of the user with the given ID.
     *
     * @param userId     the ID of the user to update
     * @param newBalance the new balance to set
     * @return the updated user
     * @throws RuntimeException if no user is found with the given ID or if the user cannot be retrieved afterward
     */
    public User updateBalance(Long userId, double newBalance) {
        String sql = "UPDATE user SET balance = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newBalance, userId);

        if (rowsAffected == 0) {
            throw new RuntimeException("No user found with id=" + userId);
        }

        Optional<User> userOptional = getById(userId);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found after updating balance"));
    }

    /**
     * RowMapper for mapping rows from the user table to {@link User} objects.
     */
    private class UserRowMapper implements RowMapper<User> {
        /**
         * Maps a row from the {@link ResultSet} to a {@link User} object.
         *
         * @param rs the result set
         * @param rowNum the current row number
         * @return a {@link User} object representing the row data
         * @throws SQLException if an SQL exception occurs
         */
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setBalance(rs.getDouble("balance"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }
}
