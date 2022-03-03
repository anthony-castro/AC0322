package com.acproject.AC0322.Objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {

    private CheckoutInfo checkoutInfo;
    private LocalDate dueDate;
    private long chargeDays;
    private Tool rentedTool;

    public CheckoutInfo getCheckoutInfo() {
        return checkoutInfo;
    }

    public void setCheckoutInfo(CheckoutInfo checkoutInfo) {
        this.checkoutInfo = checkoutInfo;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public long getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(long chargeDays) {
        this.chargeDays = chargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return rentedTool.getDailyCharge()
                .multiply(BigDecimal.valueOf(chargeDays))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Tool getRentedTool() {
        return rentedTool;
    }

    public void setRentedTool(Tool rentedTool) {
        this.rentedTool = rentedTool;
    }

    public BigDecimal getDiscountAmount() {
        return getPreDiscountCharge().multiply(BigDecimal.valueOf(checkoutInfo.getDiscountPercent()))
                .divide(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFinalCharge() {
        return getPreDiscountCharge().subtract(getDiscountAmount()).setScale(2, RoundingMode.HALF_UP);
    }

    public String getConsoleMessage() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");

        return "Tool code: " + rentedTool.name() +
                "\nTool type: " + rentedTool.getToolType() +
                "\nRental Days: " + checkoutInfo.getRentalDays() +
                "\nCheck out date: " + dtf.format(getCheckoutInfo().getCheckOutDate())+
                "\nDue date: " + dtf.format(dueDate) +
                "\nDaily rental charge: $" + rentedTool.getDailyCharge() +
                "\nCharge days: " + chargeDays +
                "\nPre-discount charge: $" + getPreDiscountCharge() +
                "\nDiscount amount: $" + getDiscountAmount() +
                "\nFinal charge: " + getFinalCharge();
    }

}
