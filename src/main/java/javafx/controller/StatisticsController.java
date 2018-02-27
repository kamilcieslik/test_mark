package javafx.controller;

import app.Main;
import javafx.CustomMessageBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import test_mark.parser.TestXlsParser;
import test_mark.test_result.Statistics;
import test_mark.test_result.StudentResults;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Controller
public class StatisticsController implements Initializable {
    private CustomMessageBox customMessageBox;
    private Statistics statistics;
    private ObservableList<StudentResults> studentsResultsObservableList = FXCollections.observableArrayList();

    @FXML
    private Label labelHeader, labelCourseName, labelExamName, labelNumberOfQuestions, labelExamDate,
            labelAverageNumberOfPoints, labelExamAverageOfTestMark, labelNumberOfFailedExams,
            labelAverageNumberOfUnansweredQuestions;
    @FXML
    private TableView<StudentResults> tableViewStudentResults;
    @FXML
    private TableColumn<StudentResults, String> tableColumnStudentResultsIndex;
    @FXML
    private TableColumn<StudentResults, Integer> tableColumnStudentResultsNumberOfCorrectAnswers,
            tableColumnStudentNumberOfUnansweredQuestions;
    @FXML
    private TableColumn<StudentResults, Double> tableColumnStudentResultsPercentageOfCorrectAnswers,
            tableColumnStudentResultsTestMark;
    @FXML
    private BarChart<String, Integer> barChartDistributionOfTheNumberOfStudentsByNumberOfPoints,
            barChartDistributionOfTheNumberOfPointsByQuestionNumbers, barChartDistributionOfTheNumberOfStudentsByTestMarks;

    public void setInitialStatisticsValues(Statistics statistics) {
        this.statistics = statistics;
        studentsResultsObservableList.addAll(statistics.getStudentsResults());
        tableViewStudentResults.setItems(studentsResultsObservableList);
        labelCourseName.setText(statistics.getAnswersCardsCollection().getCourseName());
        labelExamName.setText(statistics.getAnswersCardsCollection().getExamName());
        labelNumberOfQuestions.setText(String.valueOf(statistics.getAnswersCardsCollection().getNumberOfQuestions()));
        labelExamDate.setText(new SimpleDateFormat("dd-MM-yyyy")
                .format(statistics.getAnswersCardsCollection().getExamDate()));

        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        labelAverageNumberOfPoints.setText(decimalFormat.format(statistics.getAverageNumberOfPointsScoredByOneStudent()));
        labelExamAverageOfTestMark.setText(decimalFormat.format(statistics.getAverageOfTestMark()));
        labelNumberOfFailedExams.setText(String.valueOf(statistics.getNumberOfFailedExams()));
        labelAverageNumberOfUnansweredQuestions.setText(decimalFormat.format(statistics.getAverageNumberOfUnansweredQuestions()));

        initBarCharts();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences pref = Preferences.userRoot();
        labelHeader.setText(pref.get("test_mark_header",
                "Program do opracowywania wyników testów wyboru"));
        customMessageBox = new CustomMessageBox("image/icon.png");
        initTableView();
    }

    @FXML
    void buttonCancel_onAction() {
        Stage stage = (Stage) barChartDistributionOfTheNumberOfPointsByQuestionNumbers.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buttonExcelExport_onAction() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Test Mark - wybór katalogu zapisu wyników testu");
        File directory = chooser.showDialog(Main.getMainStage());
        if (directory != null) {
            String directoryPath = directory.getAbsolutePath();
            try {
                TestXlsParser testXlsParser = new TestXlsParser();
                testXlsParser.writeExamResults(statistics, directoryPath);
            } catch (IOException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja utworzenia wyników testu nie powiodła się.",
                        "Powód: " + e.getCause().getMessage() + ".")
                        .showAndWait();
            }
        }
    }

    private void initTableView() {
        tableColumnStudentResultsIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        tableColumnStudentResultsNumberOfCorrectAnswers.setCellValueFactory(new PropertyValueFactory<>("numberOfCorrectAnswers"));
        tableColumnStudentNumberOfUnansweredQuestions.setCellValueFactory(new PropertyValueFactory<>("numberOfUnansweredQuestions"));
        tableColumnStudentResultsPercentageOfCorrectAnswers.setCellValueFactory(new PropertyValueFactory<>("percentageOfCorrectAnswers"));
        tableColumnStudentResultsTestMark.setCellValueFactory(new PropertyValueFactory<>("testMark"));
    }

    @SuppressWarnings("unchecked")
    private void initBarCharts() {
        XYChart.Series firstHistSeries = new XYChart.Series();
        for (Map.Entry<Integer, Integer> entry : statistics.getDistributionOfTheNumberOfPointsByQuestionNumbers().entrySet()) {
            firstHistSeries.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }
        barChartDistributionOfTheNumberOfPointsByQuestionNumbers.getData().addAll(firstHistSeries);

        XYChart.Series secondHistSeries = new XYChart.Series();
        for (Map.Entry<Integer, Integer> entry : statistics.getDistributionOfTheNumberOfStudentsByNumberOfPoints().entrySet()) {
            secondHistSeries.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }
        barChartDistributionOfTheNumberOfStudentsByNumberOfPoints.getData().addAll(secondHistSeries);

        XYChart.Series thirdHistSeries = new XYChart.Series();
        for (Map.Entry<String, Integer> entry : statistics.getDistributionOfTheNumberOfStudentsByTestMarks().entrySet()) {
            thirdHistSeries.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        barChartDistributionOfTheNumberOfStudentsByTestMarks.getData().addAll(thirdHistSeries);
    }
}
