package bna.model;

import java.util.Calendar;

public class Period {
    public enum TimePeriod {
        DAY(Calendar.DAY_OF_WEEK, "День"),
        WEEK(Calendar.WEEK_OF_YEAR, "Неделя"),
        MONTH(Calendar.MONTH, "Месяц"),
        YEAR(Calendar.YEAR, "Год");

        private int field;
        private String name;
        TimePeriod(int field, String period){
            this.field = field;
            name = period;
        }
    }

    private final static long DAY_MILLISECOND = 24 * 60 * 60 * 1000;
    private final static long WEEK_MILLISECOND = 7 * 24 * 60 * 60 * 1000;
    private final static long FIRST_PAY_COUNT = 1;
    private final static int MONTHS_IN_YEAR = 12;
    private final static int DAYS_IN_YEAR = 365;
    private static final int DAYS_IN_WEEK = 7;
    private TimePeriod timePeriod;
    private int value;

    public Period(TimePeriod timePeriod, int value) {
        this.timePeriod = timePeriod;
        this.value = value == 0 ? 1 : value;
    }

    public String getName(){
        return timePeriod.name;
    }
    int getTimePeriod() {
        return timePeriod.field;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value == 0 ? 1 : value;
    }

    long countAllPayment(PaymentPeriod paymentPeriod)
    {
        return countAllPayment(paymentPeriod.getStartDate(), paymentPeriod.getEndDate());
    }

    private long countAllPayment(Calendar start, Calendar end) {
        long millisecond = end.getTimeInMillis() - start.getTimeInMillis();
        long result;
        switch (timePeriod) {
            case DAY:
                result = millisecond / (DAY_MILLISECOND * value);
                break;
            case WEEK:
                result = millisecond / (WEEK_MILLISECOND * value);
                break;
            case MONTH:
                if (end.get(Calendar.YEAR) == start.get(Calendar.YEAR)) {
                    result = (end.get(Calendar.MONTH) - start.get(Calendar.MONTH)) / value;
                } else {
                    result = MONTHS_IN_YEAR - start.get(Calendar.MONTH) + end.get(Calendar.MONTH);
                    result += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR) - 1) * MONTHS_IN_YEAR;
                    result /= value;
                }
                break;
            case YEAR:
                result = ((end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) / value);
                break;
            default:
                throw new IllegalArgumentException("Unknown timePeriod switch!");
        }
        return result + FIRST_PAY_COUNT;
    }

    int countYearPayment() {
        switch (timePeriod) {
            case DAY:
                return DAYS_IN_YEAR / value;
            case WEEK:
                return DAYS_IN_YEAR / (DAYS_IN_WEEK * value);
            case MONTH:
                return MONTHS_IN_YEAR / value;
            case YEAR:
                return 1;
            default:
                throw new IllegalArgumentException("Unknown timePeriod switch!");
        }
    }
}
