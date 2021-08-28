package com.taiping.entity.operation;

import lombok.Data;

@Data
public class HealthyCard {

    private String cardId;

    private boolean healthy;

    private String actualPercentage;

    private String healthyPercentage;
}
