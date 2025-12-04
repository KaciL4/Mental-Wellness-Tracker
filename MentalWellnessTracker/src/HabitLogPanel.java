import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class HabitLogPanel extends JPanel {
    private GUI gui;

    private JTable habitTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> goalComboBox;
    private JTextField notesField;
    private JButton addButton, deleteButton, backButton;

    public HabitLogPanel(GUI gui) {
        this.gui = gui;

        setLayout(new BorderLayout());

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

        goalComboBox = new JComboBox<>();
        loadGoalsIntoComboBox();

        notesField = new JTextField(15);
        addButton = new JButton("Add New Log");
        deleteButton = new JButton("Delete Selected Log");
        backButton = new JButton(("Back"));

        inputPanel.add(new JLabel("Goal:"));
        inputPanel.add(goalComboBox);
        inputPanel.add(new JLabel("Notes:"));
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
    }


    private void loadGoalsIntoComboBox(){
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
                // Format the date nicely
                String dateStr = "";
                if (log.getCompletedDate() != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    dateStr = sdf.format(log.getCompletedDate());
                }

                tableModel.addRow(new Object[]{
                        log.getLogId(),
                        goal.getGoalName(),
                        log.getNotes(),
                        dateStr  //  formatted date
                });
            }
        }
    }

    public void refreshHabits(){
        loadGoalsIntoComboBox();
        loadHabitLogs();
    }

    private void addHabitLog(){
        String goalName = (String) goalComboBox.getSelectedItem();
        String notes = notesField.getText();

        int goalId = getGoalIdFromName(goalName);
        if (goalId == -1){
            JOptionPane.showMessageDialog(this, "Invalid goal selected.");
            return;
        }

        Date completedDate = new Date();
        String result = gui.getController().createHabitLog(goalId, completedDate, notes);
        JOptionPane.showMessageDialog(this, result);

        if (result.startsWith("Habit log added")){
            loadHabitLogs();
            notesField.setText("");
        }
    }

    private int getGoalIdFromName(String goalName){
        for (Goal g : gui.getController().getAllGoals()){
            if (g.getGoalName().equals(goalName)){
                return g.getGoalId();
            }
        }
        return -1;
    }

    private void deleteSelectedLog(){
        int selectedRow = habitTable.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Select a log to delete.");
            return;
        }

        int logId = (int) tableModel.getValueAt(selectedRow, 0);

        String result = gui.getController().deleteHabitLog(logId);
        JOptionPane.showMessageDialog(this, result);

        if(result.startsWith("Habit Log deleted")) {
            tableModel.removeRow(selectedRow);
        }
        loadHabitLogs();
    }

    private void editSelectedLog(int row){
        int logId = (int) tableModel.getValueAt(row, 0);
        HabitLog log = gui.getController().getHabitLog(logId);
        if (log == null) return;

        JTextField notesFieldPopUp = new JTextField(log.getNotes(), 20);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Notes:"));
        panel.add(notesFieldPopUp);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Habit Log", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newNotes = notesFieldPopUp.getText();
            Date newDate = new Date();
            Date completedDate = log.getCompletedDate();

            String updateResult = gui.getController().updateHabitLog(logId, completedDate, newNotes);
            JOptionPane.showMessageDialog(this, updateResult);
            loadHabitLogs();
        }
    }
}

