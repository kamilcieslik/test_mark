package test_mark.test_result;

public class StudentResults {
    private String index;
    private Double percentageOfCorrectAnswers;
    private Integer numberOfCorrectAnswers;
    private Integer numberOfUnansweredQuestions;
    private Double testMark;

    StudentResults(String index, Double percentageOfCorrectAnswers, Integer numberOfCorrectAnswers, Integer numberOfUnansweredQuestions, Double testMark) {
        this.index = index;
        this.percentageOfCorrectAnswers = percentageOfCorrectAnswers;
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
        this.numberOfUnansweredQuestions = numberOfUnansweredQuestions;
        this.testMark = testMark;
    }

    public String getIndex() {
        return index;
    }

    public Double getPercentageOfCorrectAnswers() {
        return percentageOfCorrectAnswers;
    }

    public Integer getNumberOfCorrectAnswers() {
        return numberOfCorrectAnswers;
    }

    public Integer getNumberOfUnansweredQuestions() {
        return numberOfUnansweredQuestions;
    }

    public Double getTestMark() {
        return testMark;
    }
}
