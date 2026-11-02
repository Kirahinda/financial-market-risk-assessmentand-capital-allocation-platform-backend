package com.ytfa.fmrcap.controller;

import com.ytfa.fmrcap.entity.AssetClass;
import com.ytfa.fmrcap.service.AssetClassService;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/asset-classes")
public class AssetClassController {

    private final AssetClassService assetClassService;

    public AssetClassController(AssetClassService assetClassService) {
        this.assetClassService = assetClassService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
            @Valid @RequestBody AssetClass assetClass) {

        assetClassService.save(assetClass);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Asset class saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<AssetClass>> findAll() {
        return ResponseEntity.ok(assetClassService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetClass> findById(
            @PathVariable UUID id) {

        AssetClass assetClass = assetClassService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "The asset class with that id does not exist"));

        return ResponseEntity.ok(assetClass);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable UUID id,
            @Valid @RequestBody AssetClass assetClass) {

        if (!assetClassService.existsById(id)) {
            throw new EntityNotFoundException(
                    "The asset class with that id does not exist");
        }

        assetClass.setId(id);
        assetClassService.save(assetClass);

        return ResponseEntity.ok("Asset class updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable UUID id) {

        if (!assetClassService.existsById(id)) {
            throw new EntityNotFoundException(
                    "The asset class with that id does not exist");
        }

        assetClassService.deleteById(id);

        return ResponseEntity.ok("Asset class deleted successfully");
    }
}
