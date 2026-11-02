package com.ytfa.fmrcap.service;

import com.ytfa.fmrcap.entity.Investor;
import com.ytfa.fmrcap.exception.ResourceConflictException;
import com.ytfa.fmrcap.repository.InvestorRepository;
import com.ytfa.fmrcap.repository.RiskAssessmentRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

    public InvestorService(
            InvestorRepository investorRepository,
            RiskAssessmentRepository riskAssessmentRepository) {

        this.investorRepository = investorRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    public Investor save(Investor investor) {

        if (investor.getInvestorCode() != null
                && investorRepository.existsByInvestorCode(
                        investor.getInvestorCode())) {
            throw new IllegalArgumentException(
                    "An investor with this investor code already exists");
        }

        return investorRepository.save(investor);
    }

    public List<Investor> findAll() {
        return investorRepository.findAll();
    }

    public Optional<Investor> findById(UUID id) {
        return investorRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return investorRepository.existsById(id);
    }

    public Investor update(UUID id, Investor investor) {

        if (investor.getInvestorCode() != null
                && investorRepository.existsByInvestorCodeAndIdNot(
                        investor.getInvestorCode(), id)) {
            throw new IllegalArgumentException(
                    "Another investor already uses this investor code");
        }

        investor.setId(id);
        return investorRepository.save(investor);
    }

    public void deleteById(UUID id) {

        if (!investorRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Investor not found: " + id);
        }

        if (riskAssessmentRepository.existsByInvestor_Id(id)) {

            throw new ResourceConflictException(
                    "Cannot delete this investor because "
                    + "existing risk assessments are linked to this investor. "
                    + "Remove or reassign those assessments first.");
        }

        investorRepository.deleteById(id);
    }
}