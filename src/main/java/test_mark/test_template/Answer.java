package test_mark.test_template;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Answer {
    @XmlAttribute(name = "answerSymbol")
    private Character symbol;
    @XmlAttribute(name = "answerText")
    private String content;
    @XmlAttribute(name = "answerCorrect")
    private Boolean correct;

    public Answer() {
    }

    public Answer(Character symbol, String content, Boolean correct) {
        this.symbol = symbol;
        this.content = content;
        this.correct = correct;
    }

    public Character getSymbol() {
        return symbol;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "symbol=" + symbol +
                ", content='" + content + '\'' +
                ", correct=" + correct +
                '}';
    }
}
