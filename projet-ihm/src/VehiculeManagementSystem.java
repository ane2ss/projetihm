import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VehiculeManagementSystem {
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField brandField, modelField, yearField, capacityField, fuelField, priceField, searchField;
    private JComboBox<String> statusCombo;

    public JPanel createVehiculeManagementSystem(CardLayout cardLayout, JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Ajouter le bouton "Retour" en haut
        JButton goBackButton = new JButton("Retour");
        goBackButton.setBackground(new Color(255, 102, 102)); // Rouge clair
        goBackButton.setForeground(Color.WHITE); // Texte blanc
        goBackButton.setFocusPainted(false);
        goBackButton.setPreferredSize(new Dimension(100, 30)); // Bouton plus court
        goBackButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        // Effet hover pour le bouton "Retour"
        goBackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                goBackButton.setBackground(new Color(255, 77, 77)); // Rouge plus foncé au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                goBackButton.setBackground(new Color(255, 102, 102)); // Retour au rouge clair
            }
        });

        panel.add(goBackButton, BorderLayout.NORTH);

        // Panel principal pour contenir tout
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // Panel pour le formulaire
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        brandField = new JTextField();
        modelField = new JTextField();
        yearField = new JTextField();
        capacityField = new JTextField();
        fuelField = new JTextField();
        priceField = new JTextField();
        statusCombo = new JComboBox<>(new String[]{"Disponible", "Louée", "En maintenance"});

        formPanel.add(new JLabel("Marque:"));
        formPanel.add(brandField);
        formPanel.add(new JLabel("Modèle:"));
        formPanel.add(modelField);
        formPanel.add(new JLabel("Année:"));
        formPanel.add(yearField);
        formPanel.add(new JLabel("Capacité:"));
        formPanel.add(capacityField);
        formPanel.add(new JLabel("Type de carburant:"));
        formPanel.add(fuelField);
        formPanel.add(new JLabel("Prix de location par jour:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("État:"));
        formPanel.add(statusCombo);

        mainContentPanel.add(formPanel, BorderLayout.NORTH);

        // Table des véhicules
        tableModel = new DefaultTableModel(new String[]{"ID", "Marque", "Modèle", "Année", "Capacité", "Carburant", "Prix", "État"}, 0);
        vehicleTable = new JTable(tableModel);

        // Personnaliser le style du tableau
        vehicleTable.setRowHeight(25);
        vehicleTable.setFont(new Font("Arial", Font.PLAIN, 14));
        vehicleTable.setGridColor(Color.LIGHT_GRAY);
        vehicleTable.getTableHeader().setBackground(new Color(51, 102, 204)); // Bleu foncé
        vehicleTable.getTableHeader().setForeground(Color.WHITE); // Texte blanc
        vehicleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        ((DefaultTableCellRenderer) vehicleTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane tableScrollPane = new JScrollPane(vehicleTable);
        mainContentPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Mettre à jour");
        JButton deleteButton = new JButton("Supprimer");
        JButton searchButton = new JButton("Rechercher");
        JButton backButton = new JButton("Retour");

        searchField = new JTextField(15);

        // Ajouter les boutons au panneau
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        buttonPanel.add(backButton);

        mainContentPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(mainContentPanel, BorderLayout.CENTER);

        // Style des boutons avec couleur et hover
        styleButton(addButton, new Color(102, 178, 255), new Color(77, 140, 230)); // Bleu clair
        styleButton(updateButton, new Color(102, 178, 255), new Color(77, 140, 230)); // Bleu clair
        styleButton(deleteButton, new Color(102, 178, 255), new Color(77, 140, 230)); // Bleu clair
        styleButton(searchButton, new Color(102, 178, 255), new Color(77, 140, 230)); // Bleu clair
        styleButton(backButton, new Color(255, 102, 102), new Color(255, 77, 77)); // Rouge clair pour le bouton retour

        // Gestion des événements
        addButton.addActionListener(e -> addVehicle());
        updateButton.addActionListener(e -> updateVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        searchButton.addActionListener(e -> searchVehicle());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        refreshTable();

        return panel;
    }

    private void styleButton(JButton button, Color normalColor, Color hoverColor) {
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE); // Texte blanc
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 30)); // Bouton plus court
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor); // Couleur au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor); // Retour à la couleur normale
            }
        });
    }

    private void addVehicle() {
        String marque = brandField.getText();
        String modele = modelField.getText();
        String annee = yearField.getText();
        String capacite = capacityField.getText();
        String carburant = fuelField.getText();
        String prix = priceField.getText();
        String etat = (String) statusCombo.getSelectedItem();

        if (marque.isEmpty() || modele.isEmpty() || annee.isEmpty() || capacite.isEmpty() || carburant.isEmpty() || prix.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO vehicules (marque, modele, annee, capacite, carburant, prix_location, etat) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, marque);
            stmt.setString(2, modele);
            stmt.setString(3, annee);
            stmt.setString(4, capacite);
            stmt.setString(5, carburant);
            stmt.setString(6, prix);
            stmt.setString(7, etat);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Véhicule ajouté avec succès !");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du véhicule.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du véhicule.");
        }
    }

    private void updateVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un véhicule.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String brand = brandField.getText();
            String model = modelField.getText();
            int year = Integer.parseInt(yearField.getText());
            String capacity = capacityField.getText();
            String fuel = fuelField.getText();
            double price = Double.parseDouble(priceField.getText());
            String status = (String) statusCombo.getSelectedItem();

            String query = "UPDATE vehicules SET marque = ?, modele = ?, annee = ?, capacite = ?, carburant = ?, prix_location = ?, etat = ? WHERE id_vehicule = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, brand);
            stmt.setString(2, model);
            stmt.setInt(3, year);
            stmt.setString(4, capacity);
            stmt.setString(5, fuel);
            stmt.setDouble(6, price);
            stmt.setString(7, status);
            stmt.setInt(8, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Véhicule mis à jour avec succès !");
            clearForm();
            refreshTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du véhicule.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un véhicule.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

            String query = "DELETE FROM vehicules WHERE id_vehicule = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Véhicule supprimé avec succès !");
            refreshTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du véhicule.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchVehicle() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un mot-clé pour la recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM vehicules WHERE marque LIKE ? OR modele LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear the table
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee"),
                        rs.getString("capacite"),
                        rs.getString("carburant"),
                        rs.getDouble("prix_location"),
                        rs.getString("etat")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        capacityField.setText("");
        fuelField.setText("");
        priceField.setText("");
        statusCombo.setSelectedIndex(0);
    }

    private void refreshTable() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM vehicules";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear the table
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_vehicule"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee"),
                        rs.getString("capacite"),
                        rs.getString("carburant"),
                        rs.getDouble("prix_location"),
                        rs.getString("etat")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du rafraîchissement de la table.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
