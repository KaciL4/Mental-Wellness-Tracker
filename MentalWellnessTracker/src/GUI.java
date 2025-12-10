import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GUI extends JPanel{
    private Controller controller;
    private  JPanel cardPanel;
    private CardLayout cardLayout;
    private MainPanel mainPanel;

    // For I18N
    private Locale currentLocale;
    private ResourceBundle messages;
    private final Map<String, Localizable> localizablePanels; //Map to hold all panels


    public GUI(){
        controller=new Controller();
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        //Initialize Map
        this.localizablePanels = new HashMap<>();

        //Initialize Locale and ResourceBundle to English by default
        setCurrentLocale(Locale.ENGLISH);

        // Initialize Panels and add them to the map and card panel
        mainPanel = new MainPanel(this);
        SignUpPanel signUpPanel = new SignUpPanel(this); // Assuming this panel exists
        SignInPanel signInPanel = new SignInPanel(this); // Assuming this panel exists
        AccountManagerPanel accountPanel = new AccountManagerPanel(this);
        ChangePasswordPanel changePassPanel = new ChangePasswordPanel(this);
        MoodPanel moodPanel = new MoodPanel(this);
        EmotionSelectionPanel emotionSelectionPanel = new EmotionSelectionPanel(this);
        GoalPanel goalPanel = new GoalPanel(this);
        HabitLogPanel habitLogPanel = new HabitLogPanel(this);

        localizablePanels.put("main", mainPanel);
        localizablePanels.put("signup", signUpPanel);
        localizablePanels.put("signin", signInPanel);
        localizablePanels.put("account", accountPanel);
        localizablePanels.put("changepassword", changePassPanel);
        localizablePanels.put("moodselection", moodPanel);
        localizablePanels.put("emotionselection", emotionSelectionPanel);
        localizablePanels.put("goal", goalPanel);
        localizablePanels.put("habitlog", habitLogPanel);

        for (Map.Entry<String, Localizable> entry : localizablePanels.entrySet()) {
            cardPanel.add((JPanel)entry.getValue(), entry.getKey());
        }

        add(cardPanel, BorderLayout.CENTER);
        showPanel("signin");

    }
    public void showPanel(String panel){
        cardLayout.show(cardPanel,panel);

        if(panel.equals("main")){
            mainPanel.refreshData();
        }

        if(panel.equals("goal")){
            Component[] components = cardPanel.getComponents();
            for(Component c : components){
                if(c instanceof GoalPanel){
                    ((GoalPanel)c).refreshGoals();
                    break;
                }
            }
        }
        if(panel.equals("habitlog")){
            Component[] components = cardPanel.getComponents();
            for(Component c : components){
                if(c instanceof HabitLogPanel){
                    ((HabitLogPanel)c).refreshHabits();
                    break;
                }
           }
        }
        if(panel.equals("account")){
            ((AccountManagerPanel)localizablePanels.get("account")).updateFields();
        }
    }

    //FOR I18N
    public void setCurrentLocale(Locale newLocale) {
        if (this.currentLocale != null&& this.currentLocale.equals(newLocale)) {
            return; // Avoid unnecessary refresh
        }
        this.currentLocale = newLocale;
        try {
            this.messages = ResourceBundle.getBundle("MessagesBundle", newLocale);
        } catch (MissingResourceException e) {
            System.err.println("Could not find resource bundle for locale: " + newLocale + ". Falling back to English.");
            this.messages = ResourceBundle.getBundle("MessagesBundle", Locale.ENGLISH);
        }

        applyDialogLocalization();
        controller.setMessages(this.messages);
        refreshAllPanels();

        // Update JFrame title
        JFrame frame=(JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame !=null) {
            frame.setTitle(messages.getString("title.mainFrame"));
        }
    }
    public ResourceBundle getMessages() {
        return messages;
    }
    private void applyDialogLocalization() {
        UIManager.put("OptionPane.okButtonText", messages.getString("dialog.ok"));
        UIManager.put("OptionPane.cancelButtonText", messages.getString("dialog.cancel"));
        UIManager.put("OptionPane.yesButtonText", messages.getString("dialog.yes"));
        UIManager.put("OptionPane.noButtonText", messages.getString("dialog.no"));
        UIManager.put("OptionPane.closeButtonText", messages.getString("dialog.close"));
    }

    private void refreshAllPanels() {
        for (Localizable panel :localizablePanels.values()) {
            panel.refreshText();
        }
    }
    public Controller getController(){
        return controller;
    }
}
