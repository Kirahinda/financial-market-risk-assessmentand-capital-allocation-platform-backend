package com.ytfa.fmrcap.controller;

import com.ytfa.fmrcap.entity.RiskAssessment;
import com.ytfa.fmrcap.service.RiskAssessmentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/risk-assessments")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(
            RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @PostMapping("/save")
    public ResponseEntity<RiskAssessment> save(
            @Valid @RequestBody RiskAssessment assessment) {

        RiskAssessment savedAssessment =
                riskAssessmentService.save(assessment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedAssessment);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RiskAssessment>> findAll() {
        return ResponseEntity.ok(riskAssessmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskAssessment> findById(
            @PathVariable UUID id) {

        RiskAssessment assessment = riskAssessmentService.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Risk assessment not found: " + id));

        return ResponseEntity.ok(assessment);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RiskAssessment> update(
            @PathVariable UUID id,
            @Valid @RequestBody RiskAssessment assessment) {

        RiskAssessment updatedAssessment =
                riskAssessmentService.update(id, assessment);

        return ResponseEntity.ok(updatedAssessment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        riskAssessmentService.deleteById(id);

        return ResponseEntity.ok(
                "Risk assessment deleted successfully");
    }
}
