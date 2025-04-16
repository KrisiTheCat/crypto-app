package com.krisi.crypto.dto;

import com.krisi.crypto.model.Crypto;
import com.krisi.crypto.model.SnapshotResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing the snapshot data of a cryptocurrency.
 * Contains information such as the cryptocurrency's price, change, and the type of change (positive/negative).
 */
@Data
@AllArgsConstructor
public class SnapshotDTO {

    private Long cryptoId;
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double changePct;
    private String changeType;

    /**
     * Constructs a new {@link SnapshotDTO} from the given {@link SnapshotResponse} and {@link Crypto}.
     * Extracts and sets the necessary cryptocurrency information along with price and change details.
     *
     * @param response the snapshot response containing price and change data.
     * @param crypto the cryptocurrency entity containing information such as symbol and name.
     */
    public SnapshotDTO(SnapshotResponse response, Crypto crypto){
        this.cryptoId = crypto.getId();
        this.symbol = crypto.getSymbol();
        this.name = crypto.getName();
        this.price = response.getLast();
        this.change = response.getChange();
        this.changePct = response.getChange_pct();
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
