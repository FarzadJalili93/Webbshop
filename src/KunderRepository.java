import java.sql.*;

public class KunderRepository {
    private Connection connection;

    public KunderRepository(Connection connection) {
        this.connection = connection;
    }

    public String getLösenord(String username) throws SQLException { //tar emot användarnamnet och returnerar lösenordet för den givna användaren från databasen.
        String lösenord = null;  // om användaren inte finns returnerar vi null.
        String query = "SELECT Lösenord FROM Kunder WHERE Namn = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lösenord = resultSet.getString("Lösenord");
                }
            }
        }
        return lösenord;
    }
}
