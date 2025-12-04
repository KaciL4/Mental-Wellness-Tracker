import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GoalPanel extends JPanel{
    private GUI gui;

    private JTable goalsTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField descriptionField;
    private JButton addButton, deleteButton, backButton;

    public GoalPanel(GUI gui){
        this.gui = gui;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Goal ID", "Goal Name", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        goalsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(goalsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        nameField = new JTextField(10);
        descriptionField = new JTextField(15);
        addButton = new JButton("Add Goal");
        deleteButton = new JButton("Delete Selected");
        backButton = new JButton("Back");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
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
        String name = nameField.getText();
        String description = descriptionField.getText();

        String result = gui.getController().addGoal(name, description);

        if (result.startsWith("Goal")) {
            loadGoals();
            nameField.setText("");
            descriptionField.setText("");
        }
    }

    private void deleteSelectedGoal(){
        int selectedRow = goalsTable.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Select a goal to delete.");
            return;
        }

        int goalID = (int) tableModel.getValueAt(selectedRow, 0);
        String result = gui.getController().removeGoal(goalID);
        JOptionPane.showMessageDialog(this, result);

        if(result.startsWith("Goal")){
            loadGoals();
        }
    }

    private void editSelectedGoal(int row){
        int goalId = (int) tableModel.getValueAt(row, 0);
        String currentName = (String) tableModel.getValueAt(row, 1);
        String currentDescription = (String) tableModel.getValueAt(row, 2);

        JTextField nameFieldPopUp = new JTextField(currentName, 15);
        JTextField descFieldPopUp = new JTextField(currentDescription, 20);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Name:"));
        panel.add(nameFieldPopUp);
        panel.add(new JLabel("Description:"));
        panel.add(descFieldPopUp);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Goal", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){
            String newName = nameFieldPopUp.getText();
            String newDesc = descFieldPopUp.getText();

            String updatedResult = gui.getController().updateGoal(goalId, newName, newDesc);
            JOptionPane.showMessageDialog(this, updatedResult);
            loadGoals();
        }
    }
}
