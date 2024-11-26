import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class FrameEmprestimos extends JFrame {
    private int idBiblioteca;
    private Connection conexaoDados;

    private JTextField tCodLeitor, tCodExemplar, tDataEmprestimo, tDataPrevDevolucao;
    private JButton btnEmprestar, btnVerAtrasados;
    private JTable tabelaLivrosAtrasados;
    private JTabbedPane tabPane;

    private ResultSet rsLivrosAtrasados;

    public FrameEmprestimos(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
        this.conexaoDados = FrameBibPrinci.getConexaoDados();  // Pegar a conexão do Frame principal

        setTitle("Empréstimos de Livros - Biblioteca " + idBiblioteca);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de Campos de Empréstimo
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel lCodLeitor = new JLabel("Código do Leitor:");
        JLabel lCodExemplar = new JLabel("Código do Exemplar:");
        JLabel lDataEmprestimo = new JLabel("Data de Empréstimo:");
        JLabel lDataPrevDevolucao = new JLabel("Data Prevista de Devolução:");

        tCodLeitor = new JTextField();
        tCodExemplar = new JTextField();
        tDataEmprestimo = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));  // Data atual
        tDataPrevDevolucao = new JTextField();

        panel.add(lCodLeitor); panel.add(tCodLeitor);
        panel.add(lCodExemplar); panel.add(tCodExemplar);
        panel.add(lDataEmprestimo); panel.add(tDataEmprestimo);
        panel.add(lDataPrevDevolucao); panel.add(tDataPrevDevolucao);

        btnEmprestar = new JButton("Realizar Empréstimo");
        btnVerAtrasados = new JButton("Ver Livros em Atraso");

        panel.add(btnEmprestar); panel.add(btnVerAtrasados);

        add(panel, BorderLayout.NORTH);

        // Painel com Abas (TabControl)
        tabPane = new JTabbedPane();
        tabelaLivrosAtrasados = new JTable();  // Tabela para mostrar livros em atraso

        JScrollPane scrollPane = new JScrollPane(tabelaLivrosAtrasados);
        tabPane.addTab("Livros em Atraso", scrollPane);
        add(tabPane, BorderLayout.CENTER);

        // Configurar eventos dos botões
        configurarEventos();
    }

    // Método para configurar os eventos dos botões
    private void configurarEventos() {
        // Evento para realizar o empréstimo
        btnEmprestar.addActionListener(e -> {
            try {
                // Inserir o empréstimo no banco de dados
                String sqlInsert = "INSERT INTO SisBib.Emprestimo (codLeitor, codExemplar, dataEmprestimo, dataPrevDevolucao, devolucaoEfetiva, idBiblioteca) VALUES (?, ?, ?, ?, null, ?)";
                PreparedStatement stmt = conexaoDados.prepareStatement(sqlInsert);

                stmt.setInt(1, Integer.parseInt(tCodLeitor.getText()));
                stmt.setInt(2, Integer.parseInt(tCodExemplar.getText()));
                stmt.setString(3, tDataEmprestimo.getText());
                stmt.setString(4, tDataPrevDevolucao.getText());
                stmt.setInt(5, idBiblioteca);

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");

            } catch (SQLException ex) {
                // Captura a exceção da trigger do banco de dados e mostra ao usuário
                JOptionPane.showMessageDialog(this, "Erro ao realizar empréstimo: " + ex.getMessage());
            }
        });

        // Evento para ver livros em atraso
        btnVerAtrasados.addActionListener(e -> {
            carregarLivrosAtrasados();
        });
    }

    // Carregar livros em atraso no ResultSet e mostrar na tabela
    private void carregarLivrosAtrasados() {
        try {
            String sql = "SELECT * FROM SisBib.LivrosAtrasadosView WHERE idBiblioteca = ?";
            PreparedStatement stmt = conexaoDados.prepareStatement(sql);
            stmt.setInt(1, idBiblioteca);

            rsLivrosAtrasados = stmt.executeQuery();

            // Aqui você configuraria a JTable para mostrar os resultados
            tabelaLivrosAtrasados.setModel(buildTableModel(rsLivrosAtrasados));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helper para converter ResultSet em TableModel para a JTable
    public static TableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Nomes das colunas
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Dados das colunas
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
