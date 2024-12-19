import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ClientManager {

    public JPanel createClientPanel(CardLayout cardLayout, JPanel mainPanel) {
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

        // Tableau des clients
        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Numéro Permis"};
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

        // Formulaire pour ajouter un client
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10)); // Ajuster l'espacement
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding autour du formulaire

        JTextField tfNom = new JTextField();
        JTextField tfPrenom = new JTextField();
        JTextField tfEmail = new JTextField();
        JTextField tfTelephone = new JTextField();
        JTextField tfNumeroPermis = new JTextField();

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(tfNom);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(tfPrenom);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(tfEmail);
        formPanel.add(new JLabel("Téléphone:"));
        formPanel.add(tfTelephone);
        formPanel.add(new JLabel("Numéro de Permis:"));
        formPanel.add(tfNumeroPermis);

        // Bouton "Ajouter Client" stylé en bleu clair
        JButton btnAddClient = new JButton("Ajouter Client");
        btnAddClient.setBackground(new Color(102, 178, 255)); // Bleu clair
        btnAddClient.setForeground(Color.WHITE); // Texte blanc
        btnAddClient.setFocusPainted(false);
        btnAddClient.setPreferredSize(new Dimension(140, 30)); // Bouton plus court

        // Effet hover pour le bouton "Ajouter Client"
        btnAddClient.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddClient.setBackground(new Color(77, 140, 230)); // Bleu plus foncé au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddClient.setBackground(new Color(102, 178, 255)); // Retour au bleu clair
            }
        });

        formPanel.add(btnAddClient);
        panel.add(formPanel, BorderLayout.SOUTH);

        // Charger les clients existants depuis la base de données
        loadClients(model);

        btnAddClient.addActionListener(e -> {
            try {
                String nom = tfNom.getText();
                String prenom = tfPrenom.getText();
                String email = tfEmail.getText();
                String telephone = tfTelephone.getText();
                String numeroPermis = tfNumeroPermis.getText();

                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || numeroPermis.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                addClientToDatabase(nom, prenom, email, telephone, numeroPermis, model, tfNom, tfPrenom, tfEmail, tfTelephone, tfNumeroPermis);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du client.");
            }
        });

        return panel;
    }

    private void addClientToDatabase(String nom, String prenom, String email, String telephone, String numeroPermis,
                                     DefaultTableModel model, JTextField tfNom, JTextField tfPrenom,
                                     JTextField tfEmail, JTextField tfTelephone, JTextField tfNumeroPermis) throws SQLException {
        String query = "INSERT INTO clients (nom, prenom, email, telephone, numero_permis) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, telephone);
            stmt.setString(5, numeroPermis);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Client ajouté avec succès !");
                loadClients(model);
                tfNom.setText("");
                tfPrenom.setText("");
                tfEmail.setText("");
                tfTelephone.setText("");
                tfNumeroPermis.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Aucun client ajouté.");
            }
        }
    }

    private static void loadClients(DefaultTableModel model) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM clients";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getString("numero_permis")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des clients.");
        }
    }
}
