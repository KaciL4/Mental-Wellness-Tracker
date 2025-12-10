import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ResourceBundle;

public class GoalPanel extends JPanel implements Localizable {
    private GUI gui;

    private JTable goalsTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField descriptionField;
    private JButton addButton, deleteButton, backButton;
    private JLabel nameLabel, descriptionLabel;

    public GoalPanel(GUI gui){
        this.gui = gui;
        setLayout(new BorderLayout());

        // Table model – headers will be localized in refreshText()
        tableModel = new DefaultTableModel(new Object[]{"Goal ID", "Goal Name", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        goalsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(goalsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom input + buttons
        JPanel inputPanel = new JPanel(new FlowLayout());

        nameLabel = new JLabel("Name:");
        descriptionLabel = new JLabel("Description:");

        nameField = new JTextField(10);
        descriptionField = new JTextField(15);

        addButton = new JButton("Add Goal");
        deleteButton = new JButton("Delete Selected");
        backButton = new JButton("Back");

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addGoal());
        deleteButton.addActionListener(e -> deleteSelectedGoal());
        backButton.addActionListener(e -> gui.showPanel("main"));

        goalsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = goalsTable.getSelectedRow();
                    if (row != -1) editSelectedGoal(row);
                }
            }
        });

        loadGoals();
        refreshText();
    }

    private void loadGoals() {
        tableModel.setRowCount(0);
        List<Goal> goals = gui.getController().getAllGoals();
        for (Goal g : goals) {
            tableModel.addRow(new Object[]{g.getGoalId(), g.getGoalName(), g.getDescription()});
        }
    }

    public void refreshGoals(){
        loadGoals();
    }

    private void addGoal(){
        ResourceBundle messages = gui.getMessages();

        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();

        String result = gui.getController().addGoal(name, description);

        boolean isError = result.startsWith("Error");
        JOptionPane.showMessageDialog(
                this,
                result,
                isError ? messages.getString("err.failed") : messages.getString("goal.added"),
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE
        );

        if (!isError) {
            loadGoals();
            nameField.setText("");
            descriptionField.setText("");
        }
    }

    private void deleteSelectedGoal(){
        ResourceBundle messages = gui.getMessages();

        int selectedRow = goalsTable.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(
                    this,
                    messages.getString("goal.select"),
                    messages.getString("err.failed"),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int goalID = (int) tableModel.getValueAt(selectedRow, 0);
        String result = gui.getController().removeGoal(goalID);

        boolean isError = result.startsWith("Error");
        JOptionPane.showMessageDialog(
                this,
                result,
                isError ? messages.getString("err.failed") : messages.getString("goal.deleted"),
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE
        );

        if (!isError) {
            loadGoals();
        }
    }

    private void editSelectedGoal(int row){
        ResourceBundle messages = gui.getMessages();

        int goalId = (int) tableModel.getValueAt(row, 0);
        String currentName = (String) tableModel.getValueAt(row, 1);
        String currentDescription = (String) tableModel.getValueAt(row, 2);

        JTextField nameFieldPopUp = new JTextField(currentName, 15);
        JTextField descFieldPopUp = new JTextField(currentDescription, 20);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel(messages.getString("goal.name")));
        panel.add(nameFieldPopUp);
        panel.add(new JLabel(messages.getString("goal.desc")));
        panel.add(descFieldPopUp);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                messages.getString("goal.edit"),
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION){
            String newName = nameFieldPopUp.getText().trim();
            String newDesc = descFieldPopUp.getText().trim();

            String updatedResult = gui.getController().updateGoal(goalId, newName, newDesc);
            boolean isError = updatedResult.startsWith("Error");
            JOptionPane.showMessageDialog(
                    this,
                    updatedResult,
                    isError ? messages.getString("err.failed") : messages.getString("msg.updated"),
                    isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE
            );

            if (!isError) {
                loadGoals();
            }
        }
    }

    @Override
    public void refreshText() {
        ResourceBundle messages = gui.getMessages();

        nameLabel.setText(messages.getString("goal.name"));
        descriptionLabel.setText(messages.getString("goal.desc"));

        addButton.setText(messages.getString("goal.add"));
        deleteButton.setText(messages.getString("goal.delete"));
        backButton.setText(messages.getString("button.back"));

        tableModel.setColumnIdentifiers(new Object[]{
                messages.getString("goal.column.id"),
                messages.getString("goal.column.name"),
                messages.getString("goal.column.description")
        });
        goalsTable.getTableHeader().repaint();
    }
}
