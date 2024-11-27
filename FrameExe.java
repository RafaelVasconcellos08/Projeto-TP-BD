import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FrameExe extends JFrame {
    private int idBiblioteca;
    protected Connection conexaoDados;

    private JTable tabelaExemplares;
    private JTextField tCodExemplar, tCodLivro, tCondicao;
    private JButton btnPrimeiro, btnAnterior, btnProximo, btnUltimo, btnIncluir, btnExcluir, btnAlterar, btnBuscar;
    private ResultSet rsExemplares;

    public FrameExe(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
        this.conexaoDados = FrameBibPrinci.conexaoDados;  // Obtém a conexão do Frame principal

        setTitle("Manutenção de Exemplares - Biblioteca " + idBiblioteca);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de Campos e Botões
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel lCodExemplar = new JLabel("Código Exemplar:");
        JLabel lCodLivro = new JLabel("Código Livro:");
        JLabel lCondicao = new JLabel("Condição:");

        tCodExemplar = new JTextField();
        tCodLivro = new JTextField();
        tCondicao = new JTextField();

        panel.add(lCodExemplar); panel.add(tCodExemplar);
        panel.add(lCodLivro); panel.add(tCodLivro);
        panel.add(lCondicao); panel.add(tCondicao);

        add(panel, BorderLayout.NORTH);

        // Botões de navegação e ações
        JPanel panelBotoes = new JPanel(new FlowLayout());
        btnPrimeiro = new JButton("Primeiro");
        btnAnterior = new JButton("Anterior");
        btnProximo = new JButton("Próximo");
        btnUltimo = new JButton("Último");
        btnIncluir = new JButton("Incluir");
        btnExcluir = new JButton("Excluir");
        btnAlterar = new JButton("Alterar");
        btnBuscar = new JButton("Buscar");

        panelBotoes.add(btnPrimeiro); panelBotoes.add(btnAnterior);
        panelBotoes.add(btnProximo); panelBotoes.add(btnUltimo);
        panelBotoes.add(btnIncluir); panelBotoes.add(btnExcluir);
        panelBotoes.add(btnAlterar); panelBotoes.add(btnBuscar);

        add(panelBotoes, BorderLayout.SOUTH);

        // Configurar os eventos para os botões
        configurarEventos();

        // Carregar a tabela com os dados dos exemplares
        carregarExemplares();
    }

    // Função para carregar os exemplares no ResultSet e exibir o primeiro registro
    private void carregarExemplares() {
        try {
            String sql = "SELECT * FROM SisBib.Exemplar WHERE idBiblioteca = ?";
            PreparedStatement stmt = conexaoDados.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, idBiblioteca);
            rsExemplares = stmt.executeQuery();

            if (rsExemplares.next()) {
                mostrarRegistroAtual();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Função para mostrar o registro atual nos campos de texto
    private void mostrarRegistroAtual() throws SQLException {
        tCodExemplar.setText(rsExemplares.getString("codExemplar"));
        tCodLivro.setText(rsExemplares.getString("codLivro"));
        tCondicao.setText(rsExemplares.getString("condicao"));
    }

    // Configuração dos eventos dos botões
    private void configurarEventos() {
        btnPrimeiro.addActionListener(e -> {
            try {
                if (rsExemplares.first()) {
                    mostrarRegistroAtual();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnAnterior.addActionListener(e -> {
            try {
                if (rsExemplares.previous()) {
                    mostrarRegistroAtual();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnProximo.addActionListener(e -> {
            try {
                if (rsExemplares.next()) {
                    mostrarRegistroAtual();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnUltimo.addActionListener(e -> {
            try {
                if (rsExemplares.last()) {
                    mostrarRegistroAtual();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Evento para incluir um novo exemplar
        btnIncluir.addActionListener(e -> {
            try {
                String sqlInsert = "INSERT INTO SisBib.Exemplar (codExemplar, codLivro, condicao, idBiblioteca) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conexaoDados.prepareStatement(sqlInsert);
                stmt.setString(1, tCodExemplar.getText());
                stmt.setString(2, tCodLivro.getText());
                stmt.setString(3, tCondicao.getText());
                stmt.setInt(4, idBiblioteca);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Exemplar incluído com sucesso!");
                carregarExemplares();  // Recarregar os dados
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Evento para excluir um exemplar
        btnExcluir.addActionListener(e -> {
            try {
                String sqlDelete = "DELETE FROM SisBib.Exemplar WHERE codExemplar = ?";
                PreparedStatement stmt = conexaoDados.prepareStatement(sqlDelete);
                stmt.setString(1, tCodExemplar.getText());
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Exemplar excluído com sucesso!");
                carregarExemplares();  // Recarregar os dados
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Evento para alterar os dados de um exemplar
        btnAlterar.addActionListener(e -> {
            try {
                String sqlUpdate = "UPDATE SisBib.Exemplar SET codLivro = ?, condicao = ? WHERE codExemplar = ? AND idBiblioteca = ?";
                PreparedStatement stmt = conexaoDados.prepareStatement(sqlUpdate);
                stmt.setString(1, tCodLivro.getText());
                stmt.setString(2, tCondicao.getText());
                stmt.setString(3, tCodExemplar.getText());
                stmt.setInt(4, idBiblioteca);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Exemplar atualizado com sucesso!");
                carregarExemplares();  // Recarregar os dados
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Evento para buscar um exemplar específico
        btnBuscar.addActionListener(e -> {
            try {
                String sql = "SELECT * FROM SisBib.Exemplar WHERE codExemplar = ? AND idBiblioteca = ?";
                PreparedStatement stmt = conexaoDados.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.setString(1, tCodExemplar.getText());
                stmt.setInt(2, idBiblioteca);
                rsExemplares = stmt.executeQuery();

                if (rsExemplares.next()) {
                    mostrarRegistroAtual();
                } else {
                    JOptionPane.showMessageDialog(this, "Exemplar não encontrado.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
