package com.ytfa.fmrcap.controller;

import com.ytfa.fmrcap.entity.CapitalAllocation;
import com.ytfa.fmrcap.service.CapitalAllocationService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/capital-allocations")
public class CapitalAllocationController {

    private final CapitalAllocationService capitalAllocationService;

    public CapitalAllocationController(
            CapitalAllocationService capitalAllocationService) {
        this.capitalAllocationService = capitalAllocationService;
    }

    @PostMapping("/save")
    public ResponseEntity<CapitalAllocation> save(
            @Valid @RequestBody CapitalAllocation allocation) {

        CapitalAllocation savedAllocation =
                capitalAllocationService.save(allocation);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedAllocation);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CapitalAllocation>> findAll() {
        return ResponseEntity.ok(capitalAllocationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapitalAllocation> findById(
            @PathVariable UUID id) {

        CapitalAllocation allocation =
                capitalAllocationService.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "The capital allocation with that ID does not exist"));

        return ResponseEntity.ok(allocation);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CapitalAllocation> update(
            @PathVariable UUID id,
            @Valid @RequestBody CapitalAllocation allocation) {

        if (!capitalAllocationService.existsById(id)) {
            throw new EntityNotFoundException(
                    "The capital allocation with that ID does not exist");
        }

        CapitalAllocation updatedAllocation =
                capitalAllocationService.update(id, allocation);

        return ResponseEntity.ok(updatedAllocation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        if (!capitalAllocationService.existsById(id)) {
            throw new EntityNotFoundException(
                    "The capital allocation with that ID does not exist");
        }

        capitalAllocationService.deleteById(id);

        return ResponseEntity.ok(
                "Capital allocation deleted successfully");
    }
}
