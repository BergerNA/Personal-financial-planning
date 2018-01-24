package bna.model;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Double.NaN;

public class MoneyCollection {

    static class DateMoney implements Comparable {
        private double money;
        private double moneyChange;
        private boolean real;
        private Calendar date;

        Calendar getDate() {
            return date;
        }

        double getMoneyChange() {
            return moneyChange;
        }

        boolean isReal() {
            return real;
        }

        double getMoney() {
            return money;
        }

        void addMonthChange(double moneyChange, boolean real, Calendar date) {
            this.date = date;
            this.money += moneyChange;
            this.moneyChange += moneyChange;
            this.real = real;
        }

        DateMoney(double money, double moneyChange, boolean real, Calendar date) {
            this.money = money;
            this.moneyChange += moneyChange;
            this.real = real;
            this.date = date;
        }

        @Override
        public int compareTo(Object o) {
            DateMoney dm = (DateMoney) o;
            if (this.date.after(dm.date)) {
                return 1;
            } else if (dm.date.after(this.date)) {
                return -1;
            }
            return 0;
        }
    }

    private static final int BAD_INDEX = -1;
    private int lastRealIndex = BAD_INDEX;
    private LinkedList<DateMoney> listMoneyChange = new LinkedList<>();

    LinkedList<DateMoney> getListMoneyChange() {
        return listMoneyChange;
    }

    double getLastCash() {
        return listMoneyChange.getLast().getMoney();
    }

    double getCash(boolean real) {
        if (real) {
            if (lastRealIndex == BAD_INDEX) {
                return NaN;
            }
            return listMoneyChange.get(lastRealIndex).getMoney();
        }
        return listMoneyChange.getLast().getMoney();
    }

    void addTransaction(double money, boolean real, Calendar calendar) {
        double moneyChange = money;
        if (real) {
            clearToReal();
        }
        if (listMoneyChange.size() > 0) {
            if (listMoneyChange.getLast().getDate().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                listMoneyChange.getLast().addMonthChange(moneyChange, real, (Calendar) calendar.clone());
            } else {
                money += listMoneyChange.getLast().getMoney();
                listMoneyChange.add(new DateMoney(money, moneyChange, real, (Calendar) calendar.clone()));
            }
        } else {
            listMoneyChange.add(new DateMoney(money, moneyChange, real, (Calendar) calendar.clone()));
        }
        if (real) {
            lastRealIndex++;
        }
    }

    @Deprecated
    protected void addDayTransaction(double money, boolean real, Calendar calendar) {
        double moneyChange = money;
        if (real) {
            clearToReal();
        }
        if (listMoneyChange.size() > 0) {
            money += listMoneyChange.getLast().getMoney();
        }
        listMoneyChange.add(new DateMoney(money, moneyChange, real, (Calendar) calendar.clone()));
        if (real) {
            lastRealIndex++;
        }
    }

    void clear() {
        if (listMoneyChange.size() != 0) {
            DateMoney firstTransaction = listMoneyChange.getFirst();
            listMoneyChange.clear();
            listMoneyChange.add(firstTransaction);
        }
        lastRealIndex = BAD_INDEX;

    }

    private void clearToReal() {
        if (listMoneyChange.size() != 0) {
            listMoneyChange.removeIf((transaction) -> !transaction.isReal());
        }
    }

}
