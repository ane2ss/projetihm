import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/locationvehicules";
    private static final String USER = "root"; // Default user in XAMPP
    private static final String PASSWORD = ""; // Leave blank if no password is set

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

//Méthode pour enregistrer un utilisateur
public static boolean registerUser(String name, String firstName, String email, String password, String role) {
    String query = "INSERT INTO Utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, name);
        ps.setString(2, firstName);
        ps.setString(3, email);
        ps.setString(4, password);
        ps.setString(5, role);
        return ps.executeUpdate() > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}

// Méthode pour authentifier un utilisateur
public static String authenticateUser(String email, String password) {
    String query = "SELECT role FROM Utilisateur WHERE email = ? AND mot_de_passe = ?";
    try (Connection connection = getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("role");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return null;
}

}

