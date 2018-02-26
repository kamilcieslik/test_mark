package javafx.controller;

import app.Main;
import javafx.CustomMessageBox;
import javafx.TextChangeListenerMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import test_mark.exception.CorrectAnswersViolationException;
import test_mark.exception.MinimumNumberOfObjectsViolationException;
import test_mark.exception.UniqueViolationException;
import test_mark.parsers.TestTemplateXmlParser;
import test_mark.test_template.Answer;
import test_mark.test_template.Question;
import test_mark.test_template.TestTemplate;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Controller
public class AddTestTemplateController implements Initializable {
    private CustomMessageBox customMessageBox;
    private String withoutSpacesAtStartAndAndPattern = "^\\S$|^\\S[\\s\\S]*\\S$";
    private ObservableList<Answer> newQuestionAnswersObservableList = FXCollections.observableArrayList();
    private ObservableList<Answer> selectedQuestionAnswersObservableList = FXCollections.observableArrayList();
    private ObservableList<Question> questionsObservableList = FXCollections.observableArrayList();

    @FXML
    private Label labelHeader, labelCourseName, labelExamName, labelQuestionText, labelAnswerSymbol, labelAnswerText;
    @FXML
    private TextField textFieldCourseName, textFieldExamName, textFieldQuestionText, textFieldAnswerSymbol,
            textFieldAnswerText;
    @FXML
    private TableView<Question> tableViewQuestions;
    @FXML
    private TableColumn<Question, String> tableColumnQuestionText;
    @FXML
    private TableView<Answer> tableViewSelectedQuestionAnswers, tableViewNewQuestionAnswers;
    @FXML
    private TableColumn<Answer, Character> tableColumnSelectedQuestionAnswerSymbol, tableColumnNewQuestionAnswerSymbol;
    @FXML
    private TableColumn<Answer, String> tableColumnSelectedQuestionAnswerText, tableColumnNewQuestionAnswerText;
    @FXML
    private TableColumn<Answer, Boolean> tableColumnSelectedQuestionAnswerCorrect, tableColumnNewQuestionAnswerCorrect;
    @FXML
    private CheckBox checkBoxAnswerCorrect;

    public void setInitialTestTemplateValues(TestTemplate testTemplate){
            questionsObservableList.addAll(testTemplate.getQuestions());
            tableViewQuestions.setItems(questionsObservableList);
            textFieldCourseName.setText(testTemplate.getCourseName());
            textFieldExamName.setText(testTemplate.getExamName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences pref = Preferences.userRoot();
        labelHeader.setText(pref.get("header",
                "Program do opracowywania wyników testów wyboru"));
        customMessageBox = new CustomMessageBox("image/icon.png");
        initTableViews();

        TextChangeListenerMethods textChangeListenerMethods = new TextChangeListenerMethods();
        textFieldCourseName.textProperty().addListener((observable, oldValue, newValue) -> textChangeListenerMethods
                .changeLabelText(withoutSpacesAtStartAndAndPattern, textFieldCourseName, labelCourseName,
                        "Podaj nazwę kursu.", "Niepoprawny format."));
        textFieldExamName.textProperty().addListener((observable, oldValue, newValue) -> textChangeListenerMethods
                .changeLabelText(withoutSpacesAtStartAndAndPattern, textFieldExamName, labelExamName,
                        "Podaj nazwę testu wyboru.", "Niepoprawny format."));
        textFieldQuestionText.textProperty().addListener((observable, oldValue, newValue) -> textChangeListenerMethods
                .changeLabelText(withoutSpacesAtStartAndAndPattern, textFieldQuestionText, labelQuestionText,
                        "Podaj treść pytania.", "Niepoprawny format."));
        textFieldAnswerSymbol.textProperty().addListener((observable, oldValue, newValue) -> textChangeListenerMethods
                .changeLabelText("^[A-Z]{1}$", textFieldAnswerSymbol, labelAnswerSymbol,
                        "Podaj symbol.", "Niepoprawny forma."));
        textFieldAnswerText.textProperty().addListener((observable, oldValue, newValue) -> textChangeListenerMethods
                .changeLabelText(withoutSpacesAtStartAndAndPattern, textFieldAnswerText, labelAnswerText,
                        "Podaj treść.", "Niepoprawny forma."));
    }

    @FXML
    private void buttonAddAnswer_onAction() {
        if (labelAnswerSymbol.getText().equals("") && labelAnswerText.getText().equals("")) {
            Answer newAnswer = new Answer(textFieldAnswerSymbol.getText().charAt(0), textFieldAnswerText.getText(),
                    checkBoxAnswerCorrect.isSelected());
            newQuestionAnswersObservableList.add(newAnswer);
            tableViewNewQuestionAnswers.setItems(newQuestionAnswersObservableList);
            clearNewAnswerComponents(false);
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja utworzenia odpowiedzi nie powiodła się.",
                    "Powód: co najmniej jedna wartość odpowiedzi posiada niepoprawny format.")
                    .showAndWait();
    }

    @FXML
    private void buttonAddQuestion_onAction() {
        if (!labelQuestionText.getText().equals(""))
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja utworzenia pytania nie powiodła się.",
                    "Powód: nie podano treści pytania.")
                    .showAndWait();
        else {
            try {
                Question newQuestion = new Question(textFieldQuestionText.getText(), new ArrayList<>(newQuestionAnswersObservableList));
                questionsObservableList.add(newQuestion);
                tableViewQuestions.setItems(questionsObservableList);
                clearNewAnswerComponents(true);
            } catch (UniqueViolationException | CorrectAnswersViolationException | MinimumNumberOfObjectsViolationException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja utworzenia pytania nie powiodła się.",
                        "Powód: " + e.getCause().getMessage() + ".")
                        .showAndWait();
            }
        }
    }

    @FXML
    private void buttonAddTestTemplates_onAction() {
        if (!(labelCourseName.getText().equals("")&&labelExamName.getText().equals("")))
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja utworzenia szablonu testu nie powiodła się.",
                    "Powód: co najmniej jedna wartość szablonu testu posiada niepoprawny format.")
                    .showAndWait();
        else {
            try {
                TestTemplate newTestTemplate = new TestTemplate(textFieldCourseName.getText(),
                        textFieldExamName.getText(), new Date(), new ArrayList<>(questionsObservableList));
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Test Mark - wybór katalogu zapisu szablonu testu");
                String directoryPath = chooser.showDialog(Main.getMainStage()).getAbsolutePath();
                TestTemplateXmlParser testTemplateXmlParser = new TestTemplateXmlParser();
                testTemplateXmlParser.writeTestTemplate(newTestTemplate, directoryPath);
                Stage stage = (Stage) textFieldQuestionText.getScene().getWindow();
                stage.close();
            } catch (MinimumNumberOfObjectsViolationException | JAXBException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja utworzenia szablonu testu nie powiodła się.",
                        "Powód: " + e.getCause().getMessage() + ".")
                        .showAndWait();
            }
        }
    }

    @FXML
    private void buttonCancel_onAction() {
        Stage stage = (Stage) textFieldQuestionText.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void buttonDeleteAnswerFromNewQuestion_onAction() {
        if (tableViewNewQuestionAnswers.getSelectionModel().getSelectedItem() != null) {
            newQuestionAnswersObservableList.remove(tableViewNewQuestionAnswers.getSelectionModel().getSelectedItem());
            tableViewNewQuestionAnswers.refresh();
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja usunięcia odpowiedzi nie powiodła się.",
                    "Powód: nie zaznaczono odpowiedzi.")
                    .showAndWait();
    }

    @FXML
    private void buttonDeleteQuestion_onAction() {
        if (tableViewQuestions.getSelectionModel().getSelectedItem() != null) {
            questionsObservableList.remove(tableViewQuestions.getSelectionModel().getSelectedItem());
            selectedQuestionAnswersObservableList.clear();
            tableViewQuestions.refresh();
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja usunięcia pytania nie powiodła się.",
                    "Powód: nie zaznaczono pytania.")
                    .showAndWait();
    }

    @FXML
    private void tableViewQuestions_onMouseClicked() {
        if (tableViewQuestions.getSelectionModel().getSelectedItem() != null) {
            selectedQuestionAnswersObservableList.clear();
            selectedQuestionAnswersObservableList
                    .addAll(new ArrayList<>(tableViewQuestions.getSelectionModel().getSelectedItem().getAnswers().values()));
            tableViewSelectedQuestionAnswers.setItems(selectedQuestionAnswersObservableList);
        }
    }

    private void initTableViews() {
        tableColumnNewQuestionAnswerSymbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        tableColumnNewQuestionAnswerSymbol.setCellFactory(col -> characterConversionToString());
        tableColumnNewQuestionAnswerText.setCellValueFactory(new PropertyValueFactory<>("content"));
        tableColumnNewQuestionAnswerCorrect.setCellValueFactory(new PropertyValueFactory<>("correct"));
        tableColumnNewQuestionAnswerCorrect.setCellFactory(col -> booleanConversionToString());

        tableColumnSelectedQuestionAnswerSymbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        tableColumnSelectedQuestionAnswerSymbol.setCellFactory(col -> characterConversionToString());
        tableColumnSelectedQuestionAnswerText.setCellValueFactory(new PropertyValueFactory<>("content"));
        tableColumnSelectedQuestionAnswerCorrect.setCellValueFactory(new PropertyValueFactory<>("correct"));
        tableColumnSelectedQuestionAnswerCorrect.setCellFactory(col -> booleanConversionToString());

        tableColumnQuestionText.setCellValueFactory(new PropertyValueFactory<>("content"));
    }

    private TableCell<Answer, Boolean> booleanConversionToString() {
        return new TableCell<Answer, Boolean>() {
            @Override
            public void updateItem(Boolean correct, boolean empty) {
                super.updateItem(correct, empty);
                if (empty) {
                    setText(null);
                } else {
                    if (correct)
                        setText("tak");
                    else
                        setText("nie");
                }
            }
        };
    }

    private TableCell<Answer, Character> characterConversionToString() {
        return new TableCell<Answer, Character>() {
            @Override
            public void updateItem(Character symbol, boolean empty) {
                super.updateItem(symbol, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(symbol.toString());
                }
            }
        };
    }

    private void clearNewAnswerComponents(Boolean clearQuestionField) {
        textFieldAnswerSymbol.setText("");
        textFieldAnswerText.setText("");
        checkBoxAnswerCorrect.setSelected(false);

        if (clearQuestionField) {
            textFieldQuestionText.setText("");
            newQuestionAnswersObservableList.clear();
            tableViewNewQuestionAnswers.refresh();
        }
    }
}
