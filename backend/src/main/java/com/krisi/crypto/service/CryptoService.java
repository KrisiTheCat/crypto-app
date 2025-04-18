package com.krisi.crypto.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krisi.crypto.model.Crypto;
import com.krisi.crypto.model.SnapshotResponse;
import com.krisi.crypto.repository.CryptoRepository;
import lombok.Data;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for handling cryptocurrency-related operations.
 * This service connects to a WebSocket to receive real-time crypto price updates and stores the data.
 */
@Service
@Data
public class CryptoService implements CommandLineRunner {

    @Autowired
    private CryptoRepository cryptoRepository;

    private List<Crypto> cryptoList;
    private Map<Long, SnapshotResponse> snapshot = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Executed on application startup to connect to a WebSocket server.
     * Subscribes to real-time cryptocurrency data from the Kraken API.
     * @param args Command line arguments
     * @throws Exception if an error occurs while connecting to the WebSocket
     */
    @Override
    public void run(String... args) throws Exception {
        WebSocketClient client = new WebSocketClient(new URI("wss://ws.kraken.com/v2")) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connected to WebSocket server");
                cryptoList = cryptoRepository.findAll();
                StringBuilder request = new StringBuilder();
                for (Crypto cr : cryptoList) {
                    if (!request.isEmpty()) request.append(",");
                    request.append("\"").append(cr.getSymbol()).append("/USD\"");
                }
                this.send("{ \"method\": \"subscribe\", \"params\": { \"channel\": \"ticker\",\"symbol\": [" + request + "]}}");
            }

            @Override
            public void onMessage(String message) {
                if (message.contains("\"bid_qty\":")) {  // TODO: handle error cases better
                    try {
                        parseTickerData(message);
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        client.connect();
    }

    /**
     * Retrieves the {@link Crypto} entity by its ID.
     * @param id The ID of the cryptocurrency
     * @return The {@link Crypto} entity
     * @throws RuntimeException if the cryptocurrency is not found
     */
    public Crypto getById(Long id) {
        Optional<Crypto> crypto = cryptoRepository.findById(id);
        if (crypto.isEmpty()) {
            throw new RuntimeException("ERROR: no crypto with id=" + id);
        } else {
            return crypto.get();
        }
    }

    /**
     * Retrieves the current prices of a cryptocurrency by its ID.
     * @param id The ID of the cryptocurrency
     * @return The {@link SnapshotResponse} containing the current price and other details
     * @throws RuntimeException if no snapshot exists for the specified cryptocurrency
     */
    public SnapshotResponse getCryptoSnapshot(Long id) {
        if (!snapshot.containsKey(id)) {
            throw new RuntimeException("ERROR: no snapshot of crypto with id=" + id);
        }
        return snapshot.get(id);
    }

    /**
     * Retrieves the current prices of a cryptocurrency using its {@link Crypto} object.
     * @param crypto The cryptocurrency object
     * @return The {@link SnapshotResponse} containing the current price and other details
     * @throws RuntimeException if no snapshot exists for the specified cryptocurrency
     */
    public SnapshotResponse getCryptoSnapshot(Crypto crypto) {
        return getCryptoSnapshot(crypto.getId());
    }

    /**
     * Parses ticker data received from the WebSocket and updates the snapshot with the latest data.
     * @param json The raw JSON string containing the ticker data
     * @throws Exception if parsing fails or the data is invalid
     */
    public void parseTickerData(String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);
        JsonNode typeNode = root.get("type");
        if (typeNode != null) {
            JsonNode dataNode = root.get("data");
            if (dataNode != null && dataNode.isArray() && dataNode.size() > 0) {
                for (int i = 0; i < dataNode.size(); i++) {
                    SnapshotResponse response = objectMapper.treeToValue(dataNode.get(i), SnapshotResponse.class);
                    Optional<Crypto> crypto = cryptoRepository.findBySymbol(response.getOnlySymbol());
                    if (crypto.isEmpty()) {
                        throw new Exception("No crypto with symbol " + response.getOnlySymbol() + " found");
                    }
                    snapshot.put(crypto.get().getId(), response);
                }
            } else {
                throw new IllegalArgumentException("Invalid or empty data array");
            }
        }
    }
}
