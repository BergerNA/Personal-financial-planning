package bna.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.Calendar;

public class Goal {

    private String name;
    private int endAmount;
    private int startAmount;
    private PaymentPeriod paymentPeriod;
    private int monthPayment;
    private int rentPayment;

    public Goal(String name, int endAmount, int startAmount, PaymentPeriod paymentPeriod, int rentPayment) {
        this.name = name;
        this.endAmount = endAmount;
        this.startAmount = startAmount;
        this.paymentPeriod = paymentPeriod;
        this.rentPayment = rentPayment;
    }

    public int getRentPayment() {
        return rentPayment;
    }

    public int rentPaymentProperty() {
        return rentPayment;
    }

    public void setRentPayment(int rentPayment) {
        this.rentPayment = rentPayment;
    }

    public String getName() {
        return name;
    }

    public String nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartAmount() {
        return startAmount;
    }

    public int startAmountProperty() {
        return startAmount;
    }

    public void setStartAmount(int startAmount) {
        this.startAmount = startAmount;
    }

    public int getMonthPayment() {
        return monthPayment;
    }

    public int monthPaymentProperty() {
        return monthPayment;
    }

    public void setMonthPayment(int monthPayment) {
        this.monthPayment = monthPayment;
    }

    public Calendar getStartDate() {
        return paymentPeriod.getStartDate();
    }

    public int getEndAmount() {
        return endAmount;
    }

    public int endAmountProperty() {
        return endAmount;
    }

    public void setEndAmount(int endAmount) {
        this.endAmount = endAmount;
    }

    public Calendar getEndDate() {
        return paymentPeriod.getEndDate();
    }

    public void setEndDate(Calendar endDate) {
        paymentPeriod.setEndDate(endDate);
    }

    public PaymentPeriod getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(PaymentPeriod paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }
}
