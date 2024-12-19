import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RetourandInspection {
    private final DatabaseConnection dbConfig = new DatabaseConnection();
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Constructor to accept CardLayout and JPanel
    public RetourandInspection(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    public JPanel creerPanelRetour() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Retour et Inspection du Véhicule", JLabel.CENTER);
        titleLabel.setFont(new Font("SERIF", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form for handling return and inspection
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.add(new JLabel("ID Véhicule:"));
        JTextField vehiculeIdField = new JTextField();
        formPanel.add(vehiculeIdField);

        formPanel.add(new JLabel("Condition:"));
        JComboBox<String> conditionCombo = new JComboBox<>(new String[]{"Bonne état", "Endommagé"});
        formPanel.add(conditionCombo);

        formPanel.add(new JLabel("Frais supplémentaires:"));
        JTextField fraisField = new JTextField();
        formPanel.add(fraisField);

        JButton returnButton = new JButton("Processus de Retour");
        returnButton.addActionListener(e -> processReturn(vehiculeIdField, conditionCombo, fraisField));
        formPanel.add(new JLabel());
        formPanel.add(returnButton);

        panel.add(formPanel, BorderLayout.CENTER);

        // Add Retour (back) button
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        panel.add(retourButton, BorderLayout.NORTH); // Place the retour button at the top

        return panel;
    }

    private void processReturn(JTextField vehiculeIdField, JComboBox<String> conditionCombo, JTextField fraisField) {
        // Your logic to handle the return processing
    }
}
