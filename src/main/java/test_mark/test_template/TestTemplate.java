package test_mark.test_template;

import test_mark.exception.MinimumNumberOfObjectsViolationException;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Question> questions;

    public TestTemplate() {
    }

    public TestTemplate(String courseName, String examName, Date creationDate, List<Question> questions) throws MinimumNumberOfObjectsViolationException {
        if (questions.size() < 2) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("szablon testu musi posiadać co najmniej dwa pytania");
            throw new MinimumNumberOfObjectsViolationException("Błąd ilości pytań", exceptionCause);
        }

        this.courseName = courseName;
        this.examName = examName;
        this.creationDate = creationDate;
        this.questions = questions;
        for (Question question : this.questions)
            question.setNumber(this.questions.indexOf(question) + 1);
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) throws MinimumNumberOfObjectsViolationException {
        if (questions.size() < 2) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("szablon testu musi posiadać co najmniej dwa pytania");
            throw new MinimumNumberOfObjectsViolationException("Błąd ilości pytań", exceptionCause);
        }

        this.questions = questions;
        for (Question question : this.questions)
            question.setNumber(this.questions.indexOf(question) + 1);
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
