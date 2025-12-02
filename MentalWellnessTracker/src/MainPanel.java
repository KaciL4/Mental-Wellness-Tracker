import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainPanel extends JPanel {
    private GUI gui;
    private JLabel moodSummaryLabel;
    public MainPanel(GUI gui){
        this.gui=gui;
        setLayout(new GridBagLayout());
        setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        gbc.anchor=GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10,10,10,10);

        JButton accountButton =new JButton("Account Management");
        accountButton.setPreferredSize(new Dimension(200,50));
        accountButton.setFont(new Font("Arial",Font.BOLD,15));
        accountButton.addActionListener(e->gui.showPanel("account"));
        accountButton.setMargin(new Insets(0, 0, 0, 0));
        accountButton.setContentAreaFilled(false);
        accountButton.setBorderPainted(false);
        accountButton.setFocusPainted(false);
        accountButton.setForeground(Color.BLUE);
        gbc.gridx=0;
        gbc.gridy=0;
        add(accountButton,gbc);

        moodSummaryLabel = new JLabel();
        moodSummaryLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        moodSummaryLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 1; // Position it below the account button area, maybe center top
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0.5; // Give it space
        gbc.insets = new Insets(50, 10, 10, 10);
        add(moodSummaryLabel, gbc);

        // ADD EMOTIONS BUTTON
        JButton addEmotionsBtn = new JButton("Add Emotions");
        addEmotionsBtn.setPreferredSize(new Dimension(300, 50));
        addEmotionsBtn.setBackground(new Color(59,130,246));
        addEmotionsBtn.setForeground(Color.WHITE);
        addEmotionsBtn.setFocusPainted(false);
        addEmotionsBtn.setFont(new Font("Arial", Font.BOLD, 16));

        // Action to show EmotionSelectionPanel
        addEmotionsBtn.addActionListener(e -> {
            // Check if mood is logged first
            if (gui.getController().getTodayMood() == null) {
                JOptionPane.showMessageDialog(this,
                        "Please log your general mood for today first.",
                        "Mood Required",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                gui.showPanel("emotionselection");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(30, 10, 30, 10);
        add(addEmotionsBtn, gbc);

    }

//    method to load and refresh average mood message
    public void refreshData(){
        String avgMoodMessage = gui.getController().calculateAverageMoodLevel();
        moodSummaryLabel.setText("<html><div style='text-align: center;'>" + avgMoodMessage.replace("\n", "<br>") + "</div></html>");
    }
}
