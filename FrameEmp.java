import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FrameDev extends JFrame {
    private static int idBiblioteca, idB;
    protected static Connection conexaoDados;

    private static JTextField testeTxt;
    private static JLabel testeL;
    private static JButton testeB;

    public FrameDev(int idBiblioteca) {
        idB = idBiblioteca;
        setTitle("Empréstimos" + idBiblioteca);
        setSize(600, 400);
        setLayout(new BorderLayout());

        testeTxt = new JTextField();
        testeL = new JTextField();
        testeB = new JTextField();
    }
}
