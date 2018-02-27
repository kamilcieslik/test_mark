package javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import test_mark.answers_cards_collection.AnswersCard;
import test_mark.answers_cards_collection.AnswersCardsCollection;
import test_mark.answers_cards_collection.StudentQuestionAnswers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.Preferences;

@Controller
public class ShowAnswersCardsCollectionController implements Initializable {
    private ObservableList<AnswersCard> answersCardsObservableList = FXCollections.observableArrayList();
    private ObservableList<StudentQuestionAnswers> selectedAnswersCardAnswersObservableList = FXCollections.observableArrayList();

    @FXML
    private Label labelHeader, labelCourseName, labelExamName, labelNumberOfQuestions, labelExamDate;
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


    public void setInitialAnswersCardsCollectionValues(AnswersCardsCollection answersCardsCollection) {
        labelExamName.setText(answersCardsCollection.getExamName());
        labelCourseName.setText(answersCardsCollection.getCourseName());
        labelNumberOfQuestions.setText(String.valueOf(answersCardsCollection.getNumberOfQuestions()));
        labelExamDate.setText(new SimpleDateFormat("dd-MM-yyyy")
                .format(answersCardsCollection.getExamDate()));

        answersCardsObservableList.addAll(answersCardsCollection.getAnswersCards().values());
        tableViewAnswerCards.setItems(answersCardsObservableList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences pref = Preferences.userRoot();
        labelHeader.setText(pref.get("test_mark_header",
                "Program do opracowywania wyników testów wyboru"));
        initTableViews();
    }

    @FXML
    void buttonCancel_onAction() {
        Stage stage = (Stage) tableViewSelectedAnswersCardAnswers.getScene().getWindow();
        stage.close();
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
                    String answers = "";
                    int i = 0;
                    for (Character character : symbol) {
                        i++;
                        if (symbol.size() == 1)
                            answers = character.toString();
                        else {
                            if (i < symbol.size())
                                answers += character.toString() + ", ";
                            else
                                answers += character.toString();
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
