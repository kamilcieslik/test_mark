package javafx;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TextChangeListenerMethods {
    public TextChangeListenerMethods() {

    }

    public void changeLabelText(String regex, TextField textField, Label label, String isEmpty, String doesNotFit) {
        if (textField.getText().isEmpty())
            label.setText(isEmpty);
        else if (!textField.getText().matches(regex))
            label.setText(doesNotFit);
        else
            label.setText("");
    }
}
