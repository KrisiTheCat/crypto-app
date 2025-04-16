package com.krisi.crypto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a cryptocurrency.
 * Contains information such as the cryptocurrency's ID, symbol, and name.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String symbol;  //three-four letter symbol
    private String name;    //full name
}
