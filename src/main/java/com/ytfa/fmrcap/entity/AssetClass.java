package com.ytfa.fmrcap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "asset_class")
public class AssetClass {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Asset class name is required")
    private String name;

    @ManyToMany(mappedBy = "assetClasses")
    @JsonIgnore
    private List<RiskAnalyst> riskAnalysts;

    public AssetClass() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RiskAnalyst> getRiskAnalysts() {
        return riskAnalysts;
    }

    public void setRiskAnalysts(List<RiskAnalyst> riskAnalysts) {
        this.riskAnalysts = riskAnalysts;
    }
}
