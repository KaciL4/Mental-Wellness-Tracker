import javax.swing.*;
import java.awt.*;
import java.util.*;
public class ChangePasswordPanel extends JPanel implements Localizable{
    private GUI gui;
    private JPasswordField oldPasswordField, newPasswordField;

    private JLabel title, subtitle, oldPassLabel, newPassLabel;
    private JButton saveBtn, cancelBtn;

    public ChangePasswordPanel(GUI gui){
        this.gui=gui;
        setLayout(new GridBagLayout());
        setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,40,10,40);
        gbc.fill=GridBagConstraints.HORIZONTAL;

        // Title
        title = new JLabel("Change Password");
        title.setFont(new Font("Arial", Font.BOLD,32));
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        gbc.insets=new Insets(40,40,20,40);
        add(title,gbc);

        // Subtitle
        subtitle = new JLabel("Enter your current and new password");
        subtitle.setFont(new Font("Arial",Font.PLAIN,14));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy=1;
        gbc.insets=new Insets(0,40,50,40);
        add(subtitle,gbc);

        // Current password label
        oldPassLabel=new JLabel("Current Password");
        oldPassLabel.setFont(new Font("Arial",Font.PLAIN,14));
        gbc.gridy=2;
        gbc.gridwidth=2;
        gbc.insets=new Insets(10,40,8,40);
        add(oldPassLabel,gbc);

        // Old password label
        oldPasswordField=new JPasswordField();
        oldPasswordField.setPreferredSize(new Dimension(300,40));
        oldPasswordField.setFont(new Font("Arial",Font.PLAIN,14));
        gbc.gridy=3;
        gbc.insets=new Insets(0,40,25,40);
        add(oldPasswordField,gbc);

        // New password label
        newPassLabel = new JLabel("New Password");
        newPassLabel.setFont(new Font("Arial",Font.PLAIN,14));
        newPassLabel.setPreferredSize(new Dimension(300,40));
        gbc.gridy=4;
        gbc.insets=new Insets(10,40,8,40);
        add(newPassLabel,gbc);

        // New password field
        newPasswordField = new JPasswordField();
        newPasswordField.setPreferredSize(new Dimension(300,40));
        newPasswordField.setFont(new Font("Arial",Font.PLAIN,14));
        gbc.gridy=5;
        gbc.insets=new Insets(0,40,35,40);
        add(newPasswordField,gbc);

        // Save button
        saveBtn=new JButton("Save Changes");
        saveBtn.setPreferredSize(new Dimension(300,45));
        saveBtn.setBackground(Color.BLUE);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial",Font.BOLD,15));
        saveBtn.addActionListener(e->handleSavePassword());
        gbc.gridy=6;
        gbc.insets =new Insets(0,40,15,40);
        add(saveBtn,gbc);

        // Cancel button
        cancelBtn = new JButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(300,45));
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial",Font.BOLD,15));
        cancelBtn.addActionListener(e->{
            clearFields();
            gui.showPanel("account");
        });
        gbc.gridy=7;
        gbc.insets =new Insets(0,40,40,40);
        add(cancelBtn,gbc);

        // Apply localization
        refreshText();
    }

    private void handleSavePassword(){
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword=new String(newPasswordField.getPassword());
        String result =gui.getController().updatePassword(oldPassword,newPassword);

        if(result.startsWith("Error")){
            JOptionPane.showMessageDialog(this,result,"Error",JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this,result,"Succes",JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            gui.showPanel("account");
        }
    }

    private void clearFields(){
        oldPasswordField.setText("");
        newPasswordField.setText("");
    }
    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();
        title.setText(messages.getString("title.changePassword"));
        subtitle.setText(messages.getString("subtitle.passwordChange"));
        oldPassLabel.setText(messages.getString("label.currentPassword"));
        newPassLabel.setText(messages.getString("label.newPassword"));
        saveBtn.setText(messages.getString("button.saveChanges"));
        cancelBtn.setText(messages.getString("button.cancel"));
    }
}
