package test_mark.answers_cards_collection;

import test_mark.exception.UniqueViolationException;

import java.util.*;

public class AnswersCardsCollection {
    private String courseName;
    private String examName;
    private Date examDate;
    private Integer numberOfQuestions;
    private Map<String, AnswersCard> answersCards;

    public AnswersCardsCollection() {
    }

    public AnswersCardsCollection(String courseName, String examName, Date examDate, List<AnswersCard> answersCards) throws UniqueViolationException {
        this.courseName = courseName;
        this.examName = examName;
        this.examDate = examDate;
        this.answersCards = new HashMap<>();
        for (AnswersCard answersCard : answersCards) {
            if (this.answersCards.containsKey(answersCard.getStudentIndex())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("istnieje już karta odpowiedzi dla studenta o numerze indeksu " + answersCard.getStudentIndex());
                throw new UniqueViolationException("Błąd indeksu studenta", exceptionCause);
            }
            this.answersCards.put(answersCard.getStudentIndex(), answersCard);
        }
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public Map<String, AnswersCard> getAnswersCards() {
        return answersCards;
    }

    public void setAnswersCards(List<AnswersCard> answersCards) throws UniqueViolationException {
        Map<String, AnswersCard> tmpAnswersCards = new HashMap<>();
        for (AnswersCard answersCard : answersCards) {
            if (tmpAnswersCards.containsKey(answersCard.getStudentIndex())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("istnieje już karta odpowiedzi dla studenta o numerze indeksu " + answersCard.getStudentIndex());
                throw new UniqueViolationException("Błąd indeksu studenta", exceptionCause);
            }
            tmpAnswersCards.put(answersCard.getStudentIndex(), answersCard);
        }

        this.answersCards=tmpAnswersCards;
    }

    public void addAnswersCard(AnswersCard answersCard) throws UniqueViolationException {
        if (answersCards == null)
            answersCards = new HashMap<>();

        if (answersCards.containsKey(answersCard.getStudentIndex())) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("istnieje już karta odpowiedzi dla studenta o numerze indeksu " + answersCard.getStudentIndex());
            throw new UniqueViolationException("Błąd indeksu studenta", exceptionCause);
        }

        answersCards.put(answersCard.getStudentIndex(), answersCard);
    }

    public void deleteAnswersCard(AnswersCard answerCard) {
        answersCards.remove(answerCard);
    }

    @Override
    public String toString() {
        return "AnswersCardsCollection{" +
                "courseName='" + courseName + '\'' +
                ", examName='" + examName + '\'' +
                ", examDate=" + examDate +
                ", answersCards=" + answersCards +
                ", numberOfQuestions=" + numberOfQuestions +
                '}';
    }
}
