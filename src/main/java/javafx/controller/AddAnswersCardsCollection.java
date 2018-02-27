package javafx.controller;

import app.Main;
import javafx.CustomMessageBox;
import javafx.ListenerMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import test_mark.answers_cards_collection.AnswersCard;
import test_mark.answers_cards_collection.AnswersCardsCollection;
import test_mark.answers_cards_collection.StudentQuestionAnswers;
import test_mark.exception.MinimumNumberOfObjectsViolationException;
import test_mark.exception.UniqueViolationException;
import test_mark.parser.TestXlsParser;
import test_mark.test_template.Answer;
import test_mark.test_template.Question;
import test_mark.test_template.TestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;

@Controller
public class AddAnswersCardsCollection implements Initializable {
    private CustomMessageBox customMessageBox;
    private TestTemplate testTemplate;
    private Set<Character> questionAnswers;
    private ObservableList<Question> comboBoxQuestionsObservableList = FXCollections.observableArrayList();
    private ObservableList<Answer> comboBoxQuestionAnswersObservableList = FXCollections.observableArrayList();
    private ObservableList<StudentQuestionAnswers> newAnswersCardAnswersObservableList = FXCollections.observableArrayList();
    private ObservableList<AnswersCard> answersCardsObservableList = FXCollections.observableArrayList();
    private ObservableList<StudentQuestionAnswers> selectedAnswersCardAnswersObservableList = FXCollections.observableArrayList();

    @FXML
    private Label labelHeader, labelExamName, labelCourseName, labelNumberOfQuestions, labelCreationDate,
            labelStudentIndex, labelQuestion, labelAnswer;
    @FXML
    private ComboBox<Integer> comboBoxDay, comboBoxMonth;
    @FXML
    private ComboBox<Question> comboBoxQuestion;
    @FXML
    private ComboBox<Answer> comboBoxAnswer;
    @FXML
    private TextField textFieldYear, textFieldStudentIndex, textFieldChosenAnswers;
    @FXML
    private TableView<AnswersCard> tableViewAnswerCards;
    @FXML
    private TableColumn<AnswersCard, String> tableColumnAnswerCardsStudentIndex;
    @FXML
    private TableView<StudentQuestionAnswers> tableViewSelectedAnswersCardAnswers;
    @FXML
    private TableColumn<StudentQuestionAnswers, Integer> tableColumnSelectedAnswersCardAnswersQuestionNumber;
    @FXML
    private TableColumn<StudentQuestionAnswers, Set<Character>> tableColumnSelectedAnswersCardQuestionAnswers;
    @FXML
    private TableView<StudentQuestionAnswers> tableViewNewAnswersCardAnswers;
    @FXML
    private TableColumn<StudentQuestionAnswers, Integer> tableColumnNewAnswersCardAnswersQuestionNumber;
    @FXML
    private TableColumn<StudentQuestionAnswers, Set<Character>> tableColumnNewAnswersCardQuestionAnswers;

    public void setInitialAnswersCardsCollectionValues(TestTemplate testTemplate) {
        this.testTemplate = testTemplate;
        labelExamName.setText(testTemplate.getExamName());
        labelCourseName.setText(testTemplate.getCourseName());
        labelNumberOfQuestions.setText(String.valueOf(testTemplate.getQuestions().values().size()));
        labelCreationDate.setText(new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss")
                .format(testTemplate.getCreationDate()));

        questionAnswers = new HashSet<>();
        comboBoxQuestionsObservableList.addAll(testTemplate.getQuestions().values());
        comboBoxQuestion.setItems(comboBoxQuestionsObservableList);

        comboBoxQuestionsObservableList.remove(testTemplate.getQuestions().get(1));
        comboBoxQuestionsObservableList.add(testTemplate.getQuestions().get(1));
        comboBoxQuestionsObservableList.sort((a, b) -> a.getNumber() < b.getNumber() ? -1 : a.getNumber().equals(b.getNumber()) ? 0 : 1);

        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }
        comboBoxDay.setItems(days);

        ObservableList<Integer> months = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        comboBoxMonth.setItems(months);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences pref = Preferences.userRoot();
        labelHeader.setText(pref.get("test_mark_header",
                "Program do opracowywania wyników testów wyboru"));
        customMessageBox = new CustomMessageBox("image/icon.png");
        initTableViews();

        ListenerMethods listenerMethods = new ListenerMethods();
        textFieldStudentIndex.textProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelTextField("^[0-9]{6}$", textFieldStudentIndex, labelStudentIndex,
                        "Podaj nr indeksu studenta.", "Niepoprawny format."));

        comboBoxQuestion.valueProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelComboBox(comboBoxQuestion, labelQuestion,
                        "Wybierz pytanie."));

        comboBoxAnswer.valueProperty().addListener((observable, oldValue, newValue) -> listenerMethods
                .changeLabelComboBox(comboBoxAnswer, labelAnswer,
                        "Wybierz warianty odpowiedzi."));
    }

    @FXML
    void buttonAddAnswersCard_onAction() {
        if (!labelStudentIndex.getText().equals(""))
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja karty odpowiedzi nie powiodła się.",
                    "Powód: nie podano indeksu studenta.")
                    .showAndWait();
        else if (comboBoxQuestionsObservableList.size() > 0) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja karty odpowiedzi nie powiodła się.",
                    "Powód: nie określono odpowiedzi na wszystkie pytania.")
                    .showAndWait();
        } else {
            try {
                AnswersCard newAnswersCard = new AnswersCard(textFieldStudentIndex.getText(), newAnswersCardAnswersObservableList);
                answersCardsObservableList.add(newAnswersCard);
                tableViewAnswerCards.setItems(answersCardsObservableList);
                textFieldStudentIndex.setText("");
                newAnswersCardAnswersObservableList.clear();
                comboBoxQuestionsObservableList.clear();
                comboBoxQuestionsObservableList.addAll(testTemplate.getQuestions().values());
            } catch (UniqueViolationException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja karty odpowiedzi nie powiodła się.",
                        "Powód: " + e.getCause().getMessage() + ".")
                        .showAndWait();
            }
        }
    }

    @FXML
    void buttonAddAnswersCardsCollection_onAction() {
        if ((textFieldYear.getText().equals("") || comboBoxMonth.getSelectionModel().getSelectedItem() == null
                || comboBoxDay.getSelectionModel().getSelectedItem() == null))
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja utworzenia zbioru kart odpowiedzi nie powiodła się.",
                    "Powód: nie podano daty przeprowadzenia testu.")
                    .showAndWait();
        else {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Test Mark - wybór katalogu zapisu zbioru kart odpowiedzi");
            File directory = chooser.showDialog(Main.getMainStage());
            if (directory != null) {
                String directoryPath = directory.getAbsolutePath();
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setLenient(false);
                    calendar.set(Calendar.YEAR, Integer.parseInt(textFieldYear.getText()));
                    calendar.set(Calendar.MONTH, comboBoxMonth.getSelectionModel().getSelectedItem() - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, comboBoxDay.getSelectionModel().getSelectedItem());
                    Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    AnswersCardsCollection newAnswersCardsCollection = new AnswersCardsCollection(testTemplate.getCourseName(),
                            testTemplate.getExamName(), date, testTemplate.getQuestions().size(), answersCardsObservableList);
                    TestXlsParser testXlsParser = new TestXlsParser();
                    testXlsParser.writeAnswersCardsCollection(newAnswersCardsCollection, directoryPath);
                    Stage stage = (Stage) textFieldYear.getScene().getWindow();
                    stage.close();
                } catch (IllegalArgumentException e) {
                    customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                            "Operacja utworzenia zbioru kart odpowiedzi nie powiodła się.",
                            "Powód: wprowadzona data jest nieprawidłowa.")
                            .showAndWait();
                } catch (MinimumNumberOfObjectsViolationException | IOException | UniqueViolationException e) {
                    customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                            "Operacja utworzenia zbioru kart odpowiedzi nie powiodła się.",
                            "Powód: " + e.getCause().getMessage() + ".")
                            .showAndWait();
                }
            }
        }
    }

    @FXML
    void buttonAddQuestionAnswer_onAction() {
        Question selectedQuestion = comboBoxQuestion.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null)
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja dodania odpowiedzi do pytania nie powiodła się.",
                    "Powód: nie wybrano pytania.")
                    .showAndWait();
        else {
            Set<Character> queAns = new HashSet<>(questionAnswers);
            newAnswersCardAnswersObservableList.add(new StudentQuestionAnswers(selectedQuestion.getNumber(), queAns));
            newAnswersCardAnswersObservableList.sort((a, b) -> a.getQuestionNumber() < b.getQuestionNumber()
                    ? -1 : a.getQuestionNumber().equals(b.getQuestionNumber()) ? 0 : 1);
            tableViewNewAnswersCardAnswers.setItems(newAnswersCardAnswersObservableList);
            questionAnswers.clear();
            textFieldChosenAnswers.setText("");
            comboBoxQuestionsObservableList.remove(selectedQuestion);
            comboBoxQuestion.getSelectionModel().clearSelection();
            comboBoxQuestionAnswersObservableList.clear();
        }
    }

    @FXML
    void buttonCancel_onAction() {
        Stage stage = (Stage) textFieldYear.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buttonClearChosenAnswers_onAction() {
        questionAnswers.clear();
        textFieldChosenAnswers.setText("");
    }

    @FXML
    void buttonDeleteAnswersCard_onAction() {
        if (tableViewAnswerCards.getSelectionModel().getSelectedItem() != null) {
            answersCardsObservableList.remove(tableViewAnswerCards.getSelectionModel().getSelectedItem());
            selectedAnswersCardAnswersObservableList.clear();
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja karty odpowiedzi nie powiodła się.",
                    "Powód: nie zaznaczono karty odpowiedzi.")
                    .showAndWait();
    }

    @FXML
    void buttonDeleteQuestionAnswersFromNewAnswersCard_onAction() {
        if (tableViewNewAnswersCardAnswers.getSelectionModel().getSelectedItem() != null) {
            comboBoxQuestionsObservableList.add(testTemplate.getQuestions()
                    .get(tableViewNewAnswersCardAnswers.getSelectionModel().getSelectedItem().getQuestionNumber()));
            comboBoxQuestionsObservableList.sort((a, b) -> a.getNumber() < b.getNumber() ? -1 : a.getNumber().equals(b.getNumber()) ? 0 : 1);
            newAnswersCardAnswersObservableList.remove(tableViewNewAnswersCardAnswers.getSelectionModel().getSelectedItem());
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja usunięcia odpowiedzi pytania nie powiodła się.",
                    "Powód: nie zaznaczono odpowiedzi pytania.")
                    .showAndWait();
    }

    @FXML
    void comboBoxAnswer_onAction() {
        if (comboBoxAnswer.getSelectionModel().getSelectedItem() != null) {
            textFieldChosenAnswers.setText("");
            questionAnswers.add(comboBoxAnswer.getSelectionModel().getSelectedItem().getSymbol());

            int i = 0;
            for (Character character : questionAnswers) {
                i++;
                if (questionAnswers.size() == 1)
                    textFieldChosenAnswers.setText(character.toString());
                else {
                    if (i == questionAnswers.size())
                        textFieldChosenAnswers.setText(textFieldChosenAnswers.getText() + character.toString());
                    else
                        textFieldChosenAnswers.setText(textFieldChosenAnswers.getText() + character.toString() + ", ");
                }
            }
            textFieldChosenAnswers.setText(textFieldChosenAnswers.getText() + ".");
        }
    }

    @FXML
    void comboBoxQuestion_onAction() {
        if (comboBoxQuestion.getSelectionModel().getSelectedItem() != null) {
            questionAnswers.clear();
            textFieldChosenAnswers.setText("");
            comboBoxQuestionAnswersObservableList.clear();
            comboBoxQuestionAnswersObservableList.addAll(comboBoxQuestion.getSelectionModel().getSelectedItem().getAnswers().values());
            comboBoxAnswer.setItems(comboBoxQuestionAnswersObservableList);
        }
    }

    @FXML
    void tableViewAnswerCards_onMouseClicked() {
        if (tableViewAnswerCards.getSelectionModel().getSelectedItem() != null) {
            selectedAnswersCardAnswersObservableList.clear();
            selectedAnswersCardAnswersObservableList
                    .addAll(tableViewAnswerCards.getSelectionModel().getSelectedItem().getStudentQuestionsAnswers().values());
            tableViewSelectedAnswersCardAnswers.setItems(selectedAnswersCardAnswersObservableList);
        }
    }

    private void initTableViews() {
        tableColumnNewAnswersCardAnswersQuestionNumber.setCellValueFactory(new PropertyValueFactory<>("questionNumber"));
        tableColumnNewAnswersCardQuestionAnswers.setCellValueFactory(new PropertyValueFactory<>("questionAnswers"));
        tableColumnNewAnswersCardQuestionAnswers.setCellFactory(col -> characterSetConversionToString());

        tableColumnSelectedAnswersCardAnswersQuestionNumber.setCellValueFactory(new PropertyValueFactory<>("questionNumber"));
        tableColumnSelectedAnswersCardQuestionAnswers.setCellValueFactory(new PropertyValueFactory<>("questionAnswers"));
        tableColumnSelectedAnswersCardQuestionAnswers.setCellFactory(col -> characterSetConversionToString());

        tableColumnAnswerCardsStudentIndex.setCellValueFactory(new PropertyValueFactory<>("studentIndex"));
    }

    private TableCell<StudentQuestionAnswers, Set<Character>> characterSetConversionToString() {
        return new TableCell<StudentQuestionAnswers, Set<Character>>() {
            @Override
            public void updateItem(Set<Character> symbol, boolean empty) {
                super.updateItem(symbol, empty);
                if (empty) {
                    setText(null);
                } else {
                    int i = 0;
                    String answers = "";
                    for (Character character : symbol) {
                        i++;
                        if (symbol.size() == 1)
                            answers = character.toString();
                        else {
                            if (i == symbol.size())
                                answers += character.toString();
                            else
                                answers += character.toString() + ", ";
                        }
                    }
                    if (answers.equals("-"))
                        setText("nie udzielono odpowiedzi");
                    else
                        setText(answers);
                }
            }
        };
    }
}
