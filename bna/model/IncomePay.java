package bna.model;

import bna.PfpException.*;

public class IncomePay extends BankOperationAmount implements IHasNamePay {

    public IncomePay(String name,
                     PaymentPeriod paymentPeriod,
                     Integer amount) throws PfpExceptionAmount {
        super(name, paymentPeriod, amount);
        if (amount < 0) {
            throw new PfpExceptionAmount("Денежные поступления не могут иметь отрицательное значение");
        }
    }

    @Override
    public int getPayment() {
        return getAmount();
    }
}
