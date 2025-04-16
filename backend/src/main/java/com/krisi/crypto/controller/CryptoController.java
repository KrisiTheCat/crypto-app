package com.krisi.crypto.controller;

import com.krisi.crypto.dto.SnapshotDTO;
import com.krisi.crypto.service.CryptoService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * REST controller for retrieving cryptocurrency price information.
 * Provides an endpoint to fetch real-time cryptocurrency prices.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CryptoController {
    private static final Logger logger = LoggerFactory.getLogger(CryptoController.class);

    @Autowired
    private CryptoService cryptoService;

    /**
     * Fetches real-time cryptocurrency prices.
     * The endpoint returns information about each cryptocurrency in the snapshot.
     *
     * @return a {@link ResponseEntity} containing a list of {@link SnapshotDTO} objects,
     *         or a 400 BAD REQUEST on failure.
     */
    @GetMapping("/api/snapshot")
    public ResponseEntity getLastSnapshot() {
        try {
            return ResponseEntity.ok(cryptoService.getSnapshot()
                    .entrySet().stream()
                    .map(entry -> new SnapshotDTO(entry.getValue(), cryptoService.getCrypto(entry.getKey())))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            logger.error("Error in getLastSnapshot (): {}", stacktrace);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
