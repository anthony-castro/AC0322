package com.acproject.AC0322.Objects;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Tool {

    CHNS("Chainsaw", "Stihl", new BigDecimal("1.49"), true, false, true),
    LADW("Ladder", "Werner", new BigDecimal("1.99"), true, true, false),
    JAKD("Jackhammer", "DeWalt", new BigDecimal("2.99"), true, false, false),
    JAKR("Jackhammer", "Ridgid", new BigDecimal("2.99"), true, false, false);

    private String toolType;
    private String brand;
    private BigDecimal dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

    Tool(String toolType, String brand, BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = dailyCharge.setScale(2, RoundingMode.HALF_UP);
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    public String getToolType() {
        return toolType;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public boolean isWeekendCharge() {
        return weekendCharge;
    }

    public boolean isHolidayCharge() {
        return holidayCharge;
    }
}
