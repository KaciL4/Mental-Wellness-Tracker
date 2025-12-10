import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class AccountManagerPanel extends JPanel implements Localizable{
    private GUI gui;

    private JLabel titleLabel, userLabel, passwordLabel;
    private JButton changePassBtn, deleteBtn, logoutBtn, backBtn;

    private JTextField userField;
    private JPasswordField passwordField;

    public AccountManagerPanel(GUI gui){
        this.gui=gui;
        setLayout(new GridBagLayout());
        setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,20,10,20);
        gbc.anchor=GridBagConstraints.WEST;

        // Title
        titleLabel = new JLabel("Account Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD,28));
        gbc.gridx = 0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.insets=new Insets(30,0,40,0);
        add(titleLabel,gbc);
        gbc.gridwidth=1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.WEST;
        gbc.insets=new Insets(8,20,4,10);

        // Username label
        userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial",Font.PLAIN,15));
        gbc.gridx = 0;
        gbc.gridy=1;
        gbc.weightx=0.0;
        add(userLabel,gbc);

        // Username field
        userField = new JTextField();
        userField.setPreferredSize(new Dimension(200,40));
        userField.setFont(new Font("Arial",Font.PLAIN,15));
        userField.setBackground(Color.WHITE);
        userField.setEditable(false);
        userField.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(Color.LIGHT_GRAY),
               BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx=1;
        gbc.weightx=1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 4, 20);
        add(userField,gbc);

        // Password label
        passwordLabel = new JLabel("Current Password:");
        passwordLabel.setFont(new Font("Arial",Font.PLAIN,15));
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets=new Insets(15,20,4,10);
        add(passwordLabel,gbc);

        // Panel to hold password field + button
        JPanel passwordRowPanel=new JPanel(new GridBagLayout());
        passwordRowPanel.setOpaque(false);
        GridBagConstraints prGBC=new GridBagConstraints();
        prGBC.insets = new Insets(0,0,0,0);

        // Password field
        passwordField =new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200,40));
        passwordField.setFont(new Font("Arial",Font.PLAIN,15));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5,10,5,10)
        ));
        passwordField.setEditable(false);
        prGBC.gridx=0;
        prGBC.gridy=0;
        prGBC.fill=GridBagConstraints.HORIZONTAL;
        prGBC.weightx=1.0;
        passwordRowPanel.add(passwordField,prGBC);

        // Change password button
        changePassBtn= new JButton("Change Password");
        changePassBtn.setPreferredSize(new Dimension(180,40));
        changePassBtn.setForeground(Color.WHITE);
        changePassBtn.setBackground(Color.BLUE);
        changePassBtn.setFont(new Font("Arial", Font.BOLD,14));
        changePassBtn.addActionListener(e-> gui.showPanel("changepassword"));
        prGBC.gridx=1;
        prGBC.gridy=0;
        prGBC.weightx=0.0;
        prGBC.insets=new Insets(0,10,0,0);
        passwordRowPanel.add(changePassBtn,prGBC);
        gbc.gridx=1;
        gbc.gridy=2;
        gbc.gridwidth=1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx=1.0;
        gbc.insets=new Insets(25,10,30,20);
        add(passwordRowPanel,gbc);

        // Action buttons (logout + delete)
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionButtonPanel.setOpaque(false);

        // Logout button
        logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(120, 40));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(60, 60, 60));
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.addActionListener(e -> handleLogout());
        actionButtonPanel.add(logoutBtn);

        // Delete button
        deleteBtn = new JButton("Delete This Account");
        deleteBtn.setPreferredSize(new Dimension(180,40));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(Color.RED);
        deleteBtn.setFont(new Font("Arial", Font.BOLD,14));
        deleteBtn.addActionListener(e-> handleDeleteAccount());
        actionButtonPanel.add(deleteBtn);
        gbc.gridx=0;
        gbc.gridy=3;
        gbc.gridwidth=2;
        gbc.anchor=GridBagConstraints.SOUTHEAST;
        gbc.weighty=1.0;
        gbc.insets= new Insets(50,20,30,20);
        add(actionButtonPanel,gbc);

        // Back button
        backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(200,50));
        backBtn.setFont(new Font("Arial",Font.BOLD,15));
        backBtn.addActionListener(e -> gui.showPanel("main")
        );
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setForeground(Color.BLUE);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.NORTHWEST;
        add(backBtn, gbc);

        // Apply localization
        refreshText();

    }
    private void handleLogout() {
        ResourceBundle messages = gui.getMessages();
        int confirmation = JOptionPane.showConfirmDialog(this, messages.getString("accountManager.confirm.logout.message"),
                messages.getString("accountManager.confirm.logout.title"),JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmation == JOptionPane.YES_OPTION) {
            gui.getController().logout();
            gui.showPanel("signin");
        }
    }

    private void handleDeleteAccount(){
        ResourceBundle messages = gui.getMessages();
        int confirmation =JOptionPane.showConfirmDialog(this,messages.getString("accountManager.confirm.deleteAccount.message"),
                messages.getString("accountManager.confirm.deleteAccount.title"),JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(confirmation==JOptionPane.YES_OPTION){
            String result = gui.getController().deleteCurrentUser();
            JOptionPane.showMessageDialog(this,result,messages.getString("msg.del"),JOptionPane.INFORMATION_MESSAGE);
            gui.showPanel("signin");
        }
    }

    public void updateFields(){
        if(gui.getController().getCurrentUser()!= null){
          userField.setText(gui.getController().getCurrentUser().getUsername());
          passwordField.setText(gui.getController().getCurrentUser().getPassword());
        }
    }
    public void setVisible(boolean visible){
        super.setVisible(visible);
        if(visible){
         updateFields();
        }
    }
    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();
        titleLabel.setText(messages.getString("accountManager.title"));
        userLabel.setText(messages.getString("label.username"));
        passwordLabel.setText(messages.getString("accountManager.currentPassword"));
        changePassBtn.setText(messages.getString("accountManager.button.changePassword"));
        deleteBtn.setText(messages.getString("accountManager.button.deleteAccount"));
        logoutBtn.setText(messages.getString("accountManager.button.logout"));
        backBtn.setText(messages.getString("accountManager.button.back"));
    }
}
