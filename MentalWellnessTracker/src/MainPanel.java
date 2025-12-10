import javax.swing.*;
import java.awt.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MainPanel extends JPanel implements Localizable {
    private GUI gui;
    private JLabel moodSummaryLabel;
    private JButton accountBtn, addEmotionsBtn, goalsBtn, habitsBtn;


    public MainPanel(GUI gui){
        this.gui=gui;
        setLayout(new GridBagLayout());
        setBackground(new Color(240,240,240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        gbc.anchor=GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10,10,10,10);

        // Account Management Button
        accountBtn = new JButton("Account Management");
        accountBtn.setPreferredSize(new Dimension(200,50));
        accountBtn.setFont(new Font("Arial",Font.BOLD,15));
        accountBtn.addActionListener(e -> gui.showPanel("account")
        );
        accountBtn.setMargin(new Insets(0, 0, 0, 0));
        accountBtn.setContentAreaFilled(false);
        accountBtn.setBorderPainted(false);
        accountBtn.setFocusPainted(false);
        accountBtn.setForeground(Color.BLUE);
        gbc.gridx=0;
        gbc.gridy=0;
        add(accountBtn,gbc);

        // Mood Summary Label
        moodSummaryLabel = new JLabel();
        moodSummaryLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        moodSummaryLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(50, 10, 10, 10);
        add(moodSummaryLabel, gbc);

        // Add Emotions Button
        addEmotionsBtn = new JButton("Add Emotions");
        addEmotionsBtn.setPreferredSize(new Dimension(300, 50));
        addEmotionsBtn.setBackground(new Color(59,130,246));
        addEmotionsBtn.setForeground(Color.WHITE);
        addEmotionsBtn.setFocusPainted(false);
        addEmotionsBtn.setFont(new Font("Arial", Font.BOLD, 16));
        addEmotionsBtn.addActionListener(e -> {
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

        // Goals Button
        goalsBtn = new JButton("Goals");
        goalsBtn.setPreferredSize(new Dimension(300, 50));
        goalsBtn.setBackground(new Color(59,130,246));
        goalsBtn.setForeground(Color.WHITE);
        goalsBtn.setFocusPainted(false);
        goalsBtn.setFont(new Font("Arial", Font.BOLD, 16));
        goalsBtn.addActionListener(e -> gui.showPanel("goal"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(30, 10, 30, 10);
        add(goalsBtn, gbc);

        // Habits Button
        habitsBtn = new JButton("Habit");
        habitsBtn.setPreferredSize(new Dimension(300, 50));
        habitsBtn.setBackground(new Color(59,130,246));
        habitsBtn.setForeground(Color.WHITE);
        habitsBtn.setFocusPainted(false);
        habitsBtn.setFont(new Font("Arial", Font.BOLD, 16));
        habitsBtn.addActionListener(e -> gui.showPanel("habitlog"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(30, 10, 30, 10);
        add(habitsBtn, gbc);

        // Apply Localization
        refreshText();
    }

    public void refreshData(){
        String avgMoodMessage = gui.getController().calculateAverageMoodLevel();
        if (avgMoodMessage == null){
            moodSummaryLabel.setText("");
        } else {
            moodSummaryLabel.setText("<html><div style='text-align: center;'>" + avgMoodMessage.replace("\n",
                    "<br>") + "</div></html>");
        }
    }

    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();
        accountBtn.setText(messages.getString("main.button.accountManagement"));
        addEmotionsBtn.setText(messages.getString("main.button.addEmotions"));
        goalsBtn.setText(messages.getString("main.button.goals"));
        habitsBtn.setText(messages.getString("main.button.habits"));
    }

}
