import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ExtraExercise34_01 extends JFrame {

    // fields for all the data we need
    private JTextField tfID = new JTextField(9);
    private JTextField tfLastName = new JTextField(15);
    private JTextField tfFirstName = new JTextField(15);
    private JTextField tfMI = new JTextField(1);
    private JTextField tfAddress = new JTextField(20);
    private JTextField tfCity = new JTextField(20);
    private JTextField tfState = new JTextField(2);
    private JTextField tfTelephone = new JTextField(10);

    private JLabel lblStatus = new JLabel(" "); // for error/success messages

    // update these before running
    private static final String DB_URL = "jdbc:mysql://localhost/your_database_name";
    private static final String USER = "your_username";
    private static final String PASS = "your_password";

    public ExtraExercise34_01() {
        // setting up the form
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        formPanel.add(new JLabel("ID"));
        formPanel.add(tfID);

        // grouping name fields together
        formPanel.add(new JLabel("Last Name"));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        namePanel.add(tfLastName);
        namePanel.add(new JLabel("First Name"));
        namePanel.add(tfFirstName);
        namePanel.add(new JLabel("MI"));
        namePanel.add(tfMI);
        formPanel.add(namePanel);

        formPanel.add(new JLabel("Address"));
        formPanel.add(tfAddress);

        formPanel.add(new JLabel("City"));
        JPanel cityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        cityPanel.add(tfCity);
        cityPanel.add(new JLabel("State"));
        cityPanel.add(tfState);
        formPanel.add(cityPanel);

        formPanel.add(new JLabel("Telephone"));
        formPanel.add(tfTelephone);

        // buttons
        JPanel buttonPanel = new JPanel();
        JButton btnView   = new JButton("View");
        JButton btnInsert = new JButton("Insert");
        JButton btnUpdate = new JButton("Update");
        JButton btnClear  = new JButton("Clear");

        buttonPanel.add(btnView);
        buttonPanel.add(btnInsert);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnClear);

        setLayout(new BorderLayout(5, 5));
        add(lblStatus, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnView.addActionListener(e -> viewRecord());
        btnInsert.addActionListener(e -> insertRecord());
        btnUpdate.addActionListener(e -> updateRecord());
        btnClear.addActionListener(e -> clearFields());

        setTitle("ExtraExercise34_01");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // connects to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // looks up a record using the ID field
    private void viewRecord() {
        String id = tfID.getText().trim();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM Staff WHERE id = ?")) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tfLastName.setText(rs.getString("lastName"));
                tfFirstName.setText(rs.getString("firstName"));
                tfMI.setText(rs.getString("mi"));
                tfAddress.setText(rs.getString("address"));
                tfCity.setText(rs.getString("city"));
                tfState.setText(rs.getString("state"));
                tfTelephone.setText(rs.getString("telephone"));
                lblStatus.setText(" ");
            } else {
                lblStatus.setText("Record not found");
            }

        } catch (SQLException ex) {
            lblStatus.setText("Error: " + ex.getMessage());
        }
    }

    // adds a new record to the table
    private void insertRecord() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO Staff VALUES (?,?,?,?,?,?,?,?,null)")) {

            ps.setString(1, tfID.getText().trim());
            ps.setString(2, tfLastName.getText().trim());
            ps.setString(3, tfFirstName.getText().trim());
            ps.setString(4, tfMI.getText().trim());
            ps.setString(5, tfAddress.getText().trim());
            ps.setString(6, tfCity.getText().trim());
            ps.setString(7, tfState.getText().trim());
            ps.setString(8, tfTelephone.getText().trim());

            ps.executeUpdate();
            lblStatus.setText("Record inserted!");

        } catch (SQLException ex) {
            lblStatus.setText("Error: " + ex.getMessage());
        }
    }

    // updates an existing record, matches by ID
    private void updateRecord() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE Staff SET lastName=?, firstName=?, mi=?, " +
                 "address=?, city=?, state=?, telephone=? WHERE id=?")) {

            ps.setString(1, tfLastName.getText().trim());
            ps.setString(2, tfFirstName.getText().trim());
            ps.setString(3, tfMI.getText().trim());
            ps.setString(4, tfAddress.getText().trim());
            ps.setString(5, tfCity.getText().trim());
            ps.setString(6, tfState.getText().trim());
            ps.setString(7, tfTelephone.getText().trim());
            ps.setString(8, tfID.getText().trim());

            int rows = ps.executeUpdate();
            lblStatus.setText(rows > 0 ? "Record updated!" : "Record not found");

        } catch (SQLException ex) {
            lblStatus.setText("Error: " + ex.getMessage());
        }
    }

    // just clears everything out
    private void clearFields() {
        tfID.setText("");
        tfLastName.setText("");
        tfFirstName.setText("");
        tfMI.setText("");
        tfAddress.setText("");
        tfCity.setText("");
        tfState.setText("");
        tfTelephone.setText("");
        lblStatus.setText(" ");
    }

    public static void main(String[] args) {
        new ExtraExercise34_01();
    }
}
