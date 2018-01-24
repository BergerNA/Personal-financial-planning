package bna.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BankOperationAmount extends BankOperation {

    private IntegerProperty amount;

    public BankOperationAmount(String name, PaymentPeriod paymentPeriod, Integer amount) {
        super(name, paymentPeriod, true);
        this.amount = new SimpleIntegerProperty(amount);
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    @Override
    public double getPay() {
        return amount.get();
    }
}
