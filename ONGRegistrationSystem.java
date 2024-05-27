import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class ONGRegistrationSystem extends JFrame implements ActionListener {
    private JTextField childNameField, guardianNameField, ageField, shoeSizeField, clothingSizeField, addressField, sponsorNameField;
    private JButton registerButton, searchButton, updateButton, deleteButton, listButton;
    private JTextArea displayArea;

    private ArrayList<String[]> childrenList;

    public ONGRegistrationSystem() {
        setTitle("Sistema de Registro da ONG");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        inputPanel.add(new JLabel("Nome da Criança:"));
        childNameField = new JTextField();
        inputPanel.add(childNameField);
        inputPanel.add(new JLabel("Nome do Guardião:"));
        guardianNameField = new JTextField();
        inputPanel.add(guardianNameField);
        inputPanel.add(new JLabel("Idade:"));
        ageField = new JTextField();
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Tamanho do Sapato:"));
        shoeSizeField = new JTextField();
        inputPanel.add(shoeSizeField);
        inputPanel.add(new JLabel("Tamanho da Roupa:"));
        clothingSizeField = new JTextField();
        inputPanel.add(clothingSizeField);
        inputPanel.add(new JLabel("Endereço:"));
        addressField = new JTextField();
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Nome do Padrinho:"));
        sponsorNameField = new JTextField();
        inputPanel.add(sponsorNameField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("Registrar");
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);
        searchButton = new JButton("Pesquisar");
        searchButton.addActionListener(this);
        buttonPanel.add(searchButton);
        updateButton = new JButton("Atualizar");
        updateButton.addActionListener(this);
        buttonPanel.add(updateButton);
        deleteButton = new JButton("Deletar");
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);
        listButton = new JButton("Listar Todos");
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
        String sponsorName = sponsorNameField.getText();

        try (FileWriter writer = new FileWriter("children_database.txt", true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(childName + "," + guardianName + "," + age + "," + shoeSize + "," + clothingSize + "," + address + "," + sponsorName);
            bw.newLine();
            bw.flush();
            displayArea.setText("Criança registrada com sucesso!");
            childrenList.add(new String[]{childName, guardianName, age, shoeSize, clothingSize, address, sponsorName});
            clearFields();
        } catch (IOException ex) {
            ex.printStackTrace();
            displayArea.setText("Ocorreu um erro ao registrar a criança.");
        }
    }

    private void searchChild() {
        String searchName = childNameField.getText();
        for (String[] child : childrenList) {
            if (child[0].equals(searchName)) {
                displayArea.setText("Nome da Criança: " + child[0] + "\nNome do Guardião: " + child[1] + "\nIdade: " + child[2] +
                        "\nTamanho do Sapato: " + child[3] + "\nTamanho da Roupa: " + child[4] + "\nEndereço: " + child[5] + "\nNome do Padrinho: " + child[6]);
                return;
            }
        }
        displayArea.setText("Criança não encontrada.");
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
                child[6] = sponsorNameField.getText();

                updateDatabase();
                displayArea.setText("Criança '" + searchName + "' atualizada com sucesso.");
                return;
            }
        }
        displayArea.setText("Criança '" + searchName + "' não encontrada.");
    }

    private void deleteChild() {
        String deleteName = childNameField.getText();
        for (int i = 0; i < childrenList.size(); i++) {
            if (childrenList.get(i)[0].equals(deleteName)) {
                childrenList.remove(i);
                updateDatabase();
                displayArea.setText("Criança '" + deleteName + "' deletada com sucesso.");
                return;
            }
        }
        displayArea.setText("Criança '" + deleteName + "' não encontrada.");
    }

    private void listAllChildren() {
        StringBuilder childrenInfo = new StringBuilder();
        for (String[] child : childrenList) {
            childrenInfo.append("Nome da Criança: ").append(child[0]).append("\n");
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
        sponsorNameField.setText("");
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
            displayArea.setText("Ocorreu um erro ao carregar o banco de dados.");
        }
    }

    private void updateDatabase() {
        try (FileWriter writer = new FileWriter("children_database.txt");
             BufferedWriter bw = new BufferedWriter(writer)) {
            for (String[] child : childrenList) {
                bw.write(child[0] + "," + child[1] + "," + child[2] + "," + child[3] + "," + child[4] + "," + child[5] + "," + child[6]);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            displayArea.setText("Ocorreu um erro ao atualizar o banco de dados.");
        }
    }

    public static void main(String[] args) {
        new ONGRegistrationSystem();
    }
}
