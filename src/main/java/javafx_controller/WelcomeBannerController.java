package javafx_controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class WelcomeBannerController implements Initializable {
    @FXML
    private ProgressBar progressBarAppLoading;

    public void initMainScene() {
        runProgressBar();
        loadMainScene();
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    private void runProgressBar() {

    }

    private void loadMainScene() {

    }
}
