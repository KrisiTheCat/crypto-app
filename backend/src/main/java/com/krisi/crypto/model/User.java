package com.krisi.crypto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

/**
 * Represents a user in the system.
 * A user can have multiple holdings (of cryptocurrencies) and transactions.
 * The user's balance, name, and password are stored, along with a list of associated holdings and transactions.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double balance;
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Holding> holdings;
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    /**
     * Retrieves the holding of a specific cryptocurrency for this user.
     * @param crypto The cryptocurrency to search for in the user's holdings
     * @return An Optional containing the holding if it exists, or an empty Optional if not
     */
    public Optional<Holding> getHoldingOf(Crypto crypto){
        return holdings.stream().filter((item)-> item.getCrypto().getId().equals(crypto.getId())).findFirst();
    }
}
