package com.ytfa.fmrcap.service;

import com.ytfa.fmrcap.entity.AssetClass;
import com.ytfa.fmrcap.exception.ResourceConflictException;
import com.ytfa.fmrcap.repository.AssetClassRepository;
import com.ytfa.fmrcap.repository.RiskAnalystRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssetClassService {

    private final AssetClassRepository assetClassRepository;
    private final RiskAnalystRepository riskAnalystRepository;

    public AssetClassService(
            AssetClassRepository assetClassRepository,
            RiskAnalystRepository riskAnalystRepository) {

        this.assetClassRepository = assetClassRepository;
        this.riskAnalystRepository = riskAnalystRepository;
    }

    @Transactional
    public AssetClass save(AssetClass assetClass) {
        return assetClassRepository.save(assetClass);
    }

    public List<AssetClass> findAll() {
        return assetClassRepository.findAll();
    }

    public Optional<AssetClass> findById(UUID id) {
        return assetClassRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return assetClassRepository.existsById(id);
    }

    @Transactional
    public AssetClass update(UUID id, AssetClass assetClass) {

        if (!assetClassRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Asset class not found: " + id);
        }

        assetClass.setId(id);

        return assetClassRepository.save(assetClass);
    }

    @Transactional
    public void deleteById(UUID id) {

        if (!assetClassRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Asset class not found: " + id);
        }

        if (riskAnalystRepository.existsByAssetClasses_Id(id)) {

            throw new ResourceConflictException(
                    "Cannot delete this asset class because "
                    + "one or more risk analysts are assigned to it. "
                    + "Remove this asset class from their assignments first.");
        }

        assetClassRepository.deleteById(id);
    }
}
