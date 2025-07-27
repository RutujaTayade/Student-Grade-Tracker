import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class StudentGradeTracker extends JFrame {
    private JTextField nameField, rollField, gradesField;
    private DefaultTableModel tableModel;
    private ArrayList<Student> studentList = new ArrayList<>();

    public StudentGradeTracker() {
        setTitle("📘 Student Grade Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        /
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Look and feel not set.");
        }

        // Input section
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Student Details"));
        inputPanel.setBackground(new Color(240, 248, 255));
        inputPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(nameLabel);
        nameField = new JTextField();
        inputPanel.add(nameField);

        JLabel rollLabel = new JLabel("Roll No:");
        rollLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(rollLabel);
        rollField = new JTextField();
        inputPanel.add(rollField);

        JLabel gradesLabel = new JLabel("Grades (comma separated):");
        gradesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(gradesLabel);
        gradesField = new JTextField();
        inputPanel.add(gradesField);

        JButton addButton = new JButton("➕ Add Student");
        JButton clearButton = new JButton("🧹 Clear");
        addButton.setBackground(new Color(60, 179, 113));
        clearButton.setBackground(new Color(0xB9375D));
        addButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        clearButton.setFocusPainted(false);
        inputPanel.add(addButton);
        inputPanel.add(clearButton);

        // Table Section
        String[] columns = {"Name", "Roll No", "Grades", "Average", "Highest", "Lowest"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);

    
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> addStudent());
        clearButton.addActionListener(e -> clearFields());
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String rollText = rollField.getText().trim();
        String gradesText = gradesField.getText().trim();

        if (name.isEmpty() || rollText.isEmpty() || gradesText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int rollNo = Integer.parseInt(rollText);
            String[] gradeStrs = gradesText.split(",");
            ArrayList<Integer> grades = new ArrayList<>();
            for (String g : gradeStrs) grades.add(Integer.parseInt(g.trim()));

            Student s = new Student(name, rollNo, grades);
            studentList.add(s);

            tableModel.addRow(new Object[]{
                    s.name,
                    s.rollNo,
                    s.getGradesAsString(),
                    String.format("%.2f", s.getAverage()),
                    s.getHighest(),
                    s.getLowest()
            });

            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Please enter valid integers for Roll No and Grades.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        gradesField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGradeTracker().setVisible(true));
    }
}
