package com.krisi.crypto.controller;

import com.krisi.crypto.dto.HoldingDTO;
import com.krisi.crypto.dto.TransactionDTO;
import com.krisi.crypto.dto.UserDTO;
import com.krisi.crypto.model.*;
import com.krisi.crypto.service.HoldingService;
import com.krisi.crypto.service.CryptoService;
import com.krisi.crypto.service.TransactionService;
import com.krisi.crypto.service.UserService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing users and their crypto-related data.
 * Provides endpoints to fetch all users, retrieve detailed information about a specific user, and reset a user's transaction and holding history.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private HoldingService holdingService;
    @Autowired
    private CryptoService cryptoService;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of {@link User} objects.
     */
    @GetMapping("/api/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Retrieves detailed information for a specific user, including their transactions and holdings.
     *
     * @param id the ID of the user to retrieve.
     * @return a {@link ResponseEntity} containing a {@link UserDTO} if successful, or a 400 BAD REQUEST on error.
     */
    @GetMapping("/api/users/{id}")
    public ResponseEntity getUserInfo(@PathVariable Long id){
        try {
            User user = userService.getUserInfo(id);

            UserDTO userDTO = new UserDTO(
                    user,
                    user.getTransactions()
                            .stream()
                            .map(transaction -> {
                                return new TransactionDTO(transaction);
                            })
                            .collect(Collectors.toList()),
                    user.getHoldings()
                            .stream()
                            .map(holding -> {
                                return new HoldingDTO(holding, cryptoService.getCryptoSnapshot(holding.getCrypto()));
                            })
                            .collect(Collectors.toList()));
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            logger.error("Error in getUserInfo({}): {}",id, stacktrace);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Resets a user's transaction and holding data.
     *
     * @param id the ID of the user to reset.
     * @return a {@link ResponseEntity} containing an empty {@link UserDTO} if successful, or a 400 BAD REQUEST on error.
     */
    @PostMapping ("/api/users/reset/{id}")
    public ResponseEntity resetUser(@PathVariable Long id){
        try {
            User user = userService.resetUser(id);
            transactionService.resetUser(user);
            holdingService.resetUser(user);

            return ResponseEntity.ok(new UserDTO(
                    user,
                    new ArrayList<>(),
                    new ArrayList<>()));
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            logger.error("Error in resetUser({}): {}",id, stacktrace);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
