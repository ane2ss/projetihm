import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;

class ReservationManager {

    public JPanel createReservationPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Bouton "Retour" stylé en rouge clair
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

        // Tableau des réservations
        String[] columnNames = {"ID Réservation", "Marque", "Modèle", "ID Client", "Date Début", "Date Fin", "Montant", "Statut"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        // Personnaliser le style du tableau
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);

        // En-tête avec bleu foncé
        table.getTableHeader().setBackground(new Color(51, 102, 204)); // Bleu foncé
        table.getTableHeader().setForeground(Color.WHITE); // Texte blanc
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Formulaire pour ajouter une réservation
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2, 10, 10)); // Ajuster l'espacement
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding autour du formulaire

        JComboBox<String> marqueCombo = new JComboBox<>();
        JComboBox<String> modeleCombo = new JComboBox<>();
        JTextField tfIdClient = new JTextField();
        JTextField tfDateDebut = new JTextField();
        JTextField tfDateFin = new JTextField();

        formPanel.add(new JLabel("Marque:"));
        formPanel.add(marqueCombo);
        formPanel.add(new JLabel("Modèle:"));
        formPanel.add(modeleCombo);
        formPanel.add(new JLabel("ID Client:"));
        formPanel.add(tfIdClient);
        formPanel.add(new JLabel("Date Début (YYYY-MM-DD):"));
        formPanel.add(tfDateDebut);
        formPanel.add(new JLabel("Date Fin (YYYY-MM-DD):"));
        formPanel.add(tfDateFin);

        // Bouton "Ajouter Réservation" stylé en bleu clair
        JButton btnAddReservation = new JButton("Ajouter Réservation");
        btnAddReservation.setBackground(new Color(102, 178, 255)); // Bleu clair
        btnAddReservation.setForeground(Color.WHITE); // Texte blanc
        btnAddReservation.setFocusPainted(false);
        btnAddReservation.setPreferredSize(new Dimension(160, 30)); // Bouton plus court

        // Effet hover pour le bouton "Ajouter Réservation"
        btnAddReservation.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddReservation.setBackground(new Color(77, 140, 230)); // Bleu plus foncé au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddReservation.setBackground(new Color(102, 178, 255)); // Retour au bleu clair
            }
        });

        formPanel.add(btnAddReservation);
        panel.add(formPanel, BorderLayout.SOUTH);

        // Charger les marques et les réservations existantes
        loadMarques(marqueCombo);
        loadReservations(model);

        // Dynamically load models when a brand is selected
        marqueCombo.addActionListener(e -> loadModelsForMarque(marqueCombo, modeleCombo));

        // Ajouter une réservation à la base de données
        btnAddReservation.addActionListener(e -> {
            try {
                String selectedMarque = (String) marqueCombo.getSelectedItem();
                String selectedModele = (String) modeleCombo.getSelectedItem();
                int idClient = Integer.parseInt(tfIdClient.getText());
                String dateDebut = tfDateDebut.getText();
                String dateFin = tfDateFin.getText();

                if (!isClientValid(idClient)) {
                    JOptionPane.showMessageDialog(null, "Client non existant. Veuillez entrer un ID Client valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date startDate = sdf.parse(dateDebut);
                java.util.Date endDate = sdf.parse(dateFin);
                long diffInDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

                if (diffInDays <= 0) {
                    JOptionPane.showMessageDialog(null, "La date de fin doit être postérieure à la date de début.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double prixLocationJour;
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "SELECT id_vehicule, prix_location FROM vehicules WHERE marque = ? AND modele = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, selectedMarque);
                    stmt.setString(2, selectedModele);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        int idVehicule = rs.getInt("id_vehicule");
                        prixLocationJour = rs.getDouble("prix_location");

                        if (isVehicleReserved(idVehicule, dateDebut, dateFin)) {
                            JOptionPane.showMessageDialog(null, "Le véhicule est déjà réservé pour cette période.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        double montant = diffInDays * prixLocationJour;

                        // Insérer la réservation dans la base de données
                        String insertQuery = "INSERT INTO reservations (id_vehicule, id_client, date_debut, date_fin, montant, statut) "
                                + "VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                        insertStmt.setInt(1, idVehicule);
                        insertStmt.setInt(2, idClient);
                        insertStmt.setDate(3, java.sql.Date.valueOf(dateDebut));
                        insertStmt.setDate(4, java.sql.Date.valueOf(dateFin));
                        insertStmt.setDouble(5, montant);
                        insertStmt.setString(6, "en cours");
                        insertStmt.executeUpdate();

                        // Ajouter la réservation au tableau
                        model.addRow(new Object[]{model.getRowCount() + 1, selectedMarque, selectedModele, idClient, dateDebut, dateFin, montant, "en cours"});
                        JOptionPane.showMessageDialog(null, "Réservation ajoutée avec succès !");
                    } else {
                        JOptionPane.showMessageDialog(null, "Véhicule introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "L'ID client doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Les dates doivent être au format YYYY-MM-DD.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la réservation.");
            }
        });

        return panel;
    }

    // Validation de l'ID client
    private static boolean isClientValid(int idClient) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT 1 FROM clients WHERE id_client = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Vérification si le véhicule est déjà réservé
    private static boolean isVehicleReserved(int idVehicule, String dateDebut, String dateFin) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT 1 FROM reservations WHERE id_vehicule = ? AND "
                    + "((date_debut <= ? AND date_fin >= ?) OR (date_debut <= ? AND date_fin >= ?))";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idVehicule);
            stmt.setString(2, dateDebut);
            stmt.setString(3, dateDebut);
            stmt.setString(4, dateFin);
            stmt.setString(5, dateFin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Chargement des marques dans le combo box
    private static void loadMarques(JComboBox<String> marqueCombo) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT DISTINCT marque FROM vehicules";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                marqueCombo.addItem(rs.getString("marque"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des marques.");
        }
    }

    // Chargement des modèles pour une marque sélectionnée
    private static void loadModelsForMarque(JComboBox<String> marqueCombo, JComboBox<String> modeleCombo) {
        String selectedMarque = (String) marqueCombo.getSelectedItem();
        if (selectedMarque != null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT modele FROM vehicules WHERE marque = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, selectedMarque);
                ResultSet rs = stmt.executeQuery();

                modeleCombo.removeAllItems();
                while (rs.next()) {
                    modeleCombo.addItem(rs.getString("modele"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors du chargement des modèles.");
            }
        }
    }

    // Chargement des réservations existantes dans le tableau
    private static void loadReservations(DefaultTableModel model) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT r.id_reservation, v.marque, v.modele, r.id_client, r.date_debut, r.date_fin, r.montant, r.statut "
                         + "FROM reservations r JOIN vehicules v ON r.id_vehicule = v.id_vehicule";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_reservation"),
                    rs.getString("marque"),
                    rs.getString("modele"),
                    rs.getInt("id_client"),
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getDouble("montant"),
                    rs.getString("statut")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des réservations.");
        }
    }
}
