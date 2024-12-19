
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class main {

    private JFrame frame; 
    private JFrame registerFrame; 
    private JFrame adminFrame; 
    private JFrame managerFrame; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(main::new);
    }

    public main() {
        initializeLogin();
    }
    // Creation de fenetre
    private void initializeLogin() {
        frame = new JFrame("Connexion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(18, 18, 40)); 

        // Création du panneau 
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.2); 

       
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); 
        titlePanel.add(titleLabel, BorderLayout.CENTER);

       
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new GridBagLayout());
        boxPanel.setBackground(new Color(30, 30, 60)); 
        boxPanel.setPreferredSize(new Dimension(400, 350));
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        boxPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        boxPanel.add(emailField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        boxPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        boxPanel.add(passwordField, gbc);

        // Bouton de connexion
        JButton loginButton = new JButton("Connexion");
        loginButton.setBackground(new Color(58, 123, 255)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createBevelBorder(15)); 
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                String role = DatabaseConnection.authenticateUser(email, password); // Utilisation de DatabaseConnection
                if (role == null) {
                    JOptionPane.showMessageDialog(frame, "Email ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else if (role.equals("Administrateur")) {
                    redirectToMain2(); // Rediriger vers l'application Administrateur
                } else if (role.equals("Gestionnaire")) {
                    redirectToMain(); // Rediriger vers l'application Gestionnaire
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        boxPanel.add(loginButton, gbc);

        // Lien pour s'inscrire
        JLabel registerLink = new JLabel("<HTML><U>S'inscrire</U></HTML>");
        registerLink.setForeground(new Color(135, 206, 250)); 
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                displayRegistrationForm();
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        boxPanel.add(registerLink, gbc);

        // Ajouter les panneaux 
        splitPane.setTopComponent(titlePanel); 
        splitPane.setBottomComponent(boxPanel); 

        frame.add(splitPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void redirectToMain2() {
        frame.dispose(); // Fermer la fenêtre de connexion
        loginadmin adminApp = new loginadmin(); 
        adminApp.start(); // Lancer l'application Administrateur
    }

    private void redirectToMain() {
        frame.dispose(); // Fermer la fenêtre de connexion
        logingestionnaire managerApp = new logingestionnaire(); 
        managerApp.start(); // Lancer l'application Gestionnaire
    }

    private void displayRegistrationForm() {
        registerFrame = new JFrame("Inscription");
        registerFrame.setSize(800, 600);
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setLayout(new BorderLayout());
        registerFrame.getContentPane().setBackground(new Color(18, 18, 40)); 

        // Création du panneau 
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.2); 

       
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Inscription", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); 
        titlePanel.add(titleLabel, BorderLayout.CENTER);

     
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(new Color(30, 30, 60)); 
        registerPanel.setPreferredSize(new Dimension(400, 400));
        registerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs du formulaire
        JTextField nameField = new JTextField(20);
        JTextField firstNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Administrateur", "Gestionnaire"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        registerPanel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        registerPanel.add(new JLabel("Rôle:"), gbc);
        gbc.gridx = 1;
        registerPanel.add(roleComboBox, gbc);

        // Creation bouton "S'inscrire" 
        JButton submitButton = new JButton("S'inscrire");
        submitButton.setBackground(new Color(58, 123, 255)); 
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createBevelBorder(15)); 
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String firstName = firstNameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (name.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else if (DatabaseConnection.registerUser(name, firstName, email, password, role)) {
                JOptionPane.showMessageDialog(registerFrame, "Inscription réussie !");
                registerFrame.dispose();
                new main(); 
            } else {
                JOptionPane.showMessageDialog(registerFrame, "Erreur d'inscription. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 5;
        registerPanel.add(submitButton, gbc);

        // Bouton de retour à la page de connexion
        JButton backToLoginButton = new JButton("Retour à la connexion");
        backToLoginButton.setBackground(new Color(255, 99, 71));
        backToLoginButton.setForeground(Color.WHITE);
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.setBorder(BorderFactory.createBevelBorder(15)); 
        backToLoginButton.setPreferredSize(new Dimension(150, 40));
        backToLoginButton.addActionListener(e -> {
            registerFrame.dispose(); // Fermer la fenêtre d'inscription
            new main(); // Ouvrir la page de connexion
        });

        gbc.gridx = 1;
        gbc.gridy = 6;
        registerPanel.add(backToLoginButton, gbc);

        // Ajouter les panneaux au splitPane pour la page d'inscription
        splitPane.setTopComponent(titlePanel); // Partie nord avec le titre "Inscription"
        splitPane.setBottomComponent(registerPanel); // Partie sud avec le formulaire d'inscription

        registerFrame.add(splitPane, BorderLayout.CENTER);
        registerFrame.setVisible(true);
    }
}