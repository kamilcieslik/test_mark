package app;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        try {
            Main.mainStage = primaryStage;
            loader.setLocation(getClass().getResource("../fxml/welcome_banner.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            primaryStage.getIcons().add(new Image("/image/icon.png"));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.setScene(new Scene(root, 819, 325));
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
