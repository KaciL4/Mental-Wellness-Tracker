import javax.swing.*;
import java.awt.*;
import java.util.*;
public class SignUpPanel extends JPanel implements Localizable {
    private GUI gui;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JLabel titleLabel, usernameLabel,passwordLabel, subtitleLabel;
    private JButton createButton, cancelButton;


    private static final Font FONT_REGULAR = new Font("Arial", Font.PLAIN, 15);
    private static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 15);

    public SignUpPanel(GUI gui){
        this.gui = gui;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Title + Subtitle
        titleLabel = new JLabel("<html><center>"
                + "<span style='font-family: Arial; font-size: 28px; font-weight: bold;'>Sign Up</span><br>"
                + "<span style='font-family: Arial; font-size: 13px; color: #666666;'>Create your account</span>"
                + "</center></html>");
        subtitleLabel = new JLabel();

        gbc.gridx =0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        gbc.anchor =GridBagConstraints.CENTER;
        gbc.fill=GridBagConstraints.NONE;
        add(titleLabel,gbc);

        gbc.gridwidth=2;
        gbc.anchor= GridBagConstraints.WEST;
        gbc.fill =GridBagConstraints.HORIZONTAL;

        // Username Label
        gbc.gridy =1;
        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(FONT_REGULAR);
        add(usernameLabel, gbc);

        // Username Field
        gbc.gridy =2;
        usernameField =new JTextField();
        usernameField.setFont(FONT_REGULAR);
        usernameField.setPreferredSize(new Dimension(300,36));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(6,10,6,10)
        ));
        add(usernameField,gbc);

        // Password label
        gbc.gridy= 3;
        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(FONT_REGULAR);
        add(passwordLabel, gbc);

        // Password Input
        gbc.gridy =4;
        passwordField = new JPasswordField();
        passwordField.setFont(FONT_REGULAR);
        passwordField.setPreferredSize(new Dimension(300,36));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(6,10,6,10)
        ));
        add(passwordField, gbc);

        // Buttons Layout Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1,2,10,0));

        // Create Button
        createButton= new JButton("Create Account");
        createButton.setFont(FONT_BOLD);
        createButton.setBackground(new Color(59,130,246));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setPreferredSize(new Dimension(150, 40));
        createButton.addActionListener(e ->createUserHandler());
        buttonPanel.add(createButton);

        //  Cancel Button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(FONT_BOLD);
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(Color.RED);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.RED));
        cancelButton.setPreferredSize(new Dimension(150,40));
        cancelButton.addActionListener(e ->{
            clearFields();
            gui.showPanel("signin");
        });
        buttonPanel.add(cancelButton);

        gbc.gridy= 5;
        gbc.gridx=0;
        gbc.gridwidth=2;
        gbc.insets = new Insets(30,0,10,0);
        add(buttonPanel, gbc);

        refreshText();
    }

    private void createUserHandler(){
        String username=usernameField.getText().trim();
        String password=new String(passwordField.getPassword());

        if(username.isEmpty()||password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Error: Please fill in all fields.");
            return;
        }

        String result = gui.getController().registerUser(username, password);
        if(result.startsWith("Error")){
            JOptionPane.showMessageDialog(this,result,"Registration Failed",JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this,result,"Success Registration",JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            gui.showPanel("signin");
        }
    }
    private void clearFields(){
        usernameField.setText("");
        passwordField.setText("");
    }

    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();

        // Title + Subtitle
        String titleText = messages.getString("title.signUp");
        String subtitleText = "";
        try {
            subtitleText = messages.getString("subtitle.signUp");
        } catch (MissingResourceException e) {  // handle missing key safely
            // No subtitle key defined because it's optional so we can skip it
        }

        if (!subtitleText.isEmpty()) {
            titleLabel.setText("<html><center>"
                    + "<span style='font-family: Arial; font-size: 28px; font-weight: bold;'>" + titleText + "</span><br>"
                    + "<span style='font-family: Arial; font-size: 13px; color: #666666;'>" + subtitleText + "</span>"
                    + "</center></html>");
        } else {
            titleLabel.setText("<html><center>"
                    + "<span style='font-family: Arial; font-size: 28px; font-weight: bold;'>" + titleText + "</span>"
                    + "</center></html>");
        }

        usernameLabel.setText(messages.getString("label.username"));
        passwordLabel.setText(messages.getString("label.password"));
        createButton.setText(messages.getString("button.signup"));
        cancelButton.setText(messages.getString("button.cancel"));
    }
}