import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FrameDev extends JFrame {
    private static int idBiblioteca, idB;
    protected static Connection conexaoDados;

    private static JTextField txtCodLeitor, txtCodLivro, txtNumExemplar;
    private static JLabel lbCodLeitor,  lbCodLivro,  lbNumExemplar;
    private static JButton btnDevolver;

    public FrameDev(int idBiblioteca) {
        idB = idBiblioteca;
        setTitle("Devolução" + idBiblioteca);
        setSize(600, 400);
        setLayout(new BorderLayout());

        lbCodLeitor = new JLabel("Código Leitor:");
        lbCodLivro = new JLabel("Código Livro:");
        lbNumExemplar = new JLabel("Número Exemplar:");

        txtCodLeitor = new JTextField();
        txtCodLivro = new JTextField();
        txtNumExemplar = new JTextField();

        btnDevolver = new JButton("Devolver");

    }
}
