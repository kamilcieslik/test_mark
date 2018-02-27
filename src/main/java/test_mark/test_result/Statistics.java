package test_mark.test_result;

import test_mark.answers_cards_collection.AnswersCard;
import test_mark.answers_cards_collection.AnswersCardsCollection;
import test_mark.answers_cards_collection.StudentQuestionAnswers;
import test_mark.exception.MatchViolationException;
import test_mark.exception.RatingSystemViolationException;
import test_mark.test_template.Answer;
import test_mark.test_template.Question;
import test_mark.test_template.TestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private double marksPercentages[];
    private TestTemplate testTemplate;
    private AnswersCardsCollection answersCardsCollection;
    private List<StudentResults> studentsResults;

    private Integer numberOfQuestions, numberOfFailedExams;
    private Double averageNumberOfPointsScoredByOneStudent, averageNumberOfUnansweredQuestions, averageOfTestMark;

    private Map<String, Integer> distributionOfTheNumberOfStudentsByTestMarks;
    private Map<Integer, Integer> distributionOfTheNumberOfPointsByQuestionNumbers, distributionOfTheNumberOfStudentsByNumberOfPoints;

    public Statistics(TestTemplate testTemplate, AnswersCardsCollection answersCardsCollection, double marksPercentages[]) throws MatchViolationException, RatingSystemViolationException {
        if (!testTemplate.getExamName().equals(answersCardsCollection.getExamName()) ||
                !testTemplate.getCourseName().equals(answersCardsCollection.getCourseName()) ||
                testTemplate.getQuestions().size() != answersCardsCollection.getNumberOfQuestions()) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("podstawowe informacje identyfikujące szablon testu i zbiór kart " +
                    "odpowiedzi nie są zgodne");
            throw new MatchViolationException("Błąd dopasowania szablonu testów i odpowiedzi", exceptionCause);
        }

        if (marksPercentages.length != 6 || (!(marksPercentages[1] > marksPercentages[0] && marksPercentages[2] > marksPercentages[1]
                && marksPercentages[3] > marksPercentages[2] && marksPercentages[4] > marksPercentages[3]
                && marksPercentages[5] > marksPercentages[4]))) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("należy przekazać progi procentowe dla 5 ocen, przy czym muszą one " +
                    "posiadać wartości rosnące.");
            throw new RatingSystemViolationException("Błąd wartości systemu oceniania", exceptionCause);
        }

        this.testTemplate = testTemplate;
        this.answersCardsCollection = answersCardsCollection;
        this.numberOfQuestions = answersCardsCollection.getNumberOfQuestions();
        this.marksPercentages = marksPercentages;
        studentsResults = new ArrayList<>();
        distributionOfTheNumberOfPointsByQuestionNumbers = new HashMap<>();
        distributionOfTheNumberOfStudentsByTestMarks = new HashMap<>();
        distributionOfTheNumberOfStudentsByNumberOfPoints = new HashMap<>();
        doCalculations();
    }

    public AnswersCardsCollection getAnswersCardsCollection() {
        return answersCardsCollection;
    }

    public List<StudentResults> getStudentsResults() {
        return studentsResults;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public Integer getNumberOfFailedExams() {
        return numberOfFailedExams;
    }

    public Double getAverageNumberOfUnansweredQuestions() {
        return averageNumberOfUnansweredQuestions;
    }

    public Double getAverageNumberOfPointsScoredByOneStudent() {
        return averageNumberOfPointsScoredByOneStudent;
    }

    public Double getAverageOfTestMark() {
        return averageOfTestMark;
    }

    public Map<String, Integer> getDistributionOfTheNumberOfStudentsByTestMarks() {
        return distributionOfTheNumberOfStudentsByTestMarks;
    }

    public Map<Integer, Integer> getDistributionOfTheNumberOfPointsByQuestionNumbers() {
        return distributionOfTheNumberOfPointsByQuestionNumbers;
    }

    public Map<Integer, Integer> getDistributionOfTheNumberOfStudentsByNumberOfPoints() {
        return distributionOfTheNumberOfStudentsByNumberOfPoints;
    }

    private void doCalculations() {
        for (AnswersCard answersCard : answersCardsCollection.getAnswersCards().values()) {
            Integer numberOfCorrectQuestionAnswers = 0;
            Integer numberOfUnansweredQuestions = 0;
            Integer amountOfCorrectAnswersInQuestion;
            Integer numberOfCorrectAnswersForOneQuestion;
            Map<Integer, Question> testTemplateQuestions = testTemplate.getQuestions();

            for (StudentQuestionAnswers studentQuestionAnswers : answersCard.getStudentQuestionsAnswers().values()) {
                amountOfCorrectAnswersInQuestion = testTemplateQuestions.get(studentQuestionAnswers.getQuestionNumber()).getAmountOfCorrectAnswers();
                Map<Character, Answer> testTemplateQuestionAnswers = testTemplateQuestions.get(studentQuestionAnswers.getQuestionNumber()).getAnswers();
                numberOfCorrectAnswersForOneQuestion = 0;
                for (Character character : studentQuestionAnswers.getQuestionAnswers())
                    if (character.equals('-'))
                        numberOfUnansweredQuestions++;
                    else {
                        if (testTemplateQuestionAnswers.get(character).getCorrect())
                            numberOfCorrectAnswersForOneQuestion++;
                        else
                            numberOfCorrectAnswersForOneQuestion--;
                    }

                if (numberOfCorrectAnswersForOneQuestion.equals(amountOfCorrectAnswersInQuestion)) {
                    numberOfCorrectQuestionAnswers++;
                    distributionOfTheNumberOfPointsByQuestionNumbers.merge(studentQuestionAnswers.getQuestionNumber(), 1, (a, b) -> a + b);
                }
            }

            Double percentageOfCorrectAnswers = ((double) numberOfCorrectQuestionAnswers / (double) numberOfQuestions) * 100;
            studentsResults.add(new StudentResults(answersCard.getStudentIndex(), percentageOfCorrectAnswers,
                    numberOfCorrectQuestionAnswers, numberOfUnansweredQuestions, calculateTestMark(percentageOfCorrectAnswers)));

            this.numberOfFailedExams = 0;
            this.averageNumberOfPointsScoredByOneStudent = 0.0;
            this.averageNumberOfUnansweredQuestions = 0.0;
            this.averageOfTestMark = 0.0;
            for (StudentResults studentResults : studentsResults) {
                if (studentResults.getTestMark() == 2.0)
                    this.numberOfFailedExams++;
                this.averageNumberOfPointsScoredByOneStudent += studentResults.getNumberOfCorrectAnswers();
                if (studentResults.getNumberOfUnansweredQuestions() != null)
                    this.averageNumberOfUnansweredQuestions += studentResults.getNumberOfUnansweredQuestions();
                this.averageOfTestMark += studentResults.getTestMark();
            }
            this.averageNumberOfPointsScoredByOneStudent /= (double) studentsResults.size();
            this.averageNumberOfUnansweredQuestions /= (double) studentsResults.size();
            this.averageOfTestMark /= (double) studentsResults.size();

            calculateDistributionOfStudentsByNumberOfPoints();
            calculateDistributionOfTheNumberOfStudentsByTestMarks();
        }
    }

    private Double calculateTestMark(Double percentageOfCorrectAnswers) {
        Double testMark;
        if (percentageOfCorrectAnswers >= marksPercentages[5])
            testMark = 5.5;
        else if (percentageOfCorrectAnswers >= marksPercentages[4])
            testMark = 5.0;
        else if (percentageOfCorrectAnswers >= marksPercentages[3])
            testMark = 4.5;
        else if (percentageOfCorrectAnswers >= marksPercentages[2])
            testMark = 4.0;
        else if (percentageOfCorrectAnswers >= marksPercentages[1])
            testMark = 3.5;
        else if (percentageOfCorrectAnswers >= marksPercentages[0])
            testMark = 3.0;
        else
            testMark = 2.0;
        return testMark;
    }

    private void calculateDistributionOfStudentsByNumberOfPoints() {
        for (int i = 0; i <= numberOfQuestions; i++)
            distributionOfTheNumberOfStudentsByNumberOfPoints.put(i, 0);
        for (StudentResults studentResults : studentsResults)
            distributionOfTheNumberOfStudentsByNumberOfPoints.put(studentResults.getNumberOfCorrectAnswers(),
                    distributionOfTheNumberOfStudentsByNumberOfPoints.get(studentResults.getNumberOfCorrectAnswers()) + 1);
    }

    private void calculateDistributionOfTheNumberOfStudentsByTestMarks() {
        distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(2.0), 0);
        distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(3.0), 0);
        distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(3.5), 0);
        distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(4.0), 0);
        distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(4.5), 0);
        distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(5.0), 0);
        distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(5.5), 0);
        for (StudentResults studentResults : studentsResults)
            distributionOfTheNumberOfStudentsByTestMarks.put(String.valueOf(studentResults.getTestMark()),
                    distributionOfTheNumberOfStudentsByTestMarks.get(String.valueOf(studentResults.getTestMark())) + 1);
    }

    public String simpleFormattedStudentsResults() {
        StringBuilder result = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        result.append("Wyniki kart odpowiedzi:\n");
        int i = 0;
        for (StudentResults studentResults : studentsResults) {
            i++;
            result.append("Indeks: ").append(studentResults.getIndex()).append("\n");
            result.append("Il. popr. odp.: ").append(studentResults.getNumberOfCorrectAnswers()).append(" / ").append(numberOfQuestions).append("\n");
            result.append("Ilość pytań na które nie udzielono odp.: ").append(studentResults.getNumberOfUnansweredQuestions()).append("\n");
            result.append("Procent uzyskanych punktów: ").append(decimalFormat.format(studentResults.getPercentageOfCorrectAnswers())).append(" %\n");
            result.append("Ocena: ").append(studentResults.getTestMark()).append("\n");
            if (i != studentsResults.size())
                result.append("\n");
        }

        result.append("\nRozkład sumy uzyskanych przez studentów punktów wg numerów pytań:\n");
        for (Map.Entry<Integer, Integer> entry : distributionOfTheNumberOfPointsByQuestionNumbers.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        result.append("\nRozkład ilości studentów wg ilości uzyskanych punktów:\n");
        for (Map.Entry<Integer, Integer> entry : distributionOfTheNumberOfStudentsByNumberOfPoints.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        result.append("\nRozkład ilości studentów wg ocen:\n");
        for (Map.Entry<String, Integer> entry : distributionOfTheNumberOfStudentsByTestMarks.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        result.append("\nLiczba prac niezaliczonych: ").append(numberOfFailedExams).append("\n");
        result.append("\nSrednia liczba punktów zdobytych przez studenta: ")
                .append(averageNumberOfPointsScoredByOneStudent).append("\n");
        result.append("\nSrednia liczba nieudzielonych odpowiedzi przez studenta: ")
                .append(averageNumberOfUnansweredQuestions).append("\n");

        return String.valueOf(result);
    }
}
