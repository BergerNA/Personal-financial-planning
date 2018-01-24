package bna.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class BankOperationPercent extends BankOperation {

    private DoubleProperty percentOfAccount;
    private double percentOfPeriod;

    public BankOperationPercent(String name, PaymentPeriod paymentPeriod, double percentOfYear) {
        super(name, paymentPeriod, false);
        percentOfAccount = new SimpleDoubleProperty(percentOfYear);
        percentOfPeriod = percentOfYear / paymentPeriod.getPeriod().countYearPayment();
    }

    public double getPercentOfAccount() {
        return percentOfAccount.get();
    }

    public void setPercentOfAccount(double percentOfAccount) {
        this.percentOfAccount.set(percentOfAccount);
    }

    public DoubleProperty percentOfAccountProperty() {
        return percentOfAccount;
    }

    @Override
    public double getPay() {
        return percentOfPeriod;
    }
}
