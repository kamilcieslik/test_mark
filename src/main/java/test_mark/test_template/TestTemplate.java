package test_mark.test_template;

import test_mark.exception.MinimumNumberOfObjectsViolationException;
import test_mark.exception.UniqueViolationException;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestTemplate {
    @XmlAttribute(name = "courseName")
    private String courseName;
    @XmlAttribute(name = "examName")
    private String examName;
    @XmlAttribute(name = "creationDate")
    private Date creationDate;
    @XmlElementWrapper(name = "questions")
    @XmlElement(name = "question")
    private Map<Integer, Question> questions;

    public TestTemplate() {
    }

    public TestTemplate(String courseName, String examName, Date creationDate, List<Question> questions) throws MinimumNumberOfObjectsViolationException, UniqueViolationException {
        if (questions.size() < 2) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("szablon testu musi posiadać co najmniej dwa pytania");
            throw new MinimumNumberOfObjectsViolationException("Błąd ilości pytań", exceptionCause);
        }

        this.courseName = courseName;
        this.examName = examName;
        this.creationDate = creationDate;

        this.questions = new HashMap<>();
        for (Question question : questions) {
            if (this.questions.containsKey(question.getNumber())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("pytanie o numerze '" + question.getNumber() + "' już istnieje");
                throw new UniqueViolationException("Błąd numeru pytania", exceptionCause);
            }
            this.questions.put(question.getNumber(), question);
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Map<Integer, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) throws MinimumNumberOfObjectsViolationException, UniqueViolationException {
        if (questions.size() < 2) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("szablon testu musi posiadać co najmniej dwa pytania");
            throw new MinimumNumberOfObjectsViolationException("Błąd ilości pytań", exceptionCause);
        }

        Map<Integer, Question> tmpQuestions = new HashMap<>();
        for (Question question : questions) {
            if (tmpQuestions.containsKey(question.getNumber())) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("pytanie o numerze '" + question.getNumber() + "' już istnieje");
                throw new UniqueViolationException("Błąd numeru pytania", exceptionCause);
            }
            tmpQuestions.put(question.getNumber(), question);
        }

        this.questions = tmpQuestions;
    }

    @Override
    public String toString() {
        return "TestTemplate{" +
                "courseName='" + courseName + '\'' +
                ", examName='" + examName + '\'' +
                ", creationDate=" + creationDate +
                ", questions=" + questions +
                '}';
    }
}
