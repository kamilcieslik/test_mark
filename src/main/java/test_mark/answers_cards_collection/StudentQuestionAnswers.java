package test_mark.answers_cards_collection;

import java.util.HashSet;
import java.util.Set;

public class StudentQuestionAnswers {
    private Integer questionNumber;
    private Set<Character> questionAnswers;

    public StudentQuestionAnswers() {
    }

    public StudentQuestionAnswers(Integer questionNumber, Set<Character> questionAnswers) {
        this.questionNumber = questionNumber;
        this.questionAnswers = questionAnswers;
        if (this.questionAnswers.size() == 0)
            this.questionAnswers.add('-');
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Set<Character> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(Set<Character> questionAnswers) {
        this.questionAnswers = questionAnswers;
        if (this.questionAnswers.size() == 0)
            this.questionAnswers.add('-');
    }

    public void addQuestionAnswer(Character character) {
        if (questionAnswers == null)
            questionAnswers = new HashSet<>();
        questionAnswers.add(character);
    }

    @Override
    public String toString() {
        return "StudentQuestionAnswers{" +
                "questionNumber=" + questionNumber +
                ", questionAnswers=" + questionAnswers +
                '}';
    }
}
