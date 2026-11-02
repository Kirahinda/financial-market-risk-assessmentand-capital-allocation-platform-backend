package com.ytfa.fmrcap.service;

import com.ytfa.fmrcap.entity.TradingDesk;
import com.ytfa.fmrcap.exception.ResourceConflictException;
import com.ytfa.fmrcap.repository.RiskAnalystRepository;
import com.ytfa.fmrcap.repository.TradingDeskRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TradingDeskService {

    private final TradingDeskRepository tradingDeskRepository;
    private final RiskAnalystRepository riskAnalystRepository;

    public TradingDeskService(
            TradingDeskRepository tradingDeskRepository,
            RiskAnalystRepository riskAnalystRepository) {

        this.tradingDeskRepository = tradingDeskRepository;
        this.riskAnalystRepository = riskAnalystRepository;
    }

    public TradingDesk save(TradingDesk tradingDesk) {
        return tradingDeskRepository.save(tradingDesk);
    }

    public List<TradingDesk> findAll() {
        return tradingDeskRepository.findAll();
    }

    public Optional<TradingDesk> findById(UUID id) {
        return tradingDeskRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return tradingDeskRepository.existsById(id);
    }

    public void deleteById(UUID id) {

        if (!tradingDeskRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Trading desk not found: " + id);
        }

        if (riskAnalystRepository.existsByTradingDesk_Id(id)) {

            throw new ResourceConflictException(
                    "Cannot delete this trading desk because "
                    + "risk analysts are currently assigned to it. "
                    + "Reassign those analysts to another trading desk first.");
        }

        tradingDeskRepository.deleteById(id);
    }
}