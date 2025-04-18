package com.krisi.crypto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
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
public class User  implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double balance;
    private String password;

}
