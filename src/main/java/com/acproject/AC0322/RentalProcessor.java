package com.acproject.AC0322;

import com.acproject.AC0322.Objects.CheckoutInfo;
import com.acproject.AC0322.Objects.RentalAgreement;
import com.acproject.AC0322.Objects.Tool;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class RentalProcessor {

    public static final int INDEPENDENCE_DAY = 4;

    public static String checkForInvalidCheckoutInfo(CheckoutInfo checkoutInfo) {
        StringBuilder errorsFound = new StringBuilder();
        //No upper bound limit on tool rental, just need 1 or greater
        if (checkoutInfo.getRentalDays() < 1) {
            errorsFound.append("Error: Tool must be rented for at least 1 day.\n");
        }

        if (checkoutInfo.getDiscountPercent() < 0 || checkoutInfo.getDiscountPercent() > 100) {
            errorsFound.append("Error: Discount percentage must be in the range of 0-100%.\n");
        }

        if (Arrays.stream(Tool.values()).map(tool -> tool.name()).noneMatch(toolName -> toolName.equalsIgnoreCase(checkoutInfo.getToolCode()))) {
            errorsFound.append("Error: Tool code \"" + checkoutInfo.getToolCode() + "\" not recognized, please enter a valid tool code.");
        }

        return errorsFound.toString();
    }

    public static RentalAgreement processRentalOrder(CheckoutInfo checkoutInfo) {
        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setCheckoutInfo(checkoutInfo);

        Tool rentedTool = Tool.valueOf(checkoutInfo.getToolCode());

        rentalAgreement.setRentedTool(rentedTool);
        LocalDate dueDate = checkoutInfo.getCheckOutDate().plusDays(checkoutInfo.getRentalDays());

        rentalAgreement.setDueDate(dueDate);
        rentalAgreement.setChargeDays(chargeDays(rentedTool, checkoutInfo.getCheckOutDate(), dueDate));

        return rentalAgreement;
    }

    private static long chargeDays(Tool rentedTool, LocalDate checkoutDate, LocalDate dueDate) {
        //Here we have a total off all days, no holidays or weekends accounted for
        long chargeDays = ChronoUnit.DAYS.between(checkoutDate, dueDate);

        //Take off for weekdays if applicable
        if (!rentedTool.isWeekdayCharge()) {
            chargeDays -= checkoutDate.datesUntil(dueDate)
                    .filter(day -> !isWeekend(day.getDayOfWeek()))
                    .count();
        }
        //Take off for weekends if applicable
        if (!rentedTool.isWeekendCharge()) {
            chargeDays -= checkoutDate.datesUntil(dueDate)
                    .filter(day -> isWeekend(day.getDayOfWeek()))
                    .count();
        }
        //Based on current holiday system, all holidays are weekdays
        //To avoid double removal, confirm we would charge for weekdays
        if (!rentedTool.isHolidayCharge() && rentedTool.isWeekdayCharge()) {
            chargeDays -= numOfIndependenceDays(0, checkoutDate, dueDate);
            chargeDays -= numOfLaborDays(0, checkoutDate, dueDate);
        }

        //Now subtract for holidays as needed
        return chargeDays;
    }

    private static long numOfIndependenceDays(long daysToRemove, LocalDate checkoutDate, LocalDate dueDate) {
        //First, find next occurring 4th of July
        LocalDate independenceDay = LocalDate.of(checkoutDate.getYear(), Month.JULY, INDEPENDENCE_DAY);;
        if (checkoutDate.isAfter(independenceDay)) {
            //Already past the 4th for this year, increment year
            independenceDay = LocalDate.of(checkoutDate.getYear() + 1, Month.JULY, INDEPENDENCE_DAY);
        }

        //If we have a Saturday, it is a day earlier
        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDay = independenceDay.minusDays(1);
        //If we have a Sunday, it is a day later
        } else if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDay = independenceDay.plusDays(1);
        }
        //Inclusive check to see if we are renting on the holiday
        long checkoutDayCompare = independenceDay.compareTo(checkoutDate);
        long dueDateComapre = independenceDay.compareTo(dueDate);
         if (checkoutDayCompare >= 0
                 && dueDateComapre <= 0) {
             daysToRemove++;
         }

         //If we have a rental spanning greater than a year, we're not done yet!
        if (ChronoUnit.DAYS.between(checkoutDate, dueDate) > 365) {
            return numOfIndependenceDays(daysToRemove, checkoutDate.plusYears(1), dueDate);
        }
        return daysToRemove;
    }

    private static long numOfLaborDays(long daysToRemove, LocalDate checkoutDate, LocalDate dueDate) {
        LocalDate laborDay;

        //First, find next occurring LaborDay
        //start at Sep 1st, and move forward
        int day = 1;
        do {
            laborDay = LocalDate.of(checkoutDate.getYear(), Month.SEPTEMBER, day++);
        } while (laborDay.getDayOfWeek() != DayOfWeek.MONDAY);

        if (laborDay.isBefore(checkoutDate)) {
            //If labor day of the checkout year occurs after checkout, bump up to next year's labor day
            //Will cover case such as a rental from Dec 1, 2021-Oct 1, 2022
            day = 1;
            do {
                laborDay = LocalDate.of(checkoutDate.getYear() + 1, Month.SEPTEMBER, day++);
            } while (laborDay.getDayOfWeek() != DayOfWeek.MONDAY);
        }

        //Inclusive check to see if we are renting on the holiday
        if (laborDay.compareTo(checkoutDate) >= 0
                && laborDay.compareTo(dueDate) <= 0) {
            daysToRemove++;
        }

        //If we have a rental spanning greater than a year, we're not done yet!
        if (ChronoUnit.DAYS.between(checkoutDate, dueDate) > 365) {
            return numOfLaborDays(daysToRemove, checkoutDate.plusYears(1), dueDate);
        }
        return daysToRemove;
    }

    private static boolean isWeekend(DayOfWeek dayOfWeek) {
        //Day of week value for Mon-Fri is 1-5, for Sat-Sun 6-7
        return dayOfWeek.getValue() == 6 || dayOfWeek.getValue() == 7;
    }
}
