package bna;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class SettingsController {
    @FXML
    public DatePicker idDate;
    @FXML
    public TextField idYearsOld;
    @FXML
    public TextField idCash;
    @FXML
    public TextField idPercentOfDeposit;
    @FXML
    public TextField idPercentOfCredit;
    @FXML
    public TextField idInflation;
    @FXML
    public CheckBox idFinancial;
    @FXML
    Button idSave;
    @FXML
    Button idCancel;

    @FXML
    public void initialize() {

    }
}
