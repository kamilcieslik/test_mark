package javafx.controller;

import app.Main;
import javafx.CustomMessageBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;
import test_mark.answers_cards_collection.AnswersCardsCollection;
import test_mark.exception.UniqueViolationException;
import test_mark.parsers.AnswersCardCollectionXlsParser;
import test_mark.parsers.TestTemplateXmlParser;
import test_mark.test_template.TestTemplate;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

@Controller
public class MainController implements Initializable {
    private Preferences pref;
    private CustomMessageBox customMessageBox;
    private ObservableList<TestTemplate> testTemplatesObservableList = FXCollections.observableArrayList();
    private ObservableList<AnswersCardsCollection> answersCardsCollectionsObservableList = FXCollections.observableArrayList();

    @FXML
    private Label labelHeader, labelSelectedTestTemplateCourseName, labelSelectedTestTemplateExamName,
            labelSelectedTestTemplateQuestionsNumber, labelSelectedTestTemplateDate,
            labelSelectedAnswersCardsCollectionCourseName, labelSelectedAnswersCardsCollectionExamName,
            labelSelectedAnswersCardsCollectionQuestionsNumber, labelSelectedAnswersCardsCollectionDate;
    @FXML
    private TableView<TestTemplate> tableViewTestTemplates;
    @FXML
    private TableView<AnswersCardsCollection> tableViewAnswerCardsCollections;
    @FXML
    private TableColumn<TestTemplate, String> tableColumnTestTemplateCourseName, tableColumnTestTemplateExamName;
    @FXML
    private TableColumn<AnswersCardsCollection, String> tableColumnAnswersCardsCollectionCourseName,
            tableColumnAnswersCardsCollectionExamName;
    @FXML
    private TableColumn<AnswersCardsCollection, Date> tableColumnAnswersCardsCollectionDate;
    @FXML
    private VBox vBoxCheckResults;
    @FXML
    private HBox hBoxShowTestTemplates, hBoxShowAnswerCardsCollections;
    @FXML
    private TextArea textAreaDevelopResultsDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
        labelHeader.setText(pref.get("header",
                "Program do opracowywania wyników testów wyboru"));
        customMessageBox = new CustomMessageBox("image/icon.png");
        initTableViews();
        clearTestTemplateComponents();
        clearAnswersCardsCollectionComponents();
        setHBoxVisible(hBoxShowTestTemplates, false);
        setHBoxVisible(hBoxShowAnswerCardsCollections, false);
        setVBoxCheckResultsVisible(false);
    }

    @FXML
    void buttonAddAnswerCardsCollections_onAction() {
        //TODO:
    }

    @FXML
    void buttonShowAnswerCardsCollections_onAction() {
        //TODO:
    }

    @FXML
    void buttonAddTestTemplates_onAction() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/add_test_template.fxml"));
            loader.load();
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Test Mark - tworzenie nowego szablonu odpowiedzi");
            stage.getIcons().add(new Image("/image/icon.png"));
            Stage currentStage = (Stage) labelHeader.getScene().getWindow();
            stage.setScene(new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 42.5));
            stage.showAndWait();
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    @FXML
    void buttonShowTestTemplates_onAction() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/add_test_template.fxml"));
            loader.load();
            AddTestTemplateController loaderController = loader.getController();
            loaderController.setInitialTestTemplateValues(tableViewTestTemplates.getSelectionModel().getSelectedItem());
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Test Mark - tworzenie nowego szablonu odpowiedzi");
            stage.getIcons().add(new Image("/image/icon.png"));
            Stage currentStage = (Stage) labelHeader.getScene().getWindow();
            stage.setScene(new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 42.5));
            stage.showAndWait();
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    @FXML
    void buttonCheckResults_onAction() {
        //TODO:
    }

    @FXML
    void buttonDevelopResults_onAction() {
        setVBoxCheckResultsVisible(true);
        //TODO:
    }

    @FXML
    void buttonReadAnswerCardsCollections_onAction() {
        FileChooser answerCardsCollectionsXlsFileChooser = new FileChooser();
        answerCardsCollectionsXlsFileChooser.setTitle("Test Mark - wybór zbiorów kart odpowiedzi");
        answerCardsCollectionsXlsFileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Zbiory kart odpowiedzi", "*.xls"));
        List<File> answersCardsCollectionsXlsFiles = answerCardsCollectionsXlsFileChooser.
                showOpenMultipleDialog(Main.getMainStage());
        if (answersCardsCollectionsXlsFiles != null) {
            AnswersCardCollectionXlsParser answersCardCollectionXlsParser = new AnswersCardCollectionXlsParser();
            try {
                for (File file : answersCardsCollectionsXlsFiles)
                    answersCardsCollectionsObservableList.add(answersCardCollectionXlsParser.readAnswersCardsCollection(file));
                tableViewTestTemplates.setItems(testTemplatesObservableList);
            } catch (IOException | UniqueViolationException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja odczytania zbiorów kart odpowiedzi nie powiodła się.",
                        "Powód: " + e.getCause().getMessage() + ".")
                        .showAndWait();
            }
        }
    }

    @FXML
    void buttonClearAnswerCardsCollections_onAction() {
        answersCardsCollectionsObservableList.clear();
        setHBoxVisible(hBoxShowAnswerCardsCollections, false);
        clearAnswersCardsCollectionComponents();
        textAreaDevelopResultsDialog.setText("");
        setVBoxCheckResultsVisible(false);
    }

    @FXML
    void buttonReadTestTemplates_onAction() {
        FileChooser testTemplatesFileChooser = new FileChooser();
        testTemplatesFileChooser.setTitle("Test Mark - wybór plików szablonów testów");
        testTemplatesFileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Szablony testów", "*.xml"));
        List<File> testTemplatesXmlFiles = testTemplatesFileChooser.
                showOpenMultipleDialog(Main.getMainStage());
        if (testTemplatesXmlFiles != null) {
            TestTemplateXmlParser testTemplateXmlParser = new TestTemplateXmlParser();
            try {
                for (File file : testTemplatesXmlFiles)
                    testTemplatesObservableList.add(testTemplateXmlParser.readTestTemplate(file));
                tableViewTestTemplates.setItems(testTemplatesObservableList);
            } catch (JAXBException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja odczytania szablonów testów nie powiodła się.",
                        "Powód: " + e.getCause().getMessage() + ".")
                        .showAndWait();
            }
        }
    }

    @FXML
    void buttonClearTestTemplates_onAction() {
        testTemplatesObservableList.clear();
        setHBoxVisible(hBoxShowTestTemplates, false);
        clearTestTemplateComponents();
        textAreaDevelopResultsDialog.setText("");
        setVBoxCheckResultsVisible(false);
    }

    @FXML
    void menuItemModifyHeader_onAction() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/modify_header.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setTitle("Test Mark - modyfikacja treści nagłówka");
            stage.getIcons().add(new Image("/image/icon.png"));
            stage.setScene(new Scene(root, 819, 220));
            stage.showAndWait();
            labelHeader.setText(pref.get("header",
                    "Program do opracowywania wyników testów wyboru"));
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    @FXML
    void menuItemModifyRatingSystem_onAction() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/modify_rating_system.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setTitle("Test Mark - modyfikacja systemu oceniania");
            stage.getIcons().add(new Image("/image/icon.png"));
            stage.setScene(new Scene(root, 698, 687));
            stage.showAndWait();
            labelHeader.setText(pref.get("header",
                    "Program do opracowywania wyników testów wyboru"));
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    @FXML
    void tableViewAnswerCardsCollections_onMouseClicked() {
        //TODO:
        //setHBoxVisible(hBoxShowAnswerCardsCollections, true);
    }

    @FXML
    void tableViewTestTemplates_onMouseClicked() {
        TestTemplate selectedTestTemplate = tableViewTestTemplates.getSelectionModel().getSelectedItem();
        if (selectedTestTemplate != null) {
            labelSelectedTestTemplateCourseName.setText(selectedTestTemplate.getCourseName());
            labelSelectedTestTemplateExamName.setText(selectedTestTemplate.getExamName());
            labelSelectedTestTemplateQuestionsNumber.setText(String.valueOf(selectedTestTemplate.getQuestions().size()));
            labelSelectedTestTemplateDate.setText(new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss")
                    .format(selectedTestTemplate.getCreationDate()));
            setHBoxVisible(hBoxShowTestTemplates, true);
        }
    }

    private void initTableViews() {
        tableColumnTestTemplateCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumnTestTemplateExamName.setCellValueFactory(new PropertyValueFactory<>("examName"));

        tableColumnAnswersCardsCollectionCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumnAnswersCardsCollectionExamName.setCellValueFactory(new PropertyValueFactory<>("examName"));
        tableColumnAnswersCardsCollectionDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        tableColumnAnswersCardsCollectionDate.setCellFactory(col -> customDateFormat());
    }

    private TableCell<AnswersCardsCollection, Date> customDateFormat() {
        return new TableCell<AnswersCardsCollection, Date>() {
            @Override
            public void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss").format(date));
                }
            }
        };
    }

    private void clearTestTemplateComponents() {
        labelSelectedTestTemplateCourseName.setText("---");
        labelSelectedTestTemplateExamName.setText("---");
        labelSelectedTestTemplateQuestionsNumber.setText("---");
        labelSelectedTestTemplateDate.setText("---");
    }

    private void clearAnswersCardsCollectionComponents() {
        labelSelectedAnswersCardsCollectionCourseName.setText("---");
        labelSelectedAnswersCardsCollectionExamName.setText("---");
        labelSelectedAnswersCardsCollectionQuestionsNumber.setText("---");
        labelSelectedAnswersCardsCollectionDate.setText("---");
    }

    private void setHBoxVisible(HBox hBox, Boolean visible) {
        if (visible) {
            hBox.setVisible(true);
            hBox.setDisable(false);
            hBox.setMinWidth(Control.USE_COMPUTED_SIZE);
            hBox.setMinHeight(Control.USE_COMPUTED_SIZE);
            hBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
            hBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
            hBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
            hBox.setMaxWidth(Control.USE_COMPUTED_SIZE);
        } else {
            hBox.setVisible(false);
            hBox.setDisable(true);
            hBox.setMinWidth(0);
            hBox.setPrefWidth(0);
            hBox.setMinHeight(0);
            hBox.setPrefHeight(0);
            hBox.setMaxWidth(0);
            hBox.setMaxHeight(0);
        }

    }

    private void setVBoxCheckResultsVisible(Boolean visible) {
        if (visible) {
            vBoxCheckResults.setVisible(true);
            vBoxCheckResults.setDisable(false);
            vBoxCheckResults.setMinWidth(Control.USE_COMPUTED_SIZE);
            vBoxCheckResults.setMinHeight(Control.USE_COMPUTED_SIZE);
            vBoxCheckResults.setPrefWidth(Control.USE_COMPUTED_SIZE);
            vBoxCheckResults.setPrefHeight(Control.USE_COMPUTED_SIZE);
            vBoxCheckResults.setMaxWidth(Control.USE_COMPUTED_SIZE);
            vBoxCheckResults.setMaxHeight(Control.USE_COMPUTED_SIZE);
        } else {
            vBoxCheckResults.setVisible(false);
            vBoxCheckResults.setDisable(true);
            vBoxCheckResults.setMinWidth(0);
            vBoxCheckResults.setMinHeight(0);
            vBoxCheckResults.setPrefWidth(0);
            vBoxCheckResults.setPrefHeight(0);
            vBoxCheckResults.setMaxWidth(0);
            vBoxCheckResults.setMaxHeight(0);
        }
    }
}
