import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL =
        "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;databaseName=BD23136"+
        ";integratedSecurity=false;encrypt=false;trustServerCertificate=true";

    private static final String USER = "BD23136";
    private static final String PASSWORD = "BD23136";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}