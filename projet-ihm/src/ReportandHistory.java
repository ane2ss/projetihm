import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReportandHistory {

    private final DatabaseConnection dbConfig = new DatabaseConnection();
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Constructor to accept CardLayout and JPanel
    public ReportandHistory(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    public JPanel creerPanelReporting() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode("#F5F5F5"));

        JLabel titleLabel = new JLabel("Rapports et Historique", JLabel.CENTER);
        titleLabel.setFont(new Font("SERIF", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#2C3E50"));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Result area to display reports
        JTextArea resultArea = new JTextArea(15, 15);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons for report types
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        JButton topVehiclesButton = new JButton("Véhicules les plus loués");
        topVehiclesButton.addActionListener(e -> showTopVehicles(resultArea));

        JButton clientHistoryButton = new JButton("Historique client");
        clientHistoryButton.addActionListener(e -> showClientHistory(resultArea));

        buttonsPanel.add(topVehiclesButton);
        buttonsPanel.add(clientHistoryButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add Retour (back) button
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        panel.add(retourButton, BorderLayout.NORTH); // Place the retour button at the top

        return panel;
    }

    private void showTopVehicles(JTextArea resultArea) {
        String query = """
                SELECT vehicules.id_vehicule, vehicules.marque, vehicules.modele, COUNT(*) AS locations
                FROM reservations
                JOIN vehicule ON reservations.id_vehicule = vehicules.id_vehicule
                GROUP BY vehicules.id_vehicule, vehicules.marque, vehicules.modele
                ORDER BY locations DESC LIMIT 5
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            resultArea.setText("Top 5 des véhicules les plus loués :\n");
            while (rs.next()) {
                resultArea.append(String.format("ID: %d, Marque: %s, Modèle: %s, Locations: %d%n",
                        rs.getInt("id_vehicule"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("locations")));
            }
        } catch (SQLException e) {
            resultArea.append("Erreur lors de la récupération des données.");
        }
    }

    private void showClientHistory(JTextArea resultArea) {
        String clientId = JOptionPane.showInputDialog("Enter Client ID");
        if (clientId != null && !clientId.trim().isEmpty()) {
            String query = """
                    SELECT reservations.id_reservation, vehicules.marque, vehicules.modele, reservations.date_debut, reservations.date_fin
                    FROM reservations
                    JOIN vehicules ON reservations.id_vehicule = vehicules.id_vehicule
                    WHERE reservations.id_client = ?
                    """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(clientId));
                ResultSet rs = stmt.executeQuery();
                resultArea.setText("Historique des réservations :\n");
                while (rs.next()) {
                    resultArea.append(String.format("ID: %d, Véhicule: %s %s, Du: %s, Au: %s%n",
                            rs.getInt("id_reservation"),
                            rs.getString("marque"),
                            rs.getString("modele"),
                            rs.getDate("date_debut"),
                            rs.getDate("date_fin")));
                }
            } catch (SQLException ex) {
                resultArea.append("Erreur : " + ex.getMessage());
            }
        }
    }
}
