import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FrameLivros extends JFrame {
    private int idBiblioteca;

    // Componentes da interface
    private JLabel lbTitulo, lbAutor, lbAno, lbEditora;
    private JTextField tTitulo, tAutor, tAno, tEditora;
    private JButton btnIncluir, btnExcluir, btnAlterar, btnBuscar, btnPrimeiro, btnAnterior, btnProximo, btnUltimo;

    private ResultSet dadosLivros; // Resultado da consulta de livros
    private Connection conexaoDados;

    public FrameLivros(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
        setTitle("Gerenciar Livros - Biblioteca " + idBiblioteca);
        setSize(600, 400);
        setLayout(new GridLayout(7, 2, 10, 10));

        // Inicializa os componentes da interface
        lbTitulo = new JLabel("Título:");
        tTitulo = new JTextField();
        lbAutor = new JLabel("Autor:");
        tAutor = new JTextField();
        lbAno = new JLabel("Ano:");
        tAno = new JTextField();
        lbEditora = new JLabel("Editora:");
        tEditora = new JTextField();

        // Botões de ação
        btnIncluir = new JButton("Incluir");
        btnExcluir = new JButton("Excluir");
        btnAlterar = new JButton("Alterar");
        btnBuscar = new JButton("Buscar");
        btnPrimeiro = new JButton("Primeiro");
        btnAnterior = new JButton("Anterior");
        btnProximo = new JButton("Próximo");
        btnUltimo = new JButton("Último");

        // Adiciona os componentes ao layout
        add(lbTitulo);
        add(tTitulo);
        add(lbAutor);
        add(tAutor);
        add(lbAno);
        add(tAno);
        add(lbEditora);
        add(tEditora);
        add(btnIncluir);
        add(btnExcluir);
        add(btnAlterar);
        add(btnBuscar);
        add(btnPrimeiro);
        add(btnAnterior);
        add(btnProximo);
        add(btnUltimo);

        // Conexão ao banco de dados
        try {
            // Usa a conexão já aberta no Frame principal
            conexaoDados = FrameBibPrinci.conexaoDados;
            carregarDados();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }

        // Adiciona ActionListeners para os botões
        btnIncluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incluirLivro();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirLivro();
            }
        });

        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarLivro();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarLivro();
            }
        });

        btnPrimeiro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                primeiroLivro();
            }
        });

        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anteriorLivro();
            }
        });

        btnProximo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proximoLivro();
            }
        });

        btnUltimo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ultimoLivro();
            }
        });
    }

    // Carregar os dados de livros do banco de dados
    private void carregarDados() throws SQLException {
        String sql = "SELECT * FROM SisBib.Livro WHERE idBiblioteca = " + idBiblioteca;
        Statement comandoSQL = conexaoDados.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        dadosLivros = comandoSQL.executeQuery(sql);
        if (dadosLivros.first()) {
            exibirDados();
        }
    }

    // Exibe os dados do livro atual no formulário
    private void exibirDados() {
        try {
            tTitulo.setText(dadosLivros.getString("titulo"));
            tAutor.setText(dadosLivros.getString("autor"));
            tAno.setText(dadosLivros.getString("ano"));
            tEditora.setText(dadosLivros.getString("editora"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exibir dados: " + e.getMessage());
        }
    }

    // Incluir novo livro no banco de dados
    private void incluirLivro() {
        try {
            dadosLivros.moveToInsertRow();
            dadosLivros.updateString("titulo", tTitulo.getText());
            dadosLivros.updateString("autor", tAutor.getText());
            dadosLivros.updateString("ano", tAno.getText());
            dadosLivros.updateString("editora", tEditora.getText());
            dadosLivros.insertRow();
            JOptionPane.showMessageDialog(null, "Livro incluído com sucesso!");
            carregarDados();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao incluir livro: " + e.getMessage());
        }
    }

    // Excluir o livro atual do banco de dados
    private void excluirLivro() {
        try {
            dadosLivros.deleteRow();
            JOptionPane.showMessageDialog(null, "Livro excluído com sucesso!");
            carregarDados();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir livro: " + e.getMessage());
        }
    }

    // Alterar o livro atual no banco de dados
    private void alterarLivro() {
        try {
            dadosLivros.updateString("titulo", tTitulo.getText());
            dadosLivros.updateString("autor", tAutor.getText());
            dadosLivros.updateString("ano", tAno.getText());
            dadosLivros.updateString("editora", tEditora.getText());
            dadosLivros.updateRow();
            JOptionPane.showMessageDialog(null, "Livro alterado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar livro: " + e.getMessage());
        }
    }

    // Buscar livro por título
    private void buscarLivro() {
        String titulo = JOptionPane.showInputDialog("Digite o título do livro:");
        try {
            String sql = "SELECT * FROM SisBib.Livro WHERE titulo LIKE '%" + titulo + "%' AND idBiblioteca = "
                    + idBiblioteca;
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            dadosLivros = comandoSQL.executeQuery(sql);
            if (dadosLivros.first()) {
                exibirDados();
            } else {
                JOptionPane.showMessageDialog(null, "Livro não encontrado!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar livro: " + e.getMessage());
        }
    }

    // Navegar para o primeiro livro
    private void primeiroLivro() {
        try {
            if (dadosLivros.first()) {
                exibirDados();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao navegar: " + e.getMessage());
        }
    }

    // Navegar para o livro anterior
    private void anteriorLivro() {
        try {
            if (dadosLivros.previous()) {
                exibirDados();
            } else {
                dadosLivros.first(); // Caso esteja no primeiro registro, volta ao início
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao navegar: " + e.getMessage());
        }
    }

    // Navegar para o próximo livro
    private void proximoLivro() {
        try {
            if (dadosLivros.next()) {
                exibirDados();
            } else {
                dadosLivros.last(); // Caso esteja no último registro, vai ao final
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao navegar: " + e.getMessage());
        }
    }

    // Navegar para o último livro
    private void ultimoLivro() {
        try {
            if (dadosLivros.last()) {
                exibirDados();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao navegar: " + e.getMessage());
        }
    }
}
