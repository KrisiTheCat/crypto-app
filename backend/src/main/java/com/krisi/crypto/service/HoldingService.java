package com.krisi.crypto.service;

import com.krisi.crypto.model.Holding;
import com.krisi.crypto.model.User;
import com.krisi.crypto.repository.HoldingRepository;
import com.krisi.crypto.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing cryptocurrency holdings of users.
 * This service handles adding, removing, and retrieving holdings, as well as resetting holdings for a user.
 */
@Service
public class HoldingService {

    @Autowired
    private HoldingRepository holdingRepository;

    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    private CryptoService cryptoService;

    /**
     * Retrieves all the holdings in the system.
     * @return List of all {@link Holding} objects
     */
    public List<Holding> getAllHoldings() {
        return holdingRepository.findAll();
    }

    /**
     * Adds a new holding to the user's portfolio. If the user already holds the specified cryptocurrency,
     * the existing holding's quantity and investment are updated.
     * @param holding The {@link Holding} object to add
     */
    public void addHolding(Holding holding) {
        Optional<Holding> optionalHolding = holding.getUser().getHoldingOf(holding.getCrypto());
        if (optionalHolding.isPresent()) {
            // If the user already holds the crypto, update the existing holding's quantity and investment
            Holding holdingOld = optionalHolding.get();
            holdingOld.setQuantity(holding.getQuantity() + holdingOld.getQuantity());
            holdingOld.setInvestment(holding.getInvestment() + holdingOld.getInvestment());
        } else {
            holding.getUser().getHoldings().add(holding);
        }
    }

    /**
     * Removes a holding from the user's portfolio. If the quantity of the holding matches exactly,
     * it is deleted from the user's holdings.
     * @param holding The {@link Holding} object to remove
     * @throws RuntimeException if the holding doesn't exist in the user's portfolio
     */
    public void removeHolding(Holding holding) {
        Optional<Holding> optionalHolding = holding.getUser().getHoldingOf(holding.getCrypto());
        if (optionalHolding.isPresent()) {
            Holding holdingOld = optionalHolding.get();
            if (Math.abs(holdingOld.getQuantity() - holding.getQuantity()) <= 0.000001) {
                // If the quantity matches exactly, remove the holding completely
                holding.getUser().getHoldings().remove(holdingOld);
                holdingRepository.delete(holdingOld);
                return;
            }
            holdingOld.setQuantity(holdingOld.getQuantity() - holding.getQuantity());
            holdingOld.setInvestment(holdingOld.getInvestment() - holding.getInvestment());
        } else {
            throw new RuntimeException("Trying to remove a nonexistent holding");
        }
    }

    /**
     * Resets all holdings for a given user. This will remove all holdings associated with the user.
     * @param user The {@link User} whose holdings should be deleted
     */
    public void resetUser(User user) {
        holdingRepository.deleteAllInBatch(user.getHoldings());
    }
}
