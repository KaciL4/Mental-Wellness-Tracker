import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SignInPanel extends JPanel implements Localizable{
    private GUI gui;

    private JLabel titleLabel, subtitleLabel, usernameLabel, passwordLabel, langLabel, signupLabel;
    private JButton signInBtn, signUpLink;
    private JComboBox<String> langSelector;

    private JTextField lgnUsername;
    private JPasswordField lgnPassword;
    private JPanel contentPanel;

    public SignInPanel(GUI gui) {
        this.gui = gui;
        ResourceBundle messages = gui.getMessages();

        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        addLanguageSelector();

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        centerWrapper.add(Box.createVerticalStrut(80));

        // Card Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setMaximumSize(new Dimension(500, 700));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Subtitle
        subtitleLabel = new JLabel("Sign in to continue to your account");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);

        // Username Label
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        usernameLabel = new JLabel("Username");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        contentPanel.add(usernameLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        // Username Input
        lgnUsername = new JTextField();
        lgnUsername.setMaximumSize(new Dimension(300, 40));
        lgnUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lgnUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 30)
        ));
        contentPanel.add(lgnUsername);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password Label
        passwordLabel = new JLabel("Password");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        contentPanel.add(passwordLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        // Password Input
        lgnPassword = new JPasswordField();
        lgnPassword.setMaximumSize(new Dimension(300, 40));
        lgnPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lgnPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 30)
        ));
        contentPanel.add(lgnPassword);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Login Button
        signInBtn = new JButton("Login");
        signInBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInBtn.setMaximumSize(new Dimension(300, 40));
        signInBtn.setBackground(new Color(59, 130, 246));
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setFocusPainted(false);
        signInBtn.setFont(new Font("Arial", Font.BOLD, 15));
        signInBtn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        signInBtn.setBorderPainted(false);
        signInBtn.addActionListener(e -> handleLogin());
        contentPanel.add(signInBtn);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        contentPanel.add(separator);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Sign Up Panel
        JPanel sighUpPanel = new JPanel();
        sighUpPanel.setBackground(Color.WHITE);
        sighUpPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        sighUpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        signupLabel = new JLabel(messages.getString("link.noAccount"));
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

        centerWrapper.add(contentPanel);

        centerWrapper.add(Box.createVerticalGlue());

        add(centerWrapper, BorderLayout.CENTER);

        refreshText();
    }



    private void handleLogin() {
        ResourceBundle messages = gui.getMessages();
        String username = lgnUsername.getText().trim();
        String password = new String(lgnPassword.getPassword());
        String result = gui.getController().loginUser(username, password);

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, messages.getString("err.emptyFields"),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(result.startsWith("Error")){
            JOptionPane.showMessageDialog(this,result,messages.getString("d.loginFailed"),JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this,result,messages.getString("d.loginSuccess"),JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            gui.showPanel("moodselection");
        }
    }
    private void clearFields(){
        lgnUsername.setText("");
        lgnPassword.setText("");
    }

    private void addLanguageSelector() {
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        selectorPanel.setBackground(new Color(240, 240, 240));
        selectorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        langLabel = new JLabel("Language: ");
        langLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        selectorPanel.add(langLabel);

        langSelector = new JComboBox<>();
        langSelector.setFont(new Font("Arial", Font.PLAIN, 13));

        langSelector.addActionListener(e -> {
            if (langSelector.getItemCount() == 0) return;

            String selected = (String) langSelector.getSelectedItem();
            if (selected == null) return;

            ResourceBundle messages = gui.getMessages();
            Locale newLocale = null;

            if (selected.equals(messages.getString("language.french")) || selected.equals("Français")) {
                newLocale = Locale.FRENCH;
            }
            else if (selected.equals(messages.getString("language.english")) || selected.equals("English")) {
                newLocale = Locale.ENGLISH;
            }

            if (newLocale != null && !gui.getMessages().getLocale().equals(newLocale)) {
                gui.setCurrentLocale(newLocale);
            }
        });

        selectorPanel.add(langSelector);
        add(selectorPanel, BorderLayout.NORTH);
    }

    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();

        titleLabel.setText(messages.getString("title.signIn"));
        subtitleLabel.setText(messages.getString("subtitle.signIn"));
        usernameLabel.setText(messages.getString("label.username"));
        passwordLabel.setText(messages.getString("label.password"));
        signInBtn.setText(messages.getString("button.signin"));
        signupLabel.setText(messages.getString("link.noAccount"));
        signUpLink.setText("<html><u>" + messages.getString("title.signUp") + "</u></html>");

        langLabel.setText(messages.getLocale().getLanguage().equals("fr") ? "Langue: " : "Language: ");

        langSelector.removeAllItems();
        langSelector.addItem(messages.getString("language.english"));
        langSelector.addItem(messages.getString("language.french"));

        if (messages.getLocale().getLanguage().equals("fr")) {
            langSelector.setSelectedItem(messages.getString("language.french"));
        } else {
            langSelector.setSelectedItem(messages.getString("language.english"));
        }
    }
}
