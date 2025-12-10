import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;

public class MoodPanel extends JPanel implements Localizable {

    private  GUI gui;
    private  Controller controller;
    private List<MoodOption> availableMoods;
    private JPanel selectedMoodPanel = null;
    private int selectedMoodOptionId = -1;
    private  JButton submitBtn;
    private JLabel titleLabel, subtitleLabel;

    private static final String[] MOOD_IMAGE_FILES = {
            "emoji/terrible.png", // MoodLevel 1 (Worst)
            "emoji/bad.png",       // MoodLevel 2
            "emoji/ok.png",       // MoodLevel 3
            "emoji/good.png",      // MoodLevel 4
            "emoji/great.png"      // MoodLevel 5 (Best)
    };
    public MoodPanel(GUI gui) {
        this.gui = gui;
        this.controller = gui.getController();
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        titleLabel = new JLabel("How are you feeling today?");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(230, 100, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(10));

        subtitleLabel = new JLabel("Select the emoji that best represents your general mood");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Mood Emojis Panel
        JPanel emojisPanel = new JPanel();
        emojisPanel.setLayout(new GridLayout(1, 0, 20, 0));
        emojisPanel.setBackground(Color.WHITE);
        emojisPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Submit Button
        submitBtn = new JButton("Submit Mood");
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(300, 40));
        submitBtn.setBackground(new Color(59, 130, 246));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 15));
        submitBtn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        submitBtn.setBorderPainted(false);
        submitBtn.setEnabled(false);
        submitBtn.addActionListener(e -> handleSubmitMood());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(Color.WHITE);
        southPanel.add(submitBtn);

        add(southPanel, BorderLayout.SOUTH);
        add(emojisPanel, BorderLayout.CENTER);

        loadMoodOptions(emojisPanel);

        // Apply localization
        refreshText();
    }

    private void loadMoodOptions(JPanel emojisPanel) {
        availableMoods = controller.getAvailableMoodOption();
        if (availableMoods.isEmpty()) {
            emojisPanel.add(new JLabel("No mood options available."));
            return;
        }
        availableMoods.sort((m1, m2) -> Integer.compare(m1.getMoodLevel(), m2.getMoodLevel()));
        Mood todayMood = controller.getTodayMood();
        if (todayMood != null) {
            // Navigate directly to the main panel if mood is logged
            JOptionPane.showMessageDialog(this, "You have already logged your general mood for today. Showing the main dashboard.",
                    "Mood Already Logged", JOptionPane.INFORMATION_MESSAGE);
            gui.showPanel("main");
            return;
        }
        for (MoodOption mood : availableMoods) {
            JPanel emojiOptionPanel = createEmojiOptionPanel(mood);
            emojisPanel.add(emojiOptionPanel);
        }
    }
    private JPanel createEmojiOptionPanel(MoodOption mood) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(100, 100));
        panel.setBackground(new Color(245, 240, 235));
        panel.setBorder(BorderFactory.createLineBorder(new Color(245, 240, 235), 2));
        String fileName = getFileNameForMoodLevel(mood.getMoodLevel());
        ImageIcon icon = null;
        JLabel emojiLabel;
        try {
            // Load the image file from the disk/project root
            icon = new ImageIcon(fileName);
        } catch (Exception e) {
            // Print error but allow fallback
        }
        if (icon != null && icon.getIconWidth() > 0) {
            Image image = icon.getImage();
            // Scale the image to a consistent size (70x70)
            Image scaledImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            emojiLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            emojiLabel = new JLabel(mood.getEmoji());
            emojiLabel.setFont(new Font("Arial", Font.PLAIN, 70));
            emojiLabel.setForeground(Color.RED);
        }
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(emojiLabel, BorderLayout.CENTER);
        // Add mood label below the emoji *********
        JLabel label = new JLabel(mood.getMoodLabel());
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.SOUTH);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedMoodPanel != null) {
                    selectedMoodPanel.setBackground(new Color(245, 240, 235));
                    selectedMoodPanel.setBorder(BorderFactory.createLineBorder(new Color(245, 240, 235), 2));
                }
                panel.setBackground(new Color(255, 220, 180));
                panel.setBorder(BorderFactory.createLineBorder(new Color(250, 150, 70), 2));
                selectedMoodPanel = panel;
                selectedMoodOptionId = mood.getMoodOptionId();
                submitBtn.setEnabled(true);
            }
        });
        return panel;
    }

    private String getFileNameForMoodLevel(int moodLevel) {
        if (moodLevel >= 1 && moodLevel <= MOOD_IMAGE_FILES.length) {
            return MOOD_IMAGE_FILES[moodLevel - 1];
        }
        return "";
    }
        public void handleSubmitMood() {
            ResourceBundle messages = gui.getMessages();

            if (selectedMoodOptionId == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        messages.getString("err.noData"),
                        messages.getString("err.title"),
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String result = controller.logDailyMood(selectedMoodOptionId, null);

            boolean isError =
                    result.equals(messages.getString("err.login")) ||
                            result.equals(messages.getString("err.moodLogged"));

            String title = isError
                    ? messages.getString("err.failed")
                    : messages.getString("msg.success");

            int type = isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;

            JOptionPane.showMessageDialog(this, result, title, type);

            if (!isError) {
                gui.showPanel("main");
            }

    }

    @Override
    public void refreshText(){
        ResourceBundle messages = gui.getMessages();
        titleLabel.setText(messages.getString("mood.title"));
        subtitleLabel.setText(messages.getString("mood.subtitle"));
        submitBtn.setText(messages.getString("mood.button"));
    }
}