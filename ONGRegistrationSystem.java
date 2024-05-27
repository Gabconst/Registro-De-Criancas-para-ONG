import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class ONGRegistrationSystem extends JFrame implements ActionListener {
    private JTextField childNameField, guardianNameField, ageField, shoeSizeField, clothingSizeField, addressField;
    private JButton registerButton, searchButton, updateButton, deleteButton, listButton;
    private JTextArea displayArea;

    private ArrayList<String[]> childrenList;

    public ONGRegistrationSystem() {
        setTitle("ONG Registration System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Child's Name:"));
        childNameField = new JTextField();
        inputPanel.add(childNameField);
        inputPanel.add(new JLabel("Guardian's Name:"));
        guardianNameField = new JTextField();
        inputPanel.add(guardianNameField);
        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Shoe Size:"));
        shoeSizeField = new JTextField();
        inputPanel.add(shoeSizeField);
        inputPanel.add(new JLabel("Clothing Size:"));
        clothingSizeField = new JTextField();
        inputPanel.add(clothingSizeField);
        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        buttonPanel.add(searchButton);
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        buttonPanel.add(updateButton);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);
        listButton = new JButton("List All");
        listButton.addActionListener(this);
        buttonPanel.add(listButton);

        displayArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        childrenList = new ArrayList<>();
        loadDatabase();

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            registerChild();
        } else if (e.getSource() == searchButton) {
            searchChild();
        } else if (e.getSource() == updateButton) {
            updateChild();
        } else if (e.getSource() == deleteButton) {
            deleteChild();
        } else if (e.getSource() == listButton) {
            listAllChildren();
        }
    }

    private void registerChild() {
        String childName = childNameField.getText();
        String guardianName = guardianNameField.getText();
        String age = ageField.getText();
        String shoeSize = shoeSizeField.getText();
        String clothingSize = clothingSizeField.getText();
        String address = addressField.getText();

        try (FileWriter writer = new FileWriter("children_database.txt", true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(childName + "," + guardianName + "," + age + "," + shoeSize + "," + clothingSize + "," + address);
            bw.newLine();
            bw.flush();
            displayArea.setText("Child registered successfully!");
            childrenList.add(new String[]{childName, guardianName, age, shoeSize, clothingSize, address});
            clearFields();
        } catch (IOException ex) {
            ex.printStackTrace();
            displayArea.setText("Error occurred while registering child.");
        }
    }

    private void searchChild() {
        String searchName = childNameField.getText();
        for (String[] child : childrenList) {
            if (child[0].equals(searchName)) {
                displayArea.setText("Child's Name: " + child[0] + "\nGuardian's Name: " + child[1] + "\nAge: " + child[2] +
                        "\nShoe Size: " + child[3] + "\nClothing Size: " + child[4] + "\nAddress: " + child[5]);
                return;
            }
        }
        displayArea.setText("Child not found.");
    }

    private void updateChild() {
        String searchName = childNameField.getText();
        for (String[] child : childrenList) {
            if (child[0].equals(searchName)) {
                child[1] = guardianNameField.getText();
                child[2] = ageField.getText();
                child[3] = shoeSizeField.getText();
                child[4] = clothingSizeField.getText();
                child[5] = addressField.getText();

                updateDatabase();
                displayArea.setText("Child '" + searchName + "' updated successfully.");
                return;
            }
        }
        displayArea.setText("Child '" + searchName + "' not found.");
    }

    private void deleteChild() {
        String deleteName = childNameField.getText();
        for (int i = 0; i < childrenList.size(); i++) {
            if (childrenList.get(i)[0].equals(deleteName)) {
                childrenList.remove(i);
                updateDatabase();
                displayArea.setText("Child '" + deleteName + "' deleted successfully.");
                return;
            }
        }
        displayArea.setText("Child '" + deleteName + "' not found.");
    }

    private void listAllChildren() {
        StringBuilder childrenInfo = new StringBuilder();
        for (String[] child : childrenList) {
            childrenInfo.append("Child's Name: ").append(child[0]).append("\n");
        }
        displayArea.setText(childrenInfo.toString());
    }

    private void clearFields() {
        childNameField.setText("");
        guardianNameField.setText("");
        ageField.setText("");
        shoeSizeField.setText("");
        clothingSizeField.setText("");
        addressField.setText("");
    }

    private void loadDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader("children_database.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                childrenList.add(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            displayArea.setText("Error occurred while loading database.");
        }
    }

    private void updateDatabase() {
        try (FileWriter writer = new FileWriter("children_database.txt");
             BufferedWriter bw = new BufferedWriter(writer)) {
            for (String[] child : childrenList) {
                bw.write(child[0] + "," + child[1] + "," + child[2] + "," + child[3] + "," + child[4] + "," + child[5]);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            displayArea.setText("Error occurred while updating database.");
        }
    }

    public static void main(String[] args) {
        new ONGRegistrationSystem();
    }
}