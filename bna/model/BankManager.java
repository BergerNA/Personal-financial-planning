package bna.model;

import bna.PfpException.PfpExceptionAmount;
import javafx.scene.chart.XYChart;

import java.util.*;

public class BankManager extends AccountingManager {

    private BankAccount bankAccount;
    private double percentOfInflation;
    private TreeSet<BankOperation> listBankOperation = new TreeSet<>();
    private ArrayList<IHasNamePay> listIncomePay = new ArrayList<>();
    private ArrayList<IHasNamePay> listRatePay = new ArrayList<>();
    private ArrayList<IHasNamePay> listGoalManager = new ArrayList<>();

    public BankManager(BankAccount bankAccount) {
        super(bankAccount.getName());
        this.bankAccount = bankAccount;
        moneyCollection.addTransaction(0, true, bankAccount.getDateStart());
    }

    public void addRepetitiveOperation(BankOperation bankOperation) {
        if(bankOperation instanceof IncomePay){
            listIncomePay.add((IHasNamePay) bankOperation);
        } else if(bankOperation instanceof RatePay){
            listRatePay.add((IHasNamePay) bankOperation);
        }
        listBankOperation.add(bankOperation);
        calculate();
    }

    public ArrayList<IHasNamePay> getIncomePayList(){
        return listIncomePay;
    }

    public ArrayList<IHasNamePay> getRatePayList(){
        return listRatePay;
    }

    public ArrayList<IHasNamePay> getListGoalManager(){
        return listGoalManager;
    }

    public void addGoalManager(GoalManager goalManager){
        goalManager.subscribe(this);
        listGoalManager.add(goalManager);
        calculate();
    }

    public void calculate() {
        moneyCollection.clear();
        TreeSet<BankOperation> sortEvent = new TreeSet<>(new Comparator<BankOperation>() {
            @Override
            public int compare(BankOperation o1, BankOperation o2) {
                if(o2.getPaymentPeriod().getNextDatePay().after(o1.getPaymentPeriod().getNextDatePay())) {
                    return -1;
                }
                return 1;
            }
        });
        listGoalManager.forEach(goalManager->{
            try {
                Calendar end = ((GoalManager)goalManager).getGoal().getPaymentPeriod().getEndDate();
                end.add(Calendar.DAY_OF_MONTH,-1);
                BankOperation goalOperation = new RatePay(goalManager.getName(),
                        new PaymentPeriod(new Period(Period.TimePeriod.MONTH,1),
                                ((GoalManager)goalManager).getGoal().getPaymentPeriod().getStartDate(),
                                end),
                        goalManager.getPayment());
                sortEvent.add(goalOperation);
            } catch (PfpExceptionAmount pfpExceptionAmount) {
                pfpExceptionAmount.printStackTrace();
            }
        });
        sortEvent.addAll(listBankOperation);
        Calendar toDay = Calendar.getInstance();
        while (sortEvent.size() > 0) {
            BankOperation bankOperation = sortEvent.pollFirst();
            boolean isRealDate = false;
            if (bankOperation.getPaymentPeriod().getNextDatePay().after(toDay)) {
                isRealDate = true;
            }
            if(bankOperation.isAmount()){
                moneyCollection.addTransaction(bankOperation.getPay(), isRealDate, bankOperation.getPaymentPeriod().getNextDatePay());
            }else {
                double percentSum = moneyCollection.getLastCash()*bankOperation.getPay();
                moneyCollection.addTransaction(percentSum, isRealDate, bankOperation.getPaymentPeriod().getNextDatePay());
            }
           if (bankOperation.getPaymentPeriod().setNextDatePay() != null) {
                    sortEvent.add(bankOperation);
            }
        }
        listBankOperation.forEach(bankOperation -> bankOperation.getPaymentPeriod().resetNextDatePay());
    }

    public double getMinimumMonthCash(Calendar startDate, Calendar endDate){
        double result = 0;
        Iterator iterator = moneyCollection.getListMoneyChange().iterator();
        if(iterator.hasNext()){
            result = ((MoneyCollection.DateMoney)iterator.next()).getMoneyChange();
        }
        while (iterator.hasNext()){
            MoneyCollection.DateMoney dateMoney = (MoneyCollection.DateMoney) iterator.next();
            if(dateMoney.getDate().before(startDate)){
                continue;
            }else if(dateMoney.getDate().before(endDate)){
                result = dateMoney.getMoneyChange();
            }
        }
        return result;
    }

    public double getPercentOfInvestment(){
        return bankAccount.getPercentOfDividend();
    }

    public double getPercentOfCredit(){
        return bankAccount.getPercentOfCredit();
    }

    public double getPercentOfInflation(){
        return percentOfInflation;
    }

    public void setPercentOfInflation(double percent){
        percentOfInflation = percent;
    }


    @Override
    public void addTransaction(double money, boolean real, Calendar date) {
        moneyCollection.addTransaction(money, real, date);
    }

    @Override
    public XYChart.Series getSeries() {
        XYChart.Series series = new XYChart.Series();
        series.setName(bankAccount.getName());
        moneyCollection.getListMoneyChange().forEach((v) -> series.getData().add(new XYChart.Data(IXYDrawable.getDateFormat(v.getDate()), v.getMoney())));
        return series;
    }
}
