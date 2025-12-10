import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ResourceBundle;

public class HabitLogPanel extends JPanel implements Localizable {
    private GUI gui;

    private JTable habitTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> goalComboBox;
    private JTextField notesField;
    private JButton addButton, deleteButton, backButton;
    private JLabel goalLabel, notesLabel;

    public HabitLogPanel(GUI gui) {
        this.gui = gui;

        setLayout(new BorderLayout());

        // Table model – headers will be localized in refreshText()
        tableModel = new DefaultTableModel(new Object[]{"Log ID", "Goal", "Notes", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        habitTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(habitTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());

        goalLabel = new JLabel("Goal:");
        notesLabel = new JLabel("Notes:");

        goalComboBox = new JComboBox<>();
        loadGoalsIntoComboBox();

        notesField = new JTextField(15);
        addButton = new JButton("Add New Log");
        deleteButton = new JButton("Delete Selected Log");
        backButton = new JButton(("Back"));

        inputPanel.add(goalLabel);
        inputPanel.add(goalComboBox);
        inputPanel.add(notesLabel);
        inputPanel.add(notesField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addHabitLog());
        deleteButton.addActionListener(e -> deleteSelectedLog());
        backButton.addActionListener(e -> gui.showPanel("main"));

        habitTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = habitTable.getSelectedRow();
                    if (row != -1) editSelectedLog(row);
                }
            }
        });

        loadHabitLogs();
        refreshText();
    }

    private void loadGoalsIntoComboBox(){
        goalComboBox.removeAllItems(); // avoid duplicates when refreshing
        List<Goal> goals = gui.getController().getAllGoals();
        for (Goal g : goals) {
            goalComboBox.addItem(g.getGoalName());
        }
    }

    private void loadHabitLogs(){
        tableModel.setRowCount(0);
        List<Goal> goals = gui.getController().getAllGoals();

        for(Goal goal : goals){
            List<HabitLog> logs = gui.getController().getLogsForGoal(goal.getGoalId());
            for (HabitLog log : logs){
                String dateStr = "";
                if (log.getCompletedDate() != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    dateStr = sdf.format(log.getCompletedDate());
                }

                tableModel.addRow(new Object[]{
                        log.getLogId(),
                        goal.getGoalName(),
                        log.getNotes(),
                        dateStr
                });
            }
        }
    }

    public void refreshHabits(){
        loadGoalsIntoComboBox();
        loadHabitLogs();
    }

    private void addHabitLog(){
        ResourceBundle messages = gui.getMessages();

        String goalName = (String) goalComboBox.getSelectedItem();
        String notes = notesField.getText().trim();

        int goalId = getGoalIdFromName(goalName);
        if (goalId == -1){
            JOptionPane.showMessageDialog(
                    this,
                    messages.getString("d.err"),
                    messages.getString("msg.error"),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Date completedDate = new Date();
        String result = gui.getController().createHabitLog(goalId, completedDate, notes);

        boolean isError = result.startsWith("Error");
        JOptionPane.showMessageDialog(
                this,
                result,
                isError ? messages.getString("d.err") : messages.getString("habit.logAdded"),
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE
        );

        if (!isError){
            loadHabitLogs();
            notesField.setText("");
        }
    }

    private int getGoalIdFromName(String goalName){
        if (goalName == null) return -1;
        for (Goal g : gui.getController().getAllGoals()){
            if (g.getGoalName().equals(goalName)){
                return g.getGoalId();
            }
        }
        return -1;
    }

    private void deleteSelectedLog(){
        ResourceBundle messages = gui.getMessages();

        int selectedRow = habitTable.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(
                    this,
                    messages.getString("habit.select"),
                    messages.getString("msg.error"),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int logId = (int) tableModel.getValueAt(selectedRow, 0);

        String result = gui.getController().deleteHabitLog(logId);

        boolean isError = result.startsWith("Error");
        JOptionPane.showMessageDialog(
                this,
                result,
                isError ? messages.getString("err.failed") : messages.getString("habit.logDeleted"),
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE
        );

        if (!isError) {
            loadHabitLogs();
        }
    }

    private void editSelectedLog(int row){
        ResourceBundle messages = gui.getMessages();

        int logId = (int) tableModel.getValueAt(row, 0);
        HabitLog log = gui.getController().getHabitLog(logId);
        if (log == null) return;

        JTextField notesFieldPopUp = new JTextField(log.getNotes(), 20);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel(messages.getString("habit.column.notes")));
        panel.add(notesFieldPopUp);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                messages.getString("habit.edit"),
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String newNotes = notesFieldPopUp.getText().trim();
            Date completedDate = log.getCompletedDate();

            String updateResult = gui.getController().updateHabitLog(logId, completedDate, newNotes);

            boolean isError = updateResult.startsWith("Error");
            JOptionPane.showMessageDialog(
                    this,
                    updateResult,
                    isError ? messages.getString("err.failed") : messages.getString("msg.update"),
                    isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE
            );

            if (!isError) {
                loadHabitLogs();
            }
        }
    }

    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();

        goalLabel.setText(messages.getString("habit.column.goal"));
        notesLabel.setText(messages.getString("habit.column.notes"));

        addButton.setText(messages.getString("habit.add"));
        deleteButton.setText(messages.getString("habit.delete"));
        backButton.setText(messages.getString("button.back"));

        tableModel.setColumnIdentifiers(new Object[]{
                messages.getString("habit.column.id"),
                messages.getString("habit.column.goal"),
                messages.getString("habit.column.notes"),
                messages.getString("habit.column.date")
        });
        habitTable.getTableHeader().repaint();
    }
}
