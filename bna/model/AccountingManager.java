package bna.model;

import java.util.Calendar;

public abstract class AccountingManager implements ITransactional, IXYDrawable {

    protected MoneyCollection moneyCollection;
    protected PaymentPeriod paymentPeriod;
    protected String name;

    AccountingManager(String name) {
        moneyCollection = new MoneyCollection();
        this.name = name;
    }

    public Calendar getStartDay() {
        return moneyCollection.getListMoneyChange().get(0).getDate();
    }

}
