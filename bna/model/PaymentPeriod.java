package bna.model;

import java.util.Calendar;

public class PaymentPeriod implements Comparable {

    private Period period;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar nextDatePay;

    public Period getPeriod() {
        return period;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public PaymentPeriod(Period period, Calendar startDate, Calendar endDate) {
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
        nextDatePay = (Calendar) startDate.clone();
    }

    @Override
    public int compareTo(Object o) {
        PaymentPeriod pp2 = (PaymentPeriod) o;
        if (this.nextDatePay.after(pp2.nextDatePay)) {
            return 1;
        } else if (pp2.nextDatePay.after(this.nextDatePay)) {
            return -1;
        }
        return 1;
    }

    Calendar getNextDatePay() {
        return nextDatePay;
    }

    Calendar setNextDatePay() {
        if (nextDatePay != null) {
            nextDatePay.add(period.getTimePeriod(), period.getValue());
            if (nextDatePay.after(endDate)) {
                nextDatePay = null;
            }
        }
        return nextDatePay;
    }

    void setEndDate(int contPay) {
        Calendar start = (Calendar) startDate.clone();
        for (int i = 0; i < contPay; i++) {
            start.add(period.getTimePeriod(), period.getValue());
        }
        endDate = start;
    }

    void setEndDate(Calendar endDate) {
        this.endDate = (Calendar) endDate.clone();
    }

    Calendar resetNextDatePay() {
        return nextDatePay = (Calendar) startDate.clone();
    }
}
