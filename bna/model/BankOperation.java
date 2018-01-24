package bna.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Calendar;

public abstract class BankOperation implements Comparable {

    private StringProperty name;
    private PaymentPeriod paymentPeriod;
    private Calendar dateStart;
    private boolean isAmount;

    public BankOperation(String name,
                         PaymentPeriod paymentPeriod,
                         boolean isAmount) {
        this.name = new SimpleStringProperty(name);
        this.paymentPeriod = paymentPeriod;
        this.isAmount = isAmount;
    }

    public abstract double getPay();

    public boolean isAmount() {
        return isAmount;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public PaymentPeriod getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(PaymentPeriod paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public Calendar getDateStart() {
        return dateStart;
    }

    public void setDateStart(Calendar dateStart) {
        this.dateStart = dateStart;
    }

    @Override
    public int hashCode() {
        return name.get().hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof BankOperation) {
            BankOperation bankOp = (BankOperation) o;
            return paymentPeriod.compareTo(bankOp.paymentPeriod);
        } else {
            throw new ClassCastException("Can't cast to BankOperation!");
        }
    }
}
