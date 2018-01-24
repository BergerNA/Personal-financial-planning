package bna.model;

import javafx.scene.chart.XYChart;

import java.util.Calendar;

public class GoalManager extends AccountingManager implements IHasNamePay {

    public class FinancialInstrument {

        long monthCount;
        int endPayment;

        void setMonthCount(int monthCount) {
            this.monthCount = monthCount;
        }

        void setMonthCount(double monthCount) {
            this.monthCount = Math.round(monthCount);
        }

        public long getMonthCount() {
            return monthCount;
        }

        void setEndPayment(double endPayment) {
            this.endPayment = (int) endPayment;
        }

        public int getEndPayment() {
            return endPayment;
        }
    }

    private final static int MONTH_IN_100_YEARS = 12 * 100;
    private Goal goal;
    private BankManager bankManager;
    private BankOperationAmount bankOperation = null;
    private FinancialInstrument credit = new FinancialInstrument();
    private FinancialInstrument investment = new FinancialInstrument();

    public FinancialInstrument getCredit() {
        return credit;
    }

    public FinancialInstrument getInvestment() {
        return investment;
    }

    @Override
    public String getName() {
        return goal.getName();
    }

    @Override
    public int getPayment() {
        return goal.getMonthPayment();
    }

    public double getEndAmount() {
        return goal.getEndAmount();
    }

    public double getAmount() {
        if (goal.getPaymentPeriod().getStartDate().after(Calendar.getInstance())) {
            return goal.getStartAmount();
        }
        return moneyCollection.getCash(true);
    }

    public Calendar getStartDate() {
        return goal.getStartDate();
    }

    public Calendar getEndDate() {
        return goal.getEndDate();
    }

    public GoalManager(Goal goal, BankManager bankManager) {
        super(goal.getName());
        this.bankManager = bankManager;
        this.goal = goal;
        paymentPeriod = goal.getPaymentPeriod();
        goal.setMonthPayment((int) bankManager.getMinimumMonthCash(paymentPeriod.getStartDate(), paymentPeriod.getEndDate()));
        if (goal.getMonthPayment() == 0) {
            goal.setMonthPayment(10000);
        }
        investmentCalculate();
        creditCalculate();
        if (investment.getMonthCount() > paymentPeriod.getPeriod().countAllPayment(paymentPeriod)) {
            paymentPeriod.setEndDate((int) investment.getMonthCount());
        }
        calculate();
    }

    void subscribe(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    Goal getGoal() {
        return goal;
    }

    public FinancialInstrument investmentCalculate() {
        double deposit = 1 + bankManager.getPercentOfInvestment() / MONTH_IN_100_YEARS;
        //   double credit = 1 - bankManager.getPercentOfCredit() / MONTH_IN_100_YEARS;
        double inflation = 1 + bankManager.getPercentOfInflation() / MONTH_IN_100_YEARS;
        double depositInflation = deposit / inflation;
        double result;
        for (int x = 0; x < 1200; x++) {
            double startCapital = goal.getStartAmount() * Math.pow(deposit, x);
            double paymentCash = goal.getMonthPayment() * (Math.pow(deposit, x) - 1) / (deposit - 1);
            double rentCash;
            if (depositInflation == 1) {
                rentCash = goal.getRentPayment() * Math.pow(inflation, x) * x;
            } else {
                rentCash = goal.getRentPayment() * Math.pow(inflation, x) * ((Math.pow(depositInflation, x) - 1) / (depositInflation - 1));
            }
            result = startCapital + paymentCash - rentCash;
            double price = goal.getEndAmount() * Math.pow(inflation, x);
            if (price <= result) {
                this.investment.setMonthCount(x);
                this.investment.setEndPayment(x * goal.getMonthPayment() + price - result);
                return investment;
            }
        }
        this.investment.setMonthCount(MONTH_IN_100_YEARS);
        return investment;
    }

    public FinancialInstrument creditCalculate() {
        double creditMonth = 1 - bankManager.getPercentOfCredit() / MONTH_IN_100_YEARS;
        double missingCash = (goal.getEndAmount() - goal.getStartAmount()) / (double) goal.getMonthPayment();
        double creditPay = missingCash * (creditMonth - 1);
        double result = Math.log(creditPay + 1) / Math.log(creditMonth);
        this.credit.setMonthCount(result);
        this.credit.setEndPayment(result * goal.getMonthPayment());
        return credit;
    }

    @Override
    public void addTransaction(double money, boolean real, Calendar date) {
        System.out.print("money = " + money);
        moneyCollection.addTransaction(money, real, date);
        System.out.println(", capital = " + moneyCollection.getLastCash());
    }

    void calculate() {
        paymentPeriod.resetNextDatePay();
        moneyCollection.clear();
        while (paymentPeriod.getNextDatePay().before(paymentPeriod.getEndDate())) {
            boolean isReal = false;
            if (paymentPeriod.getNextDatePay().after(Calendar.getInstance())) {
                isReal = true;
            }
            addTransaction(goal.getMonthPayment(), isReal, paymentPeriod.getNextDatePay());
            paymentPeriod.setNextDatePay();
        }
    }

    @Override
    public XYChart.Series getSeries() {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        moneyCollection.getListMoneyChange().forEach((v) -> series.getData().add(new XYChart.Data(IXYDrawable.getDateFormat(v.getDate()), v.getMoney())));
        return series;
    }
}
