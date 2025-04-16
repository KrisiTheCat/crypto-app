package com.krisi.crypto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing how much of a specific cryptocurrency a user has
 * Contains information such as the user, cryptocurrency, quantity held, and investment amount.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    private Crypto crypto;
    private Double quantity;
    private Double investment;
}
