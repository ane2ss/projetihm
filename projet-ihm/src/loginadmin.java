
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class loginadmin {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loginadmin().start());
    }

    public void start() {
        // Configuration de la fenêtre principale
        JFrame frame = new JFrame("Gestion Véhicules et Clients");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Centrer la fenêtre

        // Initialisation du CardLayout et du panel principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Création des panels
        JPanel menuPanel = createMenuPanel();
        ClientManager clientManager = new ClientManager();
        JPanel clientManagementPanel = clientManager.createClientPanel(cardLayout, mainPanel);
        VehiculeManagementSystem vehiculeManager = new VehiculeManagementSystem();
        JPanel vehiculeManagementPanel = vehiculeManager.createVehiculeManagementSystem(cardLayout, mainPanel);
        ReservationManager reservationManager = new ReservationManager();
        JPanel reservationManagementPanel = reservationManager.createReservationPanel(cardLayout, mainPanel);
   
        RetourandInspection retourandinspection = new RetourandInspection(cardLayout, mainPanel);
        JPanel retourPanel = retourandinspection.creerPanelRetour();
        ReportandHistory reportandhistory = new ReportandHistory(cardLayout, mainPanel);
        JPanel reportPanel = reportandhistory.creerPanelReporting();

        // Ajout des panels au CardLayout
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(clientManagementPanel, "Client Management");
        mainPanel.add(vehiculeManagementPanel, "Vehicle Management");
        mainPanel.add(reservationManagementPanel, "Reservation Management");
        
    
        mainPanel.add(retourPanel, "Retour and Inspection");
        mainPanel.add(reportPanel, "Report and History");

        // Affichage du panel Menu par défaut
        cardLayout.show(mainPanel, "Menu");

        // Ajout du panel principal à la fenêtre et affichage
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Méthode pour créer le menu principal
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBackground(new Color(18, 18, 40)); // Fond bleu nuit

        // Section du titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(18, 18, 40)); // Couleur du titre
        JLabel titleLabel = new JLabel("Alger Car Dealership");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE); // Texte blanc
        titlePanel.add(titleLabel);
        menuPanel.add(titlePanel, BorderLayout.NORTH);

        // Section des boutons
        JPanel buttonSpanPanel = new JPanel();
        buttonSpanPanel.setLayout(new BoxLayout(buttonSpanPanel, BoxLayout.Y_AXIS));
        buttonSpanPanel.setBackground(new Color(18, 18, 40)); // Couleur de fond

        // Création et ajout des boutons
        JButton clientManagementButton = createStyledButton("Gestion des Clients");
        clientManagementButton.addActionListener(e -> cardLayout.show(mainPanel, "Client Management"));

        JButton vehicleManagementButton = createStyledButton("Gestion des Véhicules");
        vehicleManagementButton.addActionListener(e -> cardLayout.show(mainPanel, "Vehicle Management"));

        JButton reservationManagementButton = createStyledButton("Gestion des Réservations");
        reservationManagementButton.addActionListener(e -> cardLayout.show(mainPanel, "Reservation Management"));
        
 
        JButton retourandinspectionButton = createStyledButton("Gestion des Retours");
        retourandinspectionButton.addActionListener(e -> cardLayout.show(mainPanel, "Retour and Inspection"));
        
        JButton reportandhistoryButton = createStyledButton("L'Historique des Clients");
        reportandhistoryButton.addActionListener(e -> cardLayout.show(mainPanel, "Report and History"));

        // Centrer les boutons et ajouter de l'espace entre eux
        clientManagementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        vehicleManagementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationManagementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retourandinspectionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reportandhistoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonSpanPanel.add(Box.createVerticalGlue()); // Espacer les boutons
        buttonSpanPanel.add(reservationManagementButton);
        buttonSpanPanel.add(Box.createVerticalStrut(20));
        buttonSpanPanel.add(clientManagementButton);
        buttonSpanPanel.add(Box.createVerticalStrut(20));
        buttonSpanPanel.add(vehicleManagementButton); 
        buttonSpanPanel.add(Box.createVerticalStrut(20));
        buttonSpanPanel.add(retourandinspectionButton);
        buttonSpanPanel.add(Box.createVerticalStrut(20));
        buttonSpanPanel.add(reportandhistoryButton); 
        buttonSpanPanel.add(Box.createVerticalGlue()); 

        menuPanel.add(buttonSpanPanel, BorderLayout.CENTER);

        // Section du pied de page
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(18, 18, 40));
        JLabel footerLabel = new JLabel("Copyright © 2024 by Admins");
        footerLabel.setForeground(Color.WHITE); // Texte blanc
        footerPanel.add(footerLabel);
        menuPanel.add(footerPanel, BorderLayout.SOUTH);

        return menuPanel;
    }

    // Méthode pour créer un bouton stylisé avec effet de survol
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(58, 123, 255)); // Bleu
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50)); // Taille uniforme
        button.setMaximumSize(new Dimension(200, 50));
        button.setMinimumSize(new Dimension(200, 50));

        // Effet de survol
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(46, 98, 204)); // Bleu plus foncé
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(58, 123, 255)); // Bleu original
            }
        });

        return button;
    }
}