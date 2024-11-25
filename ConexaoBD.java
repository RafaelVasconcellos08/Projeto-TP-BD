import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    /*
    private static String data;
    private static final String URL =
        "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;databaseName="+
        ";integratedSecurity=false;encrypt=false;trustServerCertificate=true";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }*/

    public static Connection getConnection(String SERVER, String BD, String USER, String PASSWORD) throws SQLException {
        String URL = "jdbc:sqlserver://"+ SERVER + ":1433;databaseName=" + BD +
                        ";integratedSecurity=false;encrypt=false;trustServerCertificate=true";
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}