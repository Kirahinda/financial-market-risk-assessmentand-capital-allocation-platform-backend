package com.ytfa.fmrcap.controller;

import com.ytfa.fmrcap.entity.RiskAnalyst;
import com.ytfa.fmrcap.service.RiskAnalystService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/risk-analysts")
public class RiskAnalystController {

    private final RiskAnalystService riskAnalystService;

    public RiskAnalystController(RiskAnalystService riskAnalystService) {
        this.riskAnalystService = riskAnalystService;
    }

    @PostMapping("/save")
    public ResponseEntity<RiskAnalyst> save(
            @Valid @RequestBody RiskAnalyst riskAnalyst) {

        RiskAnalyst savedAnalyst =
                riskAnalystService.save(riskAnalyst);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedAnalyst);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RiskAnalyst>> findAll() {

        return ResponseEntity.ok(
                riskAnalystService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskAnalyst> findById(
            @PathVariable UUID id) {

        RiskAnalyst analyst = riskAnalystService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Risk analyst not found: " + id));

        return ResponseEntity.ok(analyst);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RiskAnalyst> update(
            @PathVariable UUID id,
            @Valid @RequestBody RiskAnalyst riskAnalyst) {

        RiskAnalyst updatedAnalyst =
                riskAnalystService.update(id, riskAnalyst);

        return ResponseEntity.ok(updatedAnalyst);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable UUID id) {

        riskAnalystService.deleteById(id);

        return ResponseEntity.ok(
                "Risk analyst deleted successfully");
    }
}
