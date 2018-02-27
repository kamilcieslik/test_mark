package test_mark.test_template;

import test_mark.exception.CorrectAnswersViolationException;
import test_mark.exception.MinimumNumberOfObjectsViolationException;
import test_mark.exception.UniqueViolationException;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Question {
    @XmlAttribute(name = "questionNumber")
    private Integer number;
    @XmlAttribute(name = "questionText")
    private String content;
    @XmlAttribute(name = "numberOfCorrectAnswers")
    private Integer amountOfCorrectAnswers = 0;
    @XmlElementWrapper(name = "answers")
    @XmlElement(name = "answer")
    private Map<Character, Answer> answers;

    public Question() {

    }

    public Question(String content, List<Answer> answers) throws UniqueViolationException, CorrectAnswersViolationException, MinimumNumberOfObjectsViolationException {
        if (answers.size() < 2) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("pytanie musi posiadać co najmniej dwie odpowiedzi");
            throw new MinimumNumberOfObjectsViolationException("Błąd ilości odpowiedzi", exceptionCause);
        }

        this.content = content;
        this.answers = new HashMap<>();
        for (Answer answer : answers) {
            if (this.answers.containsKey(answer.getSymbol())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("odpowiedź o symbolu '" + answer.getSymbol() + "' już istnieje");
                throw new UniqueViolationException("Błąd symbolu odpowiedzi", exceptionCause);
            }
            this.answers.put(answer.getSymbol(), answer);
            if (answer.getCorrect())
                amountOfCorrectAnswers++;
        }

        if (amountOfCorrectAnswers == 0) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("pytanie musi posiadać co najmniej jedną poprawną odpowiedź");
            throw new CorrectAnswersViolationException("Błąd ilości poprawnych odpowiedzi", exceptionCause);
        }
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAmountOfCorrectAnswers() {
        return amountOfCorrectAnswers;
    }

    public void setAmountOfCorrectAnswers(Integer amountOfCorrectAnswers) {
        this.amountOfCorrectAnswers = amountOfCorrectAnswers;
    }

    public Map<Character, Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) throws UniqueViolationException, CorrectAnswersViolationException, MinimumNumberOfObjectsViolationException {
        if (answers.size() < 2) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("pytanie musi posiadać co najmniej dwie odpowiedzi");
            throw new MinimumNumberOfObjectsViolationException("Błąd ilości odpowiedzi", exceptionCause);
        }

        amountOfCorrectAnswers = 0;
        Map<Character, Answer> tmpAnswers = new HashMap<>();
        for (Answer answer : answers) {
            if (tmpAnswers.containsKey(answer.getSymbol())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("odpowiedź o symbolu '" + answer.getSymbol() + "' już istnieje");
                throw new UniqueViolationException("Błąd symbolu odpowiedzi", exceptionCause);
            }
            tmpAnswers.put(answer.getSymbol(), answer);
            if (answer.getCorrect())
                amountOfCorrectAnswers++;
        }

        if (amountOfCorrectAnswers == 0) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("pytanie musi posiadać co najmniej jedną poprawną odpowiedź");
            throw new CorrectAnswersViolationException("Błąd ilości poprawnych odpowiedzi", exceptionCause);
        }

        this.answers = tmpAnswers;
    }

    @Override
    public String toString() {
        return String.valueOf(number) + ". " + content;
    }
}
