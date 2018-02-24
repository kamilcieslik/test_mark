package javafx_controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;
import test_mark.AnswersCardsCollection;
import test_mark.TestTemplate;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

@Controller
public class MainController implements Initializable {
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
    private TextArea textAreaDevelopResultsDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void buttonAddAnswerCardsCollections_onAction() {

    }

    @FXML
    void buttonAddTestTemplates_onAction() {

    }

    @FXML
    void buttonCheckResults_onAction() {

    }

    @FXML
    void buttonDevelopResults_onAction() {

    }

    @FXML
    void buttonReadAnswerCardsCollections_onAction() {

    }

    @FXML
    void buttonReadTestTemplates_onAction() {

    }

    @FXML
    void menuItemModifyHeader_onAction() {

    }

    @FXML
    void menuItemModifyRatingSystem_onAction() {

    }

    @FXML
    void tableViewAnswerCardsCollections_onMouseClicked() {

    }

    @FXML
    void tableViewTestTemplates_onMouseClicked() {

    }
}
