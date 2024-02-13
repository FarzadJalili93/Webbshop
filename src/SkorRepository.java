import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkorRepository {
    private Connection connection;

    public SkorRepository(Connection connection) {
        this.connection = connection;
    }

    public void printTillgängligaSkor() throws SQLException {  //hämtar och skriver ut information om tillgängliga skor från databasen.
        String query = "SELECT SkoID, Märke, Pris, AntalLager FROM Skor WHERE AntalLager > 0";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("Tillgängliga produkter:");
            while (resultSet.next()) {
                int shoeId = resultSet.getInt("SkoID");
                String brand = resultSet.getString("Märke");
                double price = resultSet.getDouble("Pris");
                int stock = resultSet.getInt("AntalLager");
                System.out.println(shoeId + ": " + brand + ", Pris: " + price + ", Lager: " + stock);
            }
        }
    }

    public void addToCart(int kundId, int produktId, String färg, String storlek) throws SQLException {
        String query = "{call AddToCart(?, ?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setInt(1, kundId);
            callableStatement.setNull(2, Types.INTEGER);
            callableStatement.setInt(3, produktId);
            callableStatement.setString(4, färg);
            callableStatement.setString(5, storlek);
            callableStatement.execute();
        }
    }
}