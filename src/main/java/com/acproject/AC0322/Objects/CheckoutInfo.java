package com.acproject.AC0322.Objects;

import java.time.LocalDate;

public class CheckoutInfo {
    private String toolCode;
    private int rentalDays;
    private int discountPercent;
    private LocalDate checkOutDate;

    public CheckoutInfo() {
    }

    public CheckoutInfo(String toolCode, int rentalDays, int discountPercent, LocalDate checkOutDate) {
        this.toolCode = toolCode;
        this.rentalDays = rentalDays;
        this.discountPercent = discountPercent;
        this.checkOutDate = checkOutDate;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
