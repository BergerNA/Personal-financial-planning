package bna.model;

import javafx.scene.chart.XYChart;

import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

@Deprecated
public class PfpManager {

    private final BankAccount bankAccount;
    private final TreeSet<AccountingManager> listAccounting = new TreeSet<>();

    public PfpManager(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void addAccountingManager(AccountingManager accountingManager) {
        listAccounting.add(accountingManager);
    }

    public LinkedList<XYChart.Series> getSeries() {
        TreeSet<AccountingManager> sortList = new TreeSet<>(new Comparator<AccountingManager>() {
            @Override
            public int compare(AccountingManager o1, AccountingManager o2) {
                if (o1.getStartDay().after(o2.getStartDay())) {
                    return 1;
                }
                return -1;
            }
        });
        sortList.addAll(listAccounting);
        LinkedList<XYChart.Series> sortListSeries = new LinkedList<>();
        sortList.forEach(accountingManager -> sortListSeries.add(accountingManager.getSeries()));
        return sortListSeries;
    }

    public void calculate() {
        // listAccounting.forEach(accountingManager -> accountingManager.);
    }
}
