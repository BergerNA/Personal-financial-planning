package bna;

import bna.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import bna.PfpException.*;

import java.io.IOException;
import java.util.Calendar;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<AccountingManager> observableChart = FXCollections.observableArrayList();
    BankOperationAmount salaryOperation;
    BankOperationPercent dividendOperation;
    BankManager bankManager;

    public MainApp() throws PfpExceptionAmount {
        Calendar daySalary = Calendar.getInstance();
        daySalary.set(daySalary.get(Calendar.YEAR), 1, 20);
        Calendar dayEat = Calendar.getInstance();
        dayEat.set(dayEat.get(Calendar.YEAR) + 1, 3, 25);

        Calendar birthDate = Calendar.getInstance();
        birthDate.set(1986, 13, 25);
        Person person = new Person("Nikolay", birthDate, 33);//, incomeList);

        BankAccount bankAccount = new BankAccount("Банковский счет", Calendar.getInstance());
        bankAccount.setPercentOfCredit(12);
        bankAccount.setPercentOfDeposit(7, new PaymentPeriod(new Period(Period.TimePeriod.MONTH, 1),
                Calendar.getInstance(), person.getDatePensionExit()));

        salaryOperation = new IncomePay("Зарплата",
                new PaymentPeriod(new Period(Period.TimePeriod.MONTH, 1),
                        daySalary,
                        person.getDatePensionExit()),
                60000);

        Calendar daySalary13 = Calendar.getInstance();
        daySalary.set(daySalary13.get(Calendar.YEAR), 12, 25);
        BankOperation salaryOperation13 = new IncomePay("13 зарплата",
                new PaymentPeriod(new Period(Period.TimePeriod.YEAR, 1),
                        daySalary,
                        person.getDatePensionExit()),
                60000);

        IncomePay incomeRent = new IncomePay("Аренда",
                new PaymentPeriod(new Period(Period.TimePeriod.MONTH, 1),
                        daySalary,
                        person.getDatePensionExit()),
                15000);

        BankOperation eatOperation = new RatePay("Еда",
                new PaymentPeriod(new Period(Period.TimePeriod.MONTH, 1),
                        dayEat,
                        person.getDatePensionExit()),
                -45000);

        dividendOperation = new BankOperationPercent("банковский процент",
                new PaymentPeriod(new Period(Period.TimePeriod.MONTH, 1),
                        daySalary,
                        person.getDatePensionExit()),
                0.1);

        bankManager = new BankManager(bankAccount);
        bankManager.setPercentOfInflation(0);
        bankManager.addRepetitiveOperation(salaryOperation);
        bankManager.addRepetitiveOperation(salaryOperation13);
        bankManager.addRepetitiveOperation(incomeRent);
        bankManager.addRepetitiveOperation(eatOperation);
        bankManager.addRepetitiveOperation(dividendOperation);

        Calendar dayStartCredit = Calendar.getInstance();
        dayStartCredit.set(dayStartCredit.get(Calendar.YEAR) + 1, 6, 25);
        Calendar dayEndCredit = Calendar.getInstance();
        dayEndCredit.set(dayEndCredit.get(Calendar.YEAR) + 2, 3, 25);

        Goal carGoal = new Goal("Машина Vesta",
                600000,
                100000,
                new PaymentPeriod(new Period(Period.TimePeriod.MONTH, 1), dayStartCredit, dayEndCredit), 0);
        GoalManager carManager = new GoalManager(carGoal, bankManager);

        Calendar dayStartChicken = Calendar.getInstance();
        dayStartChicken.set(dayStartChicken.get(Calendar.YEAR) + 1, 3, 25);
        Calendar dayEndChicken = Calendar.getInstance();
        dayEndChicken.set(dayEndChicken.get(Calendar.YEAR) + 1, 8, 25);

        Goal chikenGoal = new Goal("Кухонный гарнитур",
                200000,
                0,
                new PaymentPeriod(new Period(Period.TimePeriod.MONTH, 1), dayStartChicken, dayEndChicken), 0);
        GoalManager chikenManager = new GoalManager(chikenGoal, bankManager);

        double maxPayment = bankManager.getMinimumMonthCash(dayStartCredit, dayEndCredit);
        bankManager.addGoalManager(carManager);
        bankManager.addGoalManager(chikenManager);
        System.out.println(carGoal.getMonthPayment());

        System.out.println(carManager.investmentCalculate().getMonthCount());
        System.out.println(carManager.creditCalculate().getMonthCount());

        observableChart.add(bankManager);
        observableChart.add(carManager);
        observableChart.add(chikenManager);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Персональное финансовое планирование");

        initRootLayout();
        // showSettings();
        showPfpOverview();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/bna/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PfpViewController controller;

    private void showPfpOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/bna/view/PfpOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOverview);
            controller = loader.getController();
            controller.setMainApp(this, bankManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSettings() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/bna/view/DialogPerson.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Настройки");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<AccountingManager> getObservableChart() {
        return observableChart;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
