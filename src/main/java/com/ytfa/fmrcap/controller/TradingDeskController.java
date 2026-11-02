package com.ytfa.fmrcap.controller;

import com.ytfa.fmrcap.entity.TradingDesk;
import com.ytfa.fmrcap.service.TradingDeskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trading-desks")
public class TradingDeskController {

    private final TradingDeskService tradingDeskService;

    public TradingDeskController(TradingDeskService tradingDeskService) {
        this.tradingDeskService = tradingDeskService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
            @Valid @RequestBody TradingDesk tradingDesk) {

        tradingDeskService.save(tradingDesk);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Trading desk saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<TradingDesk>> findAll() {
        return ResponseEntity.ok(tradingDeskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradingDesk> findById(@PathVariable UUID id) {

        TradingDesk desk = tradingDeskService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trading desk not found: " + id));

        return ResponseEntity.ok(desk);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable UUID id,
            @Valid @RequestBody TradingDesk tradingDesk) {

        if (!tradingDeskService.existsById(id)) {
            throw new EntityNotFoundException(
                    "Trading desk not found: " + id);
        }

        tradingDesk.setId(id);
        tradingDeskService.save(tradingDesk);

        return ResponseEntity.ok("Trading desk updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        if (!tradingDeskService.existsById(id)) {
            throw new EntityNotFoundException(
                    "Trading desk not found: " + id);
        }

        tradingDeskService.deleteById(id);

        return ResponseEntity.ok("Trading desk deleted successfully");
    }
}
