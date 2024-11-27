import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class FrameDev extends JFrame {
    private int idBiblioteca;
    protected Connection conexaoDados;

    private JTextField tCodLeitor, tCodLivro, tNumExemplar;
    private JButton btnDevolver;

    public FrameDev(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
        this.conexaoDados = FrameBibPrinci.conexaoDados;  // Pega a conexão do Frame principal

        setTitle("Devolução de Livros - Biblioteca " + idBiblioteca);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        // Campos para Devolução
        JLabel lCodLeitor = new JLabel("Código do Leitor:");
        JLabel lCodLivro = new JLabel("Código do Livro:");
        JLabel lNumExemplar = new JLabel("Número do Exemplar:");

        tCodLeitor = new JTextField();
        tCodLivro = new JTextField();
        tNumExemplar = new JTextField();

        btnDevolver = new JButton("Realizar Devolução");

        add(lCodLeitor); add(tCodLeitor);
        add(lCodLivro); add(tCodLivro);
        add(lNumExemplar); add(tNumExemplar);
        add(btnDevolver);

        // Configurar eventos
        configurarEventos();
    }

    // Método para configurar os eventos dos botões
    private void configurarEventos() {
        // Evento para realizar a devolução
        btnDevolver.addActionListener(e -> {
            try {
                realizarDevolucao();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao realizar devolução: " + ex.getMessage());
            }
        });
    }

    // Método para realizar a devolução
    private void realizarDevolucao() throws SQLException {
        int codLeitor = Integer.parseInt(tCodLeitor.getText());
        int codLivro = Integer.parseInt(tCodLivro.getText());
        int numExemplar = Integer.parseInt(tNumExemplar.getText());

        // Verificar se o exemplar está emprestado ao leitor
        String sqlVerificar = "SELECT * FROM SisBib.Emprestimo WHERE codLeitor = ? AND codExemplar = ? AND devolucaoEfetiva IS NULL AND idBiblioteca = ?";
        PreparedStatement stmtVerificar = conexaoDados.prepareStatement(sqlVerificar);
        stmtVerificar.setInt(1, codLeitor);
        stmtVerificar.setInt(2, numExemplar);
        stmtVerificar.setInt(3, idBiblioteca);

        ResultSet rs = stmtVerificar.executeQuery();

        if (rs.next()) {
            // O exemplar está emprestado, então realizar a devolução
            String dataDevolucao = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());  // Data atual

            String sqlDevolver = "UPDATE SisBib.Emprestimo SET devolucaoEfetiva = ? WHERE codLeitor = ? AND codExemplar = ? AND idBiblioteca = ?";
            PreparedStatement stmtDevolver = conexaoDados.prepareStatement(sqlDevolver);
            stmtDevolver.setString(1, dataDevolucao);
            stmtDevolver.setInt(2, codLeitor);
            stmtDevolver.setInt(3, numExemplar);
            stmtDevolver.setInt(4, idBiblioteca);

            stmtDevolver.executeUpdate();

            JOptionPane.showMessageDialog(this, "Devolução realizada com sucesso!");

            // Verificar se houve atraso e suspender o leitor
            verificarAtraso(codLeitor, rs.getDate("dataPrevDevolucao"), dataDevolucao);

        } else {
            JOptionPane.showMessageDialog(this, "Exemplar não encontrado ou não emprestado ao leitor.");
        }
    }

    // Método para verificar atraso na devolução e suspender o leitor, se necessário
    private void verificarAtraso(int codLeitor, Date dataPrevDevolucao, String dataDevolucao) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date dataPrevista = sdf.parse(dataPrevDevolucao.toString());
            java.util.Date dataEfetiva = sdf.parse(dataDevolucao);

            // Se a data de devolução efetiva for posterior à data prevista, suspender o leitor
            if (dataEfetiva.after(dataPrevista)) {
                JOptionPane.showMessageDialog(this, "Devolução com atraso. O leitor será suspenso.");

                // Chamar a stored procedure para suspender o leitor
                String sqlSuspender = "{CALL SuspenderLeitor(?)}";
                CallableStatement stmtSuspender = conexaoDados.prepareCall(sqlSuspender);
                stmtSuspender.setInt(1, codLeitor);

                stmtSuspender.execute();

                JOptionPane.showMessageDialog(this, "Leitor suspenso.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
