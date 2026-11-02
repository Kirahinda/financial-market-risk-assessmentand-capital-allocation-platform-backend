package com.ytfa.fmrcap.repository;

import com.ytfa.fmrcap.entity.RiskAnalyst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RiskAnalystRepository
        extends JpaRepository<RiskAnalyst, UUID> {

    boolean existsByTradingDesk_Id(UUID tradingDeskId);

    boolean existsByAssetClasses_Id(UUID assetClassId);
}