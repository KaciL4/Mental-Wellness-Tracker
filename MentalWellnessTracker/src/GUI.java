import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GUI extends JPanel{
    private Controller controller;
    private  JPanel cardPanel;
    private CardLayout cardLayout;
    private MainPanel mainPanel;

//    For I18N
//    private Locale currentLocale;
//    private ResourceBundle messages;
//    private final Map<String, Localizable> localizablePanels; //Map to hold all panels


    public GUI(){
        controller=new Controller();
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        mainPanel = new MainPanel(this);
//        -----------------------------------------------------------------
//        //Initialize Locale and ResourceBundle to English by default
//        this.localizablePanels = new HashMap<>();
//        setCurrentLocale(Locale.ENGLISH);
//
////        Initialize Panels and add them to the map and card panel
//        MainPanel mainPanel = new MainPanel(this);
//        SignUpPanel signUpPanel = new SignUpPanel(this); // Assuming this panel exists
//        SignInPanel signInPanel = new SignInPanel(this); // Assuming this panel exists
//        AccountManagerPanel accountPanel = new AccountManagerPanel(this);
//        ChangePasswordPanel changePassPanel = new ChangePasswordPanel(this);
//
//        localizablePanels.put("main", mainPanel);
//        localizablePanels.put("signup", signUpPanel);
//        localizablePanels.put("signin", signInPanel);
//        localizablePanels.put("account", accountPanel);
//        localizablePanels.put("changepassword", changePassPanel);
//
//        for (Map.Entry<String, Localizable> entry : localizablePanels.entrySet()) {
//            cardPanel.add((JPanel)entry.getValue(), entry.getKey());
//        }
//        ----------------------------------------------------------------------------
        cardPanel.add(mainPanel,"main");
        cardPanel.add(new SignUpPanel(this),"signup");
        cardPanel.add(new SignInPanel(this),"signin");
        cardPanel.add(new MoodPanel(this), "moodselection");
        cardPanel.add(new EmotionSelectionPanel(this), "emotionselection");
        cardPanel.add(new AccountManagerPanel(this),"account");
        cardPanel.add(new ChangePasswordPanel(this),"changepassword");
        cardPanel.add(new GoalPanel(this),"goal");
        cardPanel.add(new HabitLogPanel(this),"habitlog");

        add(cardPanel,BorderLayout.CENTER);
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
    }

//FOR I18N
//    public void setCurrentLocale(Locale newLocale) {
//        if (this.currentLocale != null&& this.currentLocale.equals(newLocale)) {
//            return; // Avoid unnecessary refresh
//        }
//        this.currentLocale = newLocale;
//        try {
//            this.messages= ResourceBundle.getBundle("Messages", newLocale);
//        } catch (MissingResourceException e) {
//            System.err.println("Could not find resource bundle for locale: " + newLocale + ". Falling back to English.");
//            this.messages =ResourceBundle.getBundle("Messages", Locale.ENGLISH);
//        }
//        refreshAllPanels();
//        // Update JFrame title
//        JFrame frame=(JFrame) SwingUtilities.getWindowAncestor(this);
//        if (frame !=null) {
//            frame.setTitle(messages.getString("title.mainFrame"));
//        }
//    }
//    public ResourceBundle getMessages() {
//        return messages;
//    }
//    private void refreshAllPanels() {
//        for (Localizable panel :localizablePanels.values()) {
//            panel.refreshText();
//        }
//    }
//    public void showPanel(String panel){
//        cardLayout.show(cardPanel,panel);
//        if (panel.equals("account")){
//            ((AccountManagerPanel)localizablePanels.get("account")).updateFields();
//        }
//    }

    public Controller getController(){
        return controller;
    }
}
