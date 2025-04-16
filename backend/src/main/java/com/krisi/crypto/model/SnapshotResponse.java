package com.krisi.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the response data for a cryptocurrency snapshot.
 * Contains real-time data such as bid, ask, last price, volume, and price change.
 */
@Data
public class SnapshotResponse {
    private String symbol;
    private double bid;
    private double bid_qty;
    private double ask;
    private double ask_qty;
    private double last;
    private double volume;
    private double vwap;
    private double low;
    private double high;
    private double change;
    private double change_pct;

    public String getOnlySymbol(){
        return symbol.split("/")[0];
    }
}
