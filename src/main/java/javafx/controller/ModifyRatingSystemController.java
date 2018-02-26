package javafx.controller;

import javafx.CustomMessageBox;
import javafx.TextChangeListenerMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Controller
public class ModifyRatingSystemController implements Initializable {
    private CustomMessageBox customMessageBox;
    private Preferences pref;

    @FXML
    private VBox vBoxScene;
    @FXML
    private Label labelHeader, label_3_0, label_3_5, label_4_0, label_4_5, label_5_0, label_5_5;
    @FXML
    private TextField textField_3_0, textField_3_5, textField_4_0, textField_4_5, textField_5_0, textField_5_5;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
        labelHeader.setText(pref.get("header",
                "Program do opracowywania wyników testów wyboru"));
        customMessageBox = new CustomMessageBox("image/icon.png");

        textField_3_0.textProperty().addListener((observable, oldValue, newValue) -> labelMarkTextChange("3.0"));
        textField_3_5.textProperty().addListener((observable, oldValue, newValue) -> labelMarkTextChange("3.5"));
        textField_4_0.textProperty().addListener((observable, oldValue, newValue) -> labelMarkTextChange("4.0"));
        textField_4_5.textProperty().addListener((observable, oldValue, newValue) -> labelMarkTextChange("4.5"));
        textField_5_0.textProperty().addListener((observable, oldValue, newValue) -> labelMarkTextChange("5.0"));
        textField_5_5.textProperty().addListener((observable, oldValue, newValue) -> labelMarkTextChange("5.5"));

        textField_3_0.setText(String.valueOf(pref.getDouble("test_mark_mark_percentage_3_0", 55.00)));
        textField_3_5.setText(String.valueOf(pref.getDouble("test_mark_mark_percentage_3_5", 63.00)));
        textField_4_0.setText(String.valueOf(pref.getDouble("test_mark_mark_percentage_4_0", 72.00)));
        textField_4_5.setText(String.valueOf(pref.getDouble("test_mark_mark_percentage_4_5", 81.00)));
        textField_5_0.setText(String.valueOf(pref.getDouble("test_mark_mark_percentage_5_0", 90.00)));
        textField_5_5.setText(String.valueOf(pref.getDouble("test_mark_mark_percentage_5_5", 99.00)));
    }

    private void labelMarkTextChange(String mark) {
        TextField textFieldMarkPercentage = null;
        Label labelMark = null;

        switch (mark) {
            case "3.0":
                textFieldMarkPercentage = textField_3_0;
                labelMark = label_3_0;
                break;
            case "3.5":
                textFieldMarkPercentage = textField_3_5;
                labelMark = label_3_5;
                break;
            case "4.0":
                textFieldMarkPercentage = textField_4_0;
                labelMark = label_4_0;
                break;
            case "4.5":
                textFieldMarkPercentage = textField_4_5;
                labelMark = label_4_5;
                break;
            case "5.0":
                textFieldMarkPercentage = textField_5_0;
                labelMark = label_5_0;
                break;
            case "5.5":
                textFieldMarkPercentage = textField_5_5;
                labelMark = label_5_5;
                break;
        }

        assert textFieldMarkPercentage != null;
        TextChangeListenerMethods textChangeListenerMethods = new TextChangeListenerMethods();
        textChangeListenerMethods.changeLabelText("((?!^0*$)(?!^0*\\.0*$)^\\d{1,2}(\\.\\d{1,2})?$)|(^100$)",
                textFieldMarkPercentage, labelMark, "Podaj procent punktów.", "Niepoprawny format.");
    }

    @FXML
    void buttonCancel_onAction() {
        Stage stage = (Stage) vBoxScene.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buttonModifyHeader_onAction() {
        if (label_3_0.getText().equals("") && label_3_5.getText().equals("") && label_4_0.getText().equals("")
                && label_4_5.getText().equals("") && label_5_0.getText().equals("") && label_5_5.getText().equals("")) {
            Double markPercentage_3_0 = Double.valueOf(textField_3_0.getText()),
                    markPercentage_3_5 = Double.valueOf(textField_3_5.getText()),
                    markPercentage_4_0 = Double.valueOf(textField_4_0.getText()),
                    markPercentage_4_5 = Double.valueOf(textField_4_5.getText()),
                    markPercentage_5_0 = Double.valueOf(textField_5_0.getText()),
                    markPercentage_5_5 = Double.valueOf(textField_5_5.getText());

            if (!(markPercentage_3_5 > markPercentage_3_0 && markPercentage_4_0 > markPercentage_3_5
                    && markPercentage_4_5 > markPercentage_4_0 && markPercentage_5_0 > markPercentage_4_5
                    && markPercentage_5_5 > markPercentage_5_0))
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja modyfikacji systemu oceniania nie powiodła się.",
                        "Powód: Progi procentowe ocen muszą posiadać wartości rosnące.")
                        .showAndWait();
            else {
                pref.putDouble("test_mark_mark_percentage_3_0", markPercentage_3_0);
                pref.putDouble("test_mark_mark_percentage_3_5", markPercentage_3_5);
                pref.putDouble("test_mark_mark_percentage_4_0", markPercentage_4_0);
                pref.putDouble("test_mark_mark_percentage_4_5", markPercentage_4_5);
                pref.putDouble("test_mark_mark_percentage_5_0", markPercentage_5_0);
                pref.putDouble("test_mark_mark_percentage_5_5", markPercentage_5_5);

                Stage stage = (Stage) vBoxScene.getScene().getWindow();
                stage.close();
            }
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja modyfikacji systemu oceniania nie powiodła się.",
                    "Powód: co najmniej jedna wartość progu uzyskania oceny posiada niepoprawny format.")
                    .showAndWait();
    }
}
