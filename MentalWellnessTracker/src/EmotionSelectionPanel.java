import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmotionSelectionPanel extends JPanel implements Localizable{
    private final GUI gui;
    private final Controller controller;
    private final List<EmotionOption> availableEmotions;
    private final List<JCheckBox> emotionCheckboxes;
    private JLabel statusLabel, titleLabel, subtitleLabel;
    private JButton submitBtn;

    // Updated colors to match the design
    private static final Color HEADER_COLOR = new Color(33, 33, 33);
    private static final Color SUBTITLE_COLOR = new Color(120, 120, 120);
    private static final Color SUBMIT_BUTTON_COLOR = new Color(45, 200, 180);
    private static final Color CHECKBOX_BORDER = new Color(200, 200, 200);

    public EmotionSelectionPanel(GUI gui) {
        this.gui = gui;
        ResourceBundle messages = gui.getMessages();
        this.controller = gui.getController();

        // Data Initialization
        this.availableEmotions = controller.getAvailableEmotionOptions();

        this.emotionCheckboxes = new ArrayList<>();

        // Panel Setup
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        titleLabel = new JLabel("Track Your Emotions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(HEADER_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(12));

        subtitleLabel =new JLabel("Select all the emotions you are currently feeling");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(SUBTITLE_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Emotions Panel
        JPanel emotionsContainer = new JPanel(new BorderLayout());
        emotionsContainer.setBackground(Color.WHITE);

        JPanel emotionsPanel = new JPanel();
        emotionsPanel.setLayout(new GridLayout(3, 0, 40, 20)); // 3 rows with gaps
        emotionsPanel.setBackground(Color.WHITE);
        emotionsPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        loadEmotionOptions(emotionsPanel);

        emotionsContainer.add(emotionsPanel, BorderLayout.CENTER);
        add(emotionsContainer, BorderLayout.CENTER);

        // Bottom Panel with status and submit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

        // Status label
        statusLabel = new JLabel(messages.getString("emotion.selected.none"));
        statusLabel.setFont(new Font("Arial",Font.PLAIN,14));
        statusLabel.setForeground(SUBTITLE_COLOR);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(statusLabel);
        bottomPanel.add(statusPanel,BorderLayout.WEST);

        // Submit button
        submitBtn = new JButton("Submit");
        submitBtn.setPreferredSize(new Dimension(140, 50));
        submitBtn.setBackground(SUBMIT_BUTTON_COLOR);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setBorder(BorderFactory.createEmptyBorder());
        submitBtn.setBorderPainted(false);
        submitBtn.setOpaque(true);
        submitBtn.addActionListener(e -> handleSubmitEmotions());

        // Add rounded corners effect
        submitBtn.setContentAreaFilled(false);
        submitBtn.setOpaque(true);

        JPanel buttonPanel =new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitBtn);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Apply localization
        refreshText();
    }

    private void loadEmotionOptions(JPanel emotionsPanel){
//        if (availableEmotions == null || availableEmotions.isEmpty()) {
//            emotionsPanel.add(new JLabel("No emotion options available to display."));
//            return;
//        }
        for (EmotionOption emotion : availableEmotions) {

            // Create a panel for each emotion with emoji + text
            JPanel emotionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            emotionPanel.setBackground(Color.WHITE);

            // Checkbox
            JCheckBox checkBox = new JCheckBox();
            checkBox.setBackground(Color.WHITE);
            checkBox.setBorder(BorderFactory.createLineBorder(CHECKBOX_BORDER, 2));
            checkBox.setPreferredSize(new Dimension(24, 24));
            checkBox.addActionListener(e -> updateStatusLabel());

            // Emotion name label
            JLabel nameLabel = new JLabel(emotion.getEmotionName());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            nameLabel.setForeground(HEADER_COLOR);

            emotionPanel.add(checkBox);
            emotionPanel.add(nameLabel);

            emotionCheckboxes.add(checkBox);
            emotionsPanel.add(emotionPanel);
        }
    }

    private void updateStatusLabel(){
        ResourceBundle messages = gui.getMessages();
        int count = 0;
        for (JCheckBox checkBox:emotionCheckboxes) {
            if (checkBox.isSelected()) {
                count++;
            }
        }
        if(count == 0){
            statusLabel.setText(messages.getString("emotion.selected.none"));
        } else if(count == 1){
            statusLabel.setText(messages.getString("emotion.selected.one"));
        }else {
            statusLabel.setText(count + " " + messages.getString("emotion.selected.many"));
        }
    }

    private void handleSubmitEmotions() {
        ResourceBundle messages = gui.getMessages();

        List<Integer> selectedEmotionIds = new ArrayList<>();
        for (int i = 0; i < emotionCheckboxes.size(); i++) {
            if (emotionCheckboxes.get(i).isSelected()) {
                selectedEmotionIds.add(availableEmotions.get(i).getOptionId());
            }
        }

        String result = controller.logDailyEmotion(selectedEmotionIds);

        boolean isError =
                result.equals(messages.getString("err.login")) ||
                        result.equals(messages.getString("err.noEmotion"));

        String title = isError
                ? messages.getString("err.failed")
                : messages.getString("emotion.log");

        int type = isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;

        JOptionPane.showMessageDialog(this, result, title, type);

        if (!isError) {
            gui.showPanel("main");
        }
    }

    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();

        titleLabel.setText(messages.getString("emotion.title"));
        subtitleLabel.setText(messages.getString("emotion.subtitle"));
        submitBtn.setText(messages.getString("emotion.button"));
        statusLabel.setText(messages.getString("emotion.selected.none"));
    }
}