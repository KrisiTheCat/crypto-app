package com.krisi.crypto.dto;

import com.krisi.crypto.model.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing a user's cryptocurrency holding.
 * Contains information about the cryptocurrency, its quantity, the amount of money given (investment), current price, and the change in value.
 */
@Data
@AllArgsConstructor
public class HoldingDTO {

    private String symbol;
    private String name;
    private Double quantity;
    private Double investment;
    private Double price;
    private Double change;
    private Double changePct;
    private String changeType;

    /**
     * Constructs a new {@link HoldingDTO} from the given {@link Holding} and {@link SnapshotResponse}.
     * Calculates the price, change, and percentage change based on the snapshot data.
     *
     * @param holding the cryptocurrency holding information.
     * @param snapshot the snapshot containing the current crypto price.
     */
    public HoldingDTO(Holding holding, SnapshotResponse snapshot){
        this.symbol = holding.getCrypto().getSymbol();
        this.name = holding.getCrypto().getName();
        this.quantity = holding.getQuantity();
        this.investment = holding.getInvestment();
        this.price = snapshot.getLast()*holding.getQuantity();
        this.change = this.price-this.investment;
        this.changePct = ((this.price/this.investment)*100)-100;
        if(change<0) {
            this.change *= -1;
            this.changePct *= -1;
            this.changeType = "negative";
        }
        else{
            this.changeType = "positive";
        }
    }
}
