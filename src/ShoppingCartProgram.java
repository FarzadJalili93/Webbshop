import java.sql.*;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ShoppingCartProgram {
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/WebShopDB";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            System.out.println("Anslutning till databasen lyckades!");

            Scanner scanner = new Scanner(System.in);
            Supplier<String> inputSupplier = () -> {
                System.out.print("Ange ditt användarnamn: ");
                return scanner.nextLine();
            };

            String username = inputSupplier.get();

            Function<String, String> passwordPrompter = (message) -> {
                System.out.print(message);
                return scanner.nextLine();
            };

            String password = passwordPrompter.apply("Ange ditt lösenord: ");

            KunderRepository kunderRepository = new KunderRepository(connection);
            String storedPassword = kunderRepository.getLösenord(username);

            if (storedPassword != null && storedPassword.equals(password)) {
                System.out.println("Inloggning lyckades!");

                SkorRepository skorRepository = new SkorRepository(connection);
                skorRepository.printTillgängligaSkor();

                System.out.print("Ange ID för den produkt du vill lägga till i din kundvagn: ");
                int productId = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Ange färg för produkten: ");
                String productColor = scanner.nextLine();

                System.out.print("Ange storlek för produkten: ");
                String productSize = scanner.nextLine();

                skorRepository.addToCart(1, productId, productColor, productSize);
                System.out.println("Produkten lades till i din kundvagn!");
            } else {
                System.out.println("Fel användarnamn eller lösenord. Försök igen.");
            }
        } catch (SQLException e) {
            Consumer<SQLException> sqlExceptionHandler = (exception) -> {
                System.err.println("Databasfel: " + exception.getMessage());
            };

            sqlExceptionHandler.accept(e);
        }
    }
}