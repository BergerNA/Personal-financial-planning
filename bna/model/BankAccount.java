package bna.model;

import java.util.Calendar;

public class BankAccount {

    private String name;
    private PaymentPeriod percentPaymentPeriod;
    private Calendar dateStart;
    private double percentOfDividend = 0;
    private double percentOfCredit = 0;

    public BankAccount(String name, Calendar dateStart) {
        this.name = name;
        this.dateStart = dateStart;
    }

    public Calendar getDateStart() {
        return dateStart;
    }

    public void setPercentOfDeposit(double percent, PaymentPeriod paymentPeriod) {
        this.percentPaymentPeriod = paymentPeriod;
        this.percentOfDividend = percent;
    }

    public void setPercentOfCredit(double percent) {
        this.percentOfCredit = percent;
    }

    public double getPercentOfCredit() {
        return percentOfCredit;
    }

    public PaymentPeriod getPercentPaymentPeriod() {
        return percentPaymentPeriod;
    }

    public double getPercentOfDividend() {
        return percentOfDividend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
