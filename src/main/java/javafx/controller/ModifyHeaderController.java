package javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Controller
public class ModifyHeaderController implements Initializable {
    private Preferences pref;

    @FXML
    private TextField textFieldHeader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
        textFieldHeader.setText(pref.get("test_mark_header",
                "Program do opracowywania wyników testów wyboru"));
    }

    @FXML
    void buttonCancel_onAction() {
        Stage stage = (Stage) textFieldHeader.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buttonModifyHeader_onAction() {
        pref.put("test_mark_header", textFieldHeader.getText());
        Stage stage = (Stage) textFieldHeader.getScene().getWindow();
        stage.close();
    }
}
