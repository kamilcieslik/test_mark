package javafx_controller;

import app.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class WelcomeBannerController implements Initializable {
    private static final int PROGRESS_BAR_SECONDS = 2;
    @FXML
    private ProgressBar progressBarAppLoading;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initMainScene() {
        try {
            runProgressBar(PROGRESS_BAR_SECONDS);
            loadMainScene();
        } catch (InterruptedException e) {
            loadMainScene();
        }
    }

    private void runProgressBar(int seconds) throws InterruptedException {
        Double progress = 1.0 / (seconds * 2);
        for (int i = 0; i < seconds * 2; i++) {
            Thread.sleep(500);
            progressBarAppLoading.progressProperty().set(progress + progressBarAppLoading.progressProperty().get());
        }
    }

    private void loadMainScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                try {
                    loader.setLocation(getClass().getResource("../fxml/main.fxml"));
                    loader.load();
                    Parent parent = loader.getRoot();

                    Stage primaryStage = new Stage();
                    Main.setMainStage(primaryStage);
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.resizableProperty().setValue(Boolean.TRUE);
                    primaryStage.setOnHidden(event -> Platform.exit());
                    primaryStage.getIcons().add(new Image("/image/icon.png"));
                    primaryStage.setMinWidth(970);
                    primaryStage.setMinHeight(820);
                    primaryStage.setScene(new Scene(parent, 1600, 900));

                    Stage stage = (Stage) progressBarAppLoading.getScene().getWindow();
                    stage.hide();
                    primaryStage.show();
                } catch (IOException ioEcx) {
                    Logger.getLogger(WelcomeBannerController.class.getName()).log(Level.SEVERE, null, ioEcx);
                }
            }
        });
    }
}
