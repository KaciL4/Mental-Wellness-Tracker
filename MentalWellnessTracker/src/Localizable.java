import java.util.ResourceBundle;
//All your GUI panels must implement this interface to allow the GUI class to tell them to update their text when the language changes.
public interface Localizable {
//     Updates all visible text (labels, buttons, titles) using the current ResourceBundle.
    void refreshText();
}