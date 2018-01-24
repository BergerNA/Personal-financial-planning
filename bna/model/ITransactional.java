package bna.model;

import java.util.Calendar;

public interface ITransactional {
    void addTransaction(double money, boolean real, Calendar date);
}
