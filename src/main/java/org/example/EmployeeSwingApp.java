package org.example;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class EmployeeSwingApp extends JFrame {
    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color NAVY = new Color(25, 39, 67);
    private static final Color BLUE = new Color(48, 101, 218);
    private static final Color MUTED = new Color(104, 116, 139);

    private final EmployeeDAO dao = new EmployeeDAO();
    private final JTextField idField = new JTextField(16);
    private final JTextField nameField = new JTextField(16);
    private final JTextField salaryField = new JTextField(16);
    private final JLabel statusLabel = new JLabel("Ready");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Employee name", "Annual salary"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable employeeTable = new JTable(tableModel);

    private EmployeeSwingApp() {
        super("PeopleDesk | Employee Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 650));
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        refreshEmployees();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                System.err.println("Could not load the system look and feel: " + exception.getMessage());
            }
            new EmployeeSwingApp().setVisible(true);
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(22, 30, 22, 30));

        JPanel titleBlock = new JPanel(new GridBagLayout());
        titleBlock.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        JLabel title = new JLabel("Employee Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        titleBlock.add(title, constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 0, 0, 0);
        JLabel subtitle = new JLabel("A clear view of your people and payroll");
        subtitle.setForeground(new Color(190, 202, 223));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        titleBlock.add(subtitle, constraints);

        JLabel badge = new JLabel("PEOPLE DESK", SwingConstants.CENTER);
        badge.setForeground(new Color(216, 228, 255));
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(87, 108, 146)),
                new EmptyBorder(8, 12, 8, 12)));
        header.add(titleBlock, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(18, 0));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(22, 24, 18, 24));
        content.add(createFormCard(), BorderLayout.WEST);
        content.add(createDirectoryCard(), BorderLayout.CENTER);
        return content;
    }

    private JPanel createFormCard() {
        JPanel card = cardPanel();
        card.setPreferredSize(new Dimension(285, 0));
        card.setLayout(new BorderLayout(0, 16));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 0, 7, 0);

        addFormRow(form, constraints, 0, "Employee ID", idField);
        addFormRow(form, constraints, 1, "Full name", nameField);
        addFormRow(form, constraints, 2, "Annual salary", salaryField);

        JLabel hint = new JLabel("<html><span style='color:#68748b'>Use the ID field to search,<br>update salary, or remove a record.</span></html>");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        constraints.gridy = 6;
        constraints.insets = new Insets(4, 0, 0, 0);
        form.add(hint, constraints);

        JPanel actions = new JPanel(new GridBagLayout());
        actions.setOpaque(false);
        GridBagConstraints actionConstraints = new GridBagConstraints();
        actionConstraints.gridx = 0;
        actionConstraints.weightx = 1;
        actionConstraints.fill = GridBagConstraints.HORIZONTAL;
        actionConstraints.insets = new Insets(0, 0, 9, 0);

        JButton addButton = actionButton("Add employee", BLUE, Color.WHITE);
        addButton.addActionListener(event -> addEmployee());
        actionConstraints.gridy = 0;
        actions.add(addButton, actionConstraints);

        JButton updateButton = actionButton("Update salary", new Color(231, 239, 255), NAVY);
        updateButton.addActionListener(event -> updateSalary());
        actionConstraints.gridy = 1;
        actions.add(updateButton, actionConstraints);

        JButton deleteButton = actionButton("Delete employee", new Color(255, 239, 238), new Color(174, 54, 50));
        deleteButton.addActionListener(event -> deleteEmployee());
        actionConstraints.gridy = 2;
        actions.add(deleteButton, actionConstraints);

        JButton clearButton = actionButton("Clear form", new Color(242, 244, 248), MUTED);
        clearButton.addActionListener(event -> clearForm());
        actionConstraints.gridy = 3;
        actionConstraints.insets = new Insets(0, 0, 0, 0);
        actions.add(clearButton, actionConstraints);

        JLabel sectionTitle = sectionTitle("Employee details");
        JPanel top = new JPanel(new BorderLayout(0, 16));
        top.setOpaque(false);
        top.add(sectionTitle, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        card.add(top, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private void addFormRow(JPanel form, GridBagConstraints constraints, int row, String label, JTextField field) {
        constraints.gridy = row * 2;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        fieldLabel.setForeground(NAVY);
        form.add(fieldLabel, constraints);

        constraints.gridy = row * 2 + 1;
        constraints.insets = new Insets(0, 0, 15, 0);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 236)),
                new EmptyBorder(9, 10, 9, 10)));
        form.add(field, constraints);
        constraints.insets = new Insets(0, 0, 7, 0);
    }

    private JPanel createDirectoryCard() {
        JPanel card = cardPanel();
        card.setLayout(new BorderLayout(0, 16));

        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);
        JPanel headings = new JPanel(new GridBagLayout());
        headings.setOpaque(false);
        GridBagConstraints headingConstraints = new GridBagConstraints();
        headingConstraints.gridx = 0;
        headingConstraints.anchor = GridBagConstraints.WEST;
        headingConstraints.gridy = 0;
        headings.add(sectionTitle("Employee directory"), headingConstraints);
        headingConstraints.gridy = 1;
        headingConstraints.insets = new Insets(4, 0, 0, 0);
        JLabel description = new JLabel("Search by ID or select a row to edit");
        description.setForeground(MUTED);
        description.setFont(new Font("SansSerif", Font.PLAIN, 12));
        headings.add(description, headingConstraints);
        toolbar.add(headings, BorderLayout.WEST);

        JPanel search = new JPanel(new GridBagLayout());
        search.setOpaque(false);
        JButton searchButton = actionButton("Search ID", new Color(231, 239, 255), BLUE);
        searchButton.addActionListener(event -> searchEmployee());
        JButton refreshButton = actionButton("Refresh", new Color(242, 244, 248), NAVY);
        refreshButton.addActionListener(event -> refreshEmployees());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.insets = new Insets(0, 0, 0, 8);
        search.add(searchButton, buttonConstraints);
        buttonConstraints.gridx = 1;
        buttonConstraints.insets = new Insets(0, 0, 0, 0);
        search.add(refreshButton, buttonConstraints);
        toolbar.add(search, BorderLayout.EAST);

        employeeTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        employeeTable.setRowHeight(38);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setShowVerticalLines(false);
        employeeTable.setGridColor(new Color(235, 238, 243));
        employeeTable.setFillsViewportHeight(true);
        employeeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        employeeTable.getTableHeader().setForeground(MUTED);
        employeeTable.getTableHeader().setBackground(new Color(247, 249, 252));
        employeeTable.getTableHeader().setPreferredSize(new Dimension(0, 38));
        employeeTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && employeeTable.getSelectedRow() >= 0) {
                int row = employeeTable.convertRowIndexToModel(employeeTable.getSelectedRow());
                idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                salaryField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        JScrollPane tableScroll = new JScrollPane(employeeTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(230, 234, 241)));
        tableScroll.getViewport().setBackground(Color.WHITE);
        card.add(toolbar, BorderLayout.NORTH);
        card.add(tableScroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 234, 241)),
                new EmptyBorder(9, 25, 9, 25)));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(MUTED);
        statusBar.add(statusLabel, BorderLayout.WEST);
        JLabel footer = new JLabel("Connected to MySQL via JDBC");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setForeground(MUTED);
        statusBar.add(footer, BorderLayout.EAST);
        return statusBar;
    }

    private JPanel cardPanel() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(231, 235, 242)),
                new EmptyBorder(20, 20, 20, 20)));
        return card;
    }

    private JLabel sectionTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(NAVY);
        return title;
    }

    private JButton actionButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background),
                new EmptyBorder(8, 12, 8, 12)));
        button.setOpaque(true);
        return button;
    }

    private void addEmployee() {
        int id = readId();
        if (id < 0) {
            return;
        }
        String name = nameField.getText().trim();
        if (name.isEmpty() || name.length() > 50) {
            showValidation("Enter a name between 1 and 50 characters.");
            return;
        }
        Double salary = readSalary();
        if (salary == null) {
            return;
        }
        Employee employee = new Employee(id, name, salary);
        runDatabaseTask("Adding employee...", () -> dao.createEmployee(employee), inserted -> {
            if (inserted) {
                clearForm();
                refreshEmployees();
                showMessage("Employee added successfully.");
            } else {
                showMessage("No employee was added.");
            }
        });
    }

    private void updateSalary() {
        int id = readId();
        if (id < 0) {
            return;
        }
        Double salary = readSalary();
        if (salary == null) {
            return;
        }
        runDatabaseTask("Updating salary...", () -> dao.updateEmployeeSalary(id, salary), updated -> {
            if (updated) {
                refreshEmployees();
                showMessage("Salary updated successfully.");
            } else {
                showMessage("No employee was found with ID " + id + ".");
            }
        });
    }

    private void deleteEmployee() {
        int id = readId();
        if (id < 0) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete employee " + id + "? This action cannot be undone.",
                "Confirm deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        runDatabaseTask("Deleting employee...", () -> dao.removeEmployee(id), deleted -> {
            if (deleted) {
                clearForm();
                refreshEmployees();
                showMessage("Employee deleted successfully.");
            } else {
                showMessage("No employee was found with ID " + id + ".");
            }
        });
    }

    private void searchEmployee() {
        int id = readId();
        if (id < 0) {
            return;
        }
        runDatabaseTask("Searching employees...", () -> dao.findEmployee(id), result -> {
            tableModel.setRowCount(0);
            if (result.isPresent()) {
                Employee employee = result.get();
                addEmployeeRow(employee);
                idField.setText(String.valueOf(employee.getId()));
                nameField.setText(employee.getName());
                salaryField.setText(String.valueOf(employee.getSalary()));
                showMessage("Employee found.");
            } else {
                showMessage("No employee was found with ID " + id + ".");
            }
        });
    }

    private void refreshEmployees() {
        runDatabaseTask("Loading employee directory...", dao::getEmployees, employees -> {
            tableModel.setRowCount(0);
            for (Employee employee : employees) {
                addEmployeeRow(employee);
            }
            showMessage(employees.size() + (employees.size() == 1
                    ? " employee in the directory." : " employees in the directory."));
        });
    }

    private void addEmployeeRow(Employee employee) {
        tableModel.addRow(new Object[]{
                employee.getId(),
                employee.getName(),
                employee.getSalary()
        });
    }

    private int readId() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            if (id <= 0) {
                showValidation("Employee ID must be a positive whole number.");
                return -1;
            }
            return id;
        } catch (NumberFormatException exception) {
            showValidation("Enter a valid whole-number employee ID.");
            return -1;
        }
    }

    private Double readSalary() {
        try {
            double salary = Double.parseDouble(salaryField.getText().trim());
            if (!Double.isFinite(salary) || salary < 0) {
                showValidation("Salary must be a finite, non-negative number.");
                return null;
            }
            return salary;
        } catch (NumberFormatException exception) {
            showValidation("Enter a valid salary amount.");
            return null;
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        salaryField.setText("");
        employeeTable.clearSelection();
    }

    private <T> void runDatabaseTask(String progress, Callable<T> task, Consumer<T> onSuccess) {
        statusLabel.setText(progress);
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    onSuccess.accept(result);
                } catch (Exception exception) {
                    Throwable cause = exception.getCause() == null ? exception : exception.getCause();
                    String message = cause instanceof SQLException
                            ? cause.getMessage()
                            : cause.getMessage() == null ? cause.toString() : cause.getMessage();
                    statusLabel.setText("Database operation failed.");
                    JOptionPane.showMessageDialog(EmployeeSwingApp.this,
                            "Could not complete the database operation.\n" + message,
                            "Database error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void showValidation(String message) {
        JOptionPane.showMessageDialog(this, message, "Check your input", JOptionPane.WARNING_MESSAGE);
    }

    private void showMessage(String message) {
        statusLabel.setText(message);
    }
}
