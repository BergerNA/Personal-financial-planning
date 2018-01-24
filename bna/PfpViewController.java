package bna;

import bna.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.LinkedList;

public class PfpViewController {
    @FXML
    public LineChart<Calendar, Double> pfpChart;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public Button idButton;
    @FXML
    public ChoiceBox choiceBoxItem;
    @FXML
    public ComboBox comboBoxItem;
    @FXML
    public ListView listViewItem;
    @FXML
    public AnchorPane rootItemSettings;

    enum ItemType {Доходы, Расходы, Цели;}
    enum PeriodType {День, Неделя, Месяц, Год;}

    private MainApp mainApp;
    private BankManager bankManager;

    ObservableList<ItemType> options =
            FXCollections.observableArrayList(
                    ItemType.Доходы,
                    ItemType.Расходы,
                    ItemType.Цели
            );

    ObservableList<PeriodType> periodType =
            FXCollections.observableArrayList(
                    PeriodType.День,
                    PeriodType.Неделя,
                    PeriodType.Месяц,
                    PeriodType.День
            );

    @FXML
    public void initialize() {
        xAxis.setLabel("Month");
        comboBoxItem.setItems(options);
        comboBoxItem.setValue(ItemType.Доходы);
        comboBoxItem.setOnAction(event -> {
            ObservableList<IHasNamePay> list = null;
            paintSettingsClear();
            switch ((ItemType) comboBoxItem.getValue()) {
                case Доходы:
                    list = FXCollections.observableList(bankManager.getIncomePayList());
                    listViewItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
                        paintSettingsPayment((BankOperation) newValue);
                    });
                    break;
                case Расходы:
                    list = FXCollections.observableList(bankManager.getRatePayList());
                    listViewItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
                        paintSettingsPayment((BankOperation) newValue);
                    });
                    break;
                case Цели:
                    list = FXCollections.observableList(bankManager.getListGoalManager());
                    listViewItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
                        paintSettingsGoal((GoalManager)newValue);
                    });
            }
            listViewItem.setItems(list);
//            ObservableList<IncomePay> list = FXCollections.observableList(bankManager.getIncomePayList());
//            listViewItem.setItems(list);
        });
        listViewItem.setCellFactory(list -> new ItemCell()
        );
    }
    static class ItemCell extends ListCell<IHasNamePay> {
        @Override
        public void updateItem(IHasNamePay item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                if (item.getPayment() > 0) {
                    this.setText(item.getName() + "   +" + item.getPayment());
                } else {
                    this.setText(item.getName() + "   " + item.getPayment());
                }
            }
        }

    }

    public void setMainApp(MainApp mainApp, BankManager bankManager) {
        this.mainApp = mainApp;
        this.bankManager = bankManager;
        paint();
    }

    public void paint() {
        pfpChart.getData().retainAll();
        LinkedList<XYChart.Series> ser = new LinkedList<>();
        mainApp.getObservableChart().forEach(s -> ser.add(s.getSeries()));
        mainApp.getObservableChart().forEach(series -> pfpChart.getData().add(series.getSeries()));
        paintSettingsGoal(null);
    }

    private void paintSettingsClear() {
        rootItemSettings.getChildren().clear();
    }

    private void paintSettingsPayment(BankOperation operation) {
        GridPane root = new GridPane();

        root.setPadding(new Insets(20));
        root.setHgap(20);
        root.setVgap(10);

        Label labelItemName = new Label("Название:");
        TextField fieldName = new TextField();
        fieldName.setText(operation.getName());

        Label labelSum = new Label("Сумма:");
        NumberTextField fieldSum = new NumberTextField();
        fieldSum.setText(Double.toString(operation.getPay()));

        Label labelDate = new Label("Дата:");
        DatePicker datePickerItem = new DatePicker();
        datePickerItem.setValue(getLocalDate(operation.getPaymentPeriod().getStartDate()));

        Label labelPeriod = new Label("Периодичность:");
        NumberTextField fieldCount = new NumberTextField();
        fieldCount.setText(Integer.toString(operation.getPaymentPeriod().getPeriod().getValue()));
        ComboBox comboDate = new ComboBox();
        comboDate.setItems(periodType);
        comboDate.setValue(operation.getPaymentPeriod().getPeriod().getName());

        GridPane.setHalignment(labelItemName, HPos.RIGHT);
        root.add(labelItemName, 0, 0);

        GridPane.setHalignment(labelSum, HPos.RIGHT);
        root.add(labelSum, 0, 1);

        GridPane.setHalignment(labelDate, HPos.RIGHT);
        root.add(labelDate, 0, 2);

        GridPane.setHalignment(labelPeriod, HPos.RIGHT);
        root.add(labelPeriod, 0, 3);

        // Horizontal alignment for User Name field.
        GridPane.setHalignment(fieldName, HPos.LEFT);
        root.add(fieldName, 1, 0, 2, 1);

        // Horizontal alignment for Password field.
        GridPane.setHalignment(fieldSum, HPos.LEFT);
        root.add(fieldSum, 1, 1, 2, 1);

        GridPane.setHalignment(datePickerItem, HPos.LEFT);
        root.add(datePickerItem, 1, 2, 2, 1);

        GridPane.setHalignment(fieldCount, HPos.LEFT);
        root.add(fieldCount, 1, 3);

        GridPane.setHalignment(comboDate, HPos.LEFT);
        root.add(comboDate, 2, 3);

        rootItemSettings.getChildren().addAll(root);
    }

    private LocalDate getLocalDate (Calendar calendar){
        LocalDate localDateStart = LocalDate.of(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        return localDateStart;
    }

    private void paintSettingsGoal(GoalManager goalManager) {
        GridPane root = new GridPane();

        root.setPadding(new Insets(20));
        root.setHgap(15);
        root.setVgap(10);

        Label labelGoalName = new Label("Цель:");
        TextField fieldName = new TextField();
        GridPane.setHalignment(labelGoalName, HPos.RIGHT);
        root.add(labelGoalName, 0, 0);
        GridPane.setHalignment(fieldName, HPos.LEFT);
        root.add(fieldName, 1, 0, 2, 1);

        Label labelCash = new Label("Сумма:");
        NumberTextField fieldCash = new NumberTextField();
        GridPane.setHalignment(labelCash, HPos.RIGHT);
        root.add(labelCash, 0, 1);
        GridPane.setHalignment(fieldCash, HPos.LEFT);
        root.add(fieldCash, 1, 1, 2, 1);

        Label labelCashStart = new Label("Накоплено:");
        NumberTextField fieldCashStart = new NumberTextField();
        GridPane.setHalignment(labelCashStart, HPos.RIGHT);
        root.add(labelCashStart, 0, 2);
        GridPane.setHalignment(fieldCashStart, HPos.LEFT);
        root.add(fieldCashStart, 1, 2, 2, 1);

        Label labelPayment = new Label("Платеж:");
        NumberTextField fieldPayment = new NumberTextField();
        GridPane.setHalignment(labelPayment, HPos.RIGHT);
        root.add(labelPayment, 0, 3);
        GridPane.setHalignment(fieldPayment, HPos.LEFT);
        root.add(fieldPayment, 1, 3, 2, 1);

        Label labelDateStart = new Label("Дата начала:");
        DatePicker dateStartPickerGoal = new DatePicker();
        GridPane.setHalignment(labelDateStart, HPos.RIGHT);
        root.add(labelDateStart, 0, 4);
        GridPane.setHalignment(dateStartPickerGoal, HPos.LEFT);
        root.add(dateStartPickerGoal, 1, 4, 2, 1);

        Label labelDateEnd = new Label("Дата завершения:");
        DatePicker dateEndPickerGoal = new DatePicker();
        GridPane.setHalignment(labelDateEnd, HPos.RIGHT);
        root.add(labelDateEnd, 0, 5);
        GridPane.setHalignment(dateEndPickerGoal, HPos.LEFT);
        root.add(dateEndPickerGoal, 1, 5, 2, 1);

        Label labelCredit = new Label("Кредит:");
        GridPane.setHalignment(labelCredit, HPos.RIGHT);
        root.add(labelCredit, 0, 7);

        Label labelInvestment = new Label("Вклад:");
        GridPane.setHalignment(labelInvestment, HPos.RIGHT);
        root.add(labelInvestment, 0, 8);

        Label labelMonth = new Label("мес.");
        GridPane.setHalignment(labelMonth, HPos.CENTER);
        root.add(labelMonth, 1, 6);

        Label labelMonthCredit = new Label("");
        GridPane.setHalignment(labelMonthCredit, HPos.CENTER);
        root.add(labelMonthCredit, 1, 7);

        Label labelMonthInvestment = new Label("");
        GridPane.setHalignment(labelMonthInvestment, HPos.CENTER);
        root.add(labelMonthInvestment, 1, 8);

        Label labelSum = new Label("сумма");
        GridPane.setHalignment(labelSum, HPos.CENTER);
        root.add(labelSum, 2, 6);

        Label labelSumCredit = new Label("");
        GridPane.setHalignment(labelSumCredit, HPos.CENTER);
        root.add(labelSumCredit, 2, 7);

        Label labelSumInvestment = new Label("");
        GridPane.setHalignment(labelSumInvestment, HPos.CENTER);
        root.add(labelSumInvestment, 2, 8);
        rootItemSettings.getChildren().addAll(root);

        if (goalManager != null){
            fieldName.setText(goalManager.getName());
            fieldCash.setText(Double.toString(goalManager.getEndAmount()));
            fieldCashStart.setText(Double.toString(goalManager.getAmount()));
            fieldPayment.setText(Integer.toString(goalManager.getPayment()));
            LocalDate localDateStart = LocalDate.of(goalManager.getStartDate().get(Calendar.YEAR),
                    goalManager.getStartDate().get(Calendar.MONTH),
                    goalManager.getStartDate().get(Calendar.DAY_OF_MONTH));
            dateStartPickerGoal.setValue(localDateStart);
            LocalDate localDateEnd = LocalDate.of(goalManager.getEndDate().get(Calendar.YEAR),
                    goalManager.getEndDate().get(Calendar.MONTH),
                    goalManager.getEndDate().get(Calendar.DAY_OF_MONTH));
            dateEndPickerGoal.setValue(localDateEnd);
            labelMonthCredit.setText(Long.toString(goalManager.getCredit().getMonthCount()));
            labelMonthInvestment.setText(Long.toString(goalManager.getInvestment().getMonthCount()));
            labelSumCredit.setText(Double.toString(goalManager.getCredit().getEndPayment()));
            labelSumInvestment.setText(Double.toString(goalManager.getInvestment().getEndPayment()));
        }
    }

    class NumberTextField extends TextField {
        @Override
        public void replaceText(int start, int end, String text) {
            if (validate(text)) {
                super.replaceText(start, end, text);
            }
        }

        @Override
        public void replaceSelection(String text) {
            if (validate(text)) {
                super.replaceSelection(text);
            }
        }

        private boolean validate(String text) {
            return text.matches("[0-9]*");
        }
    }
}
