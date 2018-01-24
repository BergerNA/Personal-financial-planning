package bna.model;

import java.util.Calendar;

public class PensionGoal extends Goal {

    private Person person;

    public PensionGoal(Person person, int endAmount, int startAmount, PaymentPeriod paymentPeriod, int rentPayment) {
        super("Пенсия", endAmount, startAmount, paymentPeriod, rentPayment);
        this.person = person;
    }

    @Override
    public void setEndDate(Calendar endDate) {
        super.setEndDate(person.getDatePensionExit());
    }
}
