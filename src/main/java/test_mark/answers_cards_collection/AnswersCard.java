package test_mark.answers_cards_collection;

import test_mark.exception.UniqueViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswersCard {
    private String studentIndex;
    private Map<Integer, StudentQuestionAnswers> studentAnswers;

    public AnswersCard() {
    }

    public AnswersCard(String studentIndex, List<StudentQuestionAnswers> studentAnswers) throws UniqueViolationException {
        this.studentIndex = studentIndex;

        this.studentAnswers = new HashMap<>();
        for (StudentQuestionAnswers studentQuestionAnswers : studentAnswers) {
            if (this.studentAnswers.containsKey(studentQuestionAnswers.getQuestionNumber())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("pytanie o numerze '" + studentQuestionAnswers.getQuestionNumber() + "' już istnieje");
                throw new UniqueViolationException("Błąd numeru pytania", exceptionCause);
            }
            this.studentAnswers.put(studentQuestionAnswers.getQuestionNumber(), studentQuestionAnswers);
        }
    }

    public String getStudentIndex() {
        return studentIndex;
    }

    public void setStudentIndex(String studentIndex) {
        this.studentIndex = studentIndex;
    }

    public Map<Integer, StudentQuestionAnswers> getStudentQuestionsAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<StudentQuestionAnswers> studentAnswers) throws UniqueViolationException {
        Map<Integer, StudentQuestionAnswers> tmpStudentAnswers = new HashMap<>();
        for (StudentQuestionAnswers studentQuestionAnswers : studentAnswers) {
            if (tmpStudentAnswers.containsKey(studentQuestionAnswers.getQuestionNumber())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("pytanie o numerze '" + studentQuestionAnswers.getQuestionNumber() + "' już istnieje");
                throw new UniqueViolationException("Błąd numeru pytania", exceptionCause);
            }
            tmpStudentAnswers.put(studentQuestionAnswers.getQuestionNumber(), studentQuestionAnswers);
        }

        this.studentAnswers = tmpStudentAnswers;
    }

    public void addQuestionAnswers(StudentQuestionAnswers studentQuestionAnswers) throws UniqueViolationException {
        if (studentAnswers == null) {
            studentAnswers = new HashMap<>();
            studentAnswers.put(studentQuestionAnswers.getQuestionNumber(), studentQuestionAnswers);
        } else {
            if (studentAnswers.containsKey(studentQuestionAnswers.getQuestionNumber())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("pytanie o numerze '" + studentQuestionAnswers.getQuestionNumber() + "' już istnieje");
                throw new UniqueViolationException("Błąd numeru pytania", exceptionCause);
            }
            studentAnswers.put(studentQuestionAnswers.getQuestionNumber(), studentQuestionAnswers);
        }
    }

    @Override
    public String toString() {
        return "AnswersCard{" +
                "studentIndex='" + studentIndex + '\'' +
                ", studentAnswers=" + studentAnswers +
                '}';
    }
}
