package bna.model;

import bna.PfpException.PfpExceptionAmount;

public class RatePay extends BankOperationAmount implements IHasNamePay{

    public RatePay(String name,
                   PaymentPeriod paymentPeriod,
                   Integer amount) throws PfpExceptionAmount {
        super(name, paymentPeriod, amount);
        if (amount > 0) {
            setAmount(-amount);
          //  throw new PfpExceptionAmount("Денежные траты не могут принимать положительное значение");
        }
    }

    @Override
    public int getPayment() {
        return getAmount();
    }
}
