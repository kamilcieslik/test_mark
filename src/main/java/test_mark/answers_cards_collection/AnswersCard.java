package test_mark.answers_cards_collection;

import java.util.ArrayList;
import java.util.List;

public class AnswersCard {
    private String studentIndex;
    private List<List<Character>> studentAnswers;

    public AnswersCard() {
    }

    public AnswersCard(String studentIndex, List<List<Character>> studentAnswers) {
        this.studentIndex = studentIndex;
        this.studentAnswers = studentAnswers;
    }

    public String getStudentIndex() {
        return studentIndex;
    }

    public void setStudentIndex(String studentIndex) {
        this.studentIndex = studentIndex;
    }

    public List<List<Character>> getStudentQuestionsAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<List<Character>> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    public void addQuestionAnswers(List<Character> questionAnswers){
        if (studentAnswers==null)
            studentAnswers = new ArrayList<>();
        studentAnswers.add(questionAnswers);
    }

    @Override
    public String toString() {
        return "AnswersCard{" +
                "studentIndex='" + studentIndex + '\'' +
                ", studentAnswers=" + studentAnswers +
                '}';
    }
}
