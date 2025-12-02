import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SignInPanel extends JPanel{
    private GUI gui;

    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton signInBtn;
    private JButton signUpLink;
    private JComboBox<String> langSelector;

    private JTextField lgnUsername;
    private JPasswordField lgnPassword;
    private JPanel contentPanel;

//    private final Map<String, Locale> supportedLocales;
    public SignInPanel(GUI gui){

        this.gui=gui;

//        this.supportedLocales = new HashMap<>();
//        supportedLocales.put("English",Locale.ENGLISH);
//        supportedLocales.put("French",Locale.FRENCH);

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBackground(new Color(240,240,240));
//      Add Language selector (top right)
//        addLanguageSelector();

//        Format Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setMaximumSize(new Dimension(500,600));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230), 1),
                BorderFactory.createEmptyBorder(40,40,40,40)
        ));
//        title
        titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0,8)));
//      subtitle
        subtitleLabel = new JLabel("Sign in to continue to your account");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);

//        username Label
        contentPanel.add(Box.createRigidArea(new Dimension(0,50)));

        usernameLabel = new JLabel("Username");
        usernameLabel.setAlignmentX((Component.CENTER_ALIGNMENT));
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        contentPanel.add(usernameLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0,6)));
//      Username input

        lgnUsername = new JTextField();
        lgnUsername.setMaximumSize((new Dimension(300, 40)));
        lgnUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lgnUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200, 200), 1),
                BorderFactory.createEmptyBorder(10,10,10,30)));
        contentPanel.add(lgnUsername);

        contentPanel.add(Box.createRigidArea(new Dimension(0,20)));
//      password label
        passwordLabel = new JLabel("Password");
        passwordLabel.setAlignmentX((Component.CENTER_ALIGNMENT));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        contentPanel.add(passwordLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0,6)));
//      Password input
        lgnPassword = new JPasswordField();
        lgnPassword.setMaximumSize((new Dimension(300, 40)));
        lgnPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lgnPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200, 200), 1),
                BorderFactory.createEmptyBorder(10,10,10,30)));
        contentPanel.add(lgnPassword);

        contentPanel.add(Box.createRigidArea(new Dimension(0,40)));
//      Login button
        signInBtn = new JButton("Login");
        signInBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInBtn.setMaximumSize(new Dimension(300, 40));
        signInBtn.setBackground(new Color(59,130,246));
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setFocusPainted(false);
        signInBtn.setFont(new Font("Arial", Font.BOLD, 15));
        signInBtn.setBorder(BorderFactory.createEmptyBorder(15,30,15,30));
        signInBtn.setBorderPainted(false);
        signInBtn.addActionListener(e -> handleLogin());
        contentPanel.add(signInBtn);

        contentPanel.add(Box.createRigidArea(new Dimension(0,40)));
//      separator line
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        contentPanel.add(separator);

        contentPanel.add(Box.createRigidArea(new Dimension(0,20)));
//      sign up button
        JPanel sighUpPanel = new JPanel();
        sighUpPanel.setBackground(Color.WHITE);
        sighUpPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
        sighUpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        sighUpPanel.add(signupLabel);

        signUpLink = new JButton("<html><u>Sign Up</u></html>");
        signUpLink.setFont(new Font("Arial", Font.BOLD, 14));
        signUpLink.setForeground(Color.BLUE);
        signUpLink.setBackground(Color.WHITE);
        signUpLink.setBorderPainted(false);
        signUpLink.setContentAreaFilled(false);
        signUpLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLink.setFocusPainted(false);
        signUpLink.addActionListener(e -> {
            clearFields();
            gui.showPanel("signup");
        });
        sighUpPanel.add(signUpLink);
        contentPanel.add(sighUpPanel);

        add(contentPanel,BorderLayout.CENTER);
    }

    private void handleLogin() {
        String username = lgnUsername.getText().trim();
        String password = new String(lgnPassword.getPassword());
        String result = gui.getController().loginUser(username, password);

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(result.startsWith("Error")){
            JOptionPane.showMessageDialog(this,result,"Login Failed",JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this,result,"Success Login",JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            gui.showPanel("moodselection");
        }
    }
    private void clearFields(){
        lgnUsername.setText("");
        lgnPassword.setText("");
    }
//    private void addLanguageSelector() {
//        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        selectorPanel.setOpaque(false);
//
//        // Populate with keys (English, French) which we will translate in refreshText()
//        String[] languages = {"English", "French"};
//        langSelector = new JComboBox<>(languages);
//
//        // Set initial selection based on the language.english key in the bundle
//        langSelector.setSelectedItem(gui.getMessages().getString("language.english"));
//
//        langSelector.addActionListener(e -> {
//            String selectedText = (String) langSelector.getSelectedItem();
//            // Map the selected text back to a Locale object
//            Locale newLocale = null;
//            if (selectedText != null) {
//                if (selectedText.equals(gui.getMessages().getString("language.english")) || selectedText.equals("English")) {
//                    newLocale = Locale.ENGLISH;
//                } else if (selectedText.equals(gui.getMessages().getString("language.french")) || selectedText.equals("Français")) {
//                    newLocale = Locale.FRENCH;
//                }
//            }
//            if (newLocale != null) {
//                gui.setCurrentLocale(newLocale);
//            }
//        });
//
//        selectorPanel.add(langSelector);
//        add(selectorPanel, BorderLayout.NORTH);
//    }
//    @Override
//    public void refreshText() {
//        ResourceBundle messages = gui.getMessages();
//
//        // Update components
//        titleLabel.setText(messages.getString("title.signIn"));
//        subtitleLabel.setText(messages.getString("subtitle.signIn"));
//        usernameLabel.setText(messages.getString("label.username"));
//        passwordLabel.setText(messages.getString("label.password"));
//        signInBtn.setText(messages.getString("button.signin"));
//
//        // Update link text
//        JLabel signUpLabel = (JLabel)((JPanel)contentPanel.getComponent(contentPanel.getComponentCount()-1)).getComponent(0);
//        signUpLabel.setText(messages.getString("link.noAccount"));
//        signUpLink.setText(messages.getString("title.signUp"));
//
//        // Update language selector options and maintain selection
//        String selectedLangKey = gui.currentLocale.getLanguage().equals("fr") ? "language.french" : "language.english";
//        String selectedLangText = messages.getString(selectedLangKey);
//
//        // Temporarily remove listener to prevent trigger loop
//        langSelector.removeAllItems();
//        langSelector.addItem(messages.getString("language.english"));
//        langSelector.addItem(messages.getString("language.french"));
//        langSelector.setSelectedItem(selectedLangText);
//    }
}
