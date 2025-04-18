package com.krisi.crypto.service;

import com.krisi.crypto.enums.TransactionMethod;
import com.krisi.crypto.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransactionOrchestrator {

    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private UserService userService;
    @Autowired
    private HoldingService holdingService;
    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionOrchestrator.class);

    boolean userUpdated;
    boolean holdingUpdated;
    boolean transactionUpdated;

    /**
     * Executes a transaction order and ensures all operations are consistent and atomic.
     *
     * @param request The request containing transaction details.
     * @return The transaction that was executed.
     * @throws Exception If the transaction fails due to validation or business logic errors.
     */
    public Transaction execute(TransactionRequest request) throws Exception {
        logger.info("Received transaction order: {}", request.toString());
        Transaction transaction = null;
        userUpdated = false;
        holdingUpdated = false;
        transactionUpdated = false;

        try {
            validateRequest(request);

            System.out.println(request.toString());
            Crypto crypto = cryptoService.getById(request.getCryptoId());

            SnapshotResponse snapshotResponse = cryptoService.getCryptoSnapshot(crypto);

            if (snapshotResponse == null) throw new Exception("Crypto unsupported");

            User user = userService.getUserInfo(request.getUserId());

            transaction = createTransaction(request, user, crypto, snapshotResponse);

            handleRequest(transaction, user);

            logger.info("Transaction order completed successfully: {}", transaction.toString());
            return transaction;
        } catch (Exception e) {
            logger.error("Transaction failed: {}", e.getMessage(), e);

            if (transaction != null) {
                logger.error("Failed transaction: {}", transaction.toString());
                compensate(transaction);
            }
            throw e;
        }
    }

    /**
     * Validates the data in the transaction request to ensure it is correct.
     *
     * @param request The transaction request containing details for validation.
     * @throws Exception If any validation fails.
     */
    private void validateRequest(TransactionRequest request) throws Exception {
        if (request.getUserId() < 0 ||
                request.getCryptoId() < 0 ||
                !(request.getMethod().equals("BUY") || request.getMethod().equals("SELL")) ||
                request.getQuantity() <= 0 ||
                request.getDate() == null) {
            throw new Exception("Invalid data for transaction request");
        }
    }

    /**
     * Creates a transaction object based on the provided details.
     *
     * @param request The transaction request containing transaction details.
     * @param user The user executing the transaction.
     * @param crypto The cryptocurrency involved in the transaction.
     * @param snapshotResponse The snapshot response of the crypto price.
     * @return A newly created Transaction object.
     */
    private Transaction createTransaction(TransactionRequest request, User user, Crypto crypto, SnapshotResponse snapshotResponse) {
        TransactionMethod method = TransactionMethod.valueOf(request.getMethod());
        double price = request.getQuantity() * snapshotResponse.getLast();

        return new Transaction(null, user, crypto, method, request.getQuantity(), price, request.getDate());
    }

    /**
     * Handles the logic for processing a buy or sell transaction, depending on the method.
     *
     * @param transaction The transaction to be processed.
     * @param user The user executing the transaction.
     * @throws Exception If the user does not have enough balance for a buy transaction.
     * @throws Exception If the user does not have enough crypto for a sell transaction.
     */
    private void handleRequest(Transaction transaction, User user) throws Exception {
        if (transaction.getMethod() == TransactionMethod.BUY) {
            handleBuy(transaction, user);
        } else {
            handleSell(transaction, user);
        }
        holdingUpdated = true;

        transactionService.createTransaction(transaction);
        transactionUpdated = true;

        userService.updateBalance(transaction);
        userUpdated = true;
    }

    /**
     * Processes the buy transaction by checking if the user has enough balance and
     * adding the holding if the transaction is valid.
     *
     * @param transaction The transaction to be processed.
     * @param user The user executing the transaction.
     * @throws Exception If the user does not have enough balance for the transaction.
     */
    private void handleBuy(Transaction transaction, User user) throws Exception {
        if (user.getBalance() < transaction.getPrice()) {
            throw new Exception("Not enough money to complete the buy transaction");
        }

        Holding holding = new Holding(null, user, transaction.getCrypto(), transaction.getQuantity(), transaction.getPrice());
        holdingService.addHolding(holding);
    }

    /**
     * Processes the sell transaction by ensuring the user has enough crypto holdings
     * and removing the holding if the transaction is valid.
     *
     * @param transaction The transaction to be processed.
     * @param user The user executing the transaction.
     * @throws Exception If the user does not have enough crypto for the transaction.
     */
    private void handleSell(Transaction transaction, User user) throws Exception {
        Holding existingHolding = holdingService.getByUserIdAndCryptoId(user.getId(), transaction.getCrypto().getId());
        if (existingHolding == null || transaction.getQuantity() - existingHolding.getQuantity() > 0.00001) {
            throw new Exception("Not enough crypto to sell");
        }

        Holding holding = new Holding(null, user, transaction.getCrypto(), transaction.getQuantity(), transaction.getPrice());
        holdingService.removeHolding(holding);
    }

    /**
     * Compensates for a failed transaction by rolling back any changes made.
     *
     * @param transaction The failed transaction that needs compensation.
     */
    private void compensate(Transaction transaction) {
        try {
            User user = transaction.getUser();

            if(holdingUpdated) {
                Holding holding = new Holding(null, user, transaction.getCrypto(), transaction.getQuantity(), transaction.getPrice());
                if (transaction.getMethod() == TransactionMethod.BUY) {
                    holdingService.removeHolding(holding);
                } else {
                    holdingService.addHolding(holding);
                }
            }

            if(transactionUpdated){
                transactionService.deleteTransaction(transaction.getId());
            }

            if(userUpdated){
                userService.refundTransaction(transaction);
            }

            logger.info("Compensation actions completed successfully for transaction: {}", transaction.toString());
        } catch (Exception e) {
            logger.error("Compensation failed: {}", e.getMessage(), e);
        }
    }
}
