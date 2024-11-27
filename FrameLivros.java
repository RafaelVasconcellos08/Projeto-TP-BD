import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Objects;

public class FrameLivros extends JFrame {
    private int idBiblioteca;

    private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
    // Componentes da interface
    private JLabel lbTitulo, lbAutor, lbAno, lbEditora;
    private JTextField txtCodLivro, txtTitulo, txtIdAutor, txtIdArea;
    private JButton btnIncluir, btnExcluir, btnAlterar, btnBuscar, btnInicio, btnAnterior, btnProximo, btnFinal;


    private static JTable tabLivros;	// controle que exibe dados em formato tabular (linhas e colunas)

    private ResultSet dadosLivros; // Resultado da consulta de livros
    protected Connection conexaoDados;

    public FrameLivros(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
        setTitle("Gerenciar Livros - Biblioteca " + idBiblioteca);
        setSize(600, 400);
        setLayout(new BorderLayout());
        JPanel pnlGrade = new JPanel();     		 	// colocaremos JTable com os registros da tabela
        tbBotoes = new JToolBar();  // orientação padrão é HORIZONTAL
        JPanel pnlCampos = new JPanel();    		 // colocaremos os campos de digitação de dados
        // Inicializa os componentes da interface
        Object [][] dadosDepto = { {"", "", 0, 0}, { "", "", 1, 1} };
        String[] titulosColunas = {"codLivro","titulo","idAutor","idArea"};
        tabLivros = new JTable(dadosDepto,  titulosColunas);
        JScrollPane barraRolagem = new JScrollPane(tabLivros);
        pnlGrade.add(barraRolagem);
        pnlCampos.setLayout(new GridLayout(4, 2));	//  4 linhas e 2 colunas
        txtCodLivro = new JTextField();
        txtTitulo = new JTextField();
        txtIdAutor  = new JTextField();
        txtIdArea  = new JTextField();

        pnlCampos.add(new JLabel("CodLivro:"));			// 1, 1
        pnlCampos.add(txtCodLivro);					// 1, 2
        pnlCampos.add(new JLabel("Nome:"));				// 2, 1
        pnlCampos.add(txtTitulo);					// 2, 2
        pnlCampos.add(new JLabel("Autor:"));		// 3, 1
        pnlCampos.add(txtIdAutor);					// 3, 2
        pnlCampos.add(new JLabel("Area:"));		// 4, 1
        pnlCampos.add(txtIdArea);					// 4, 2


        // Botões de ação
        btnAlterar = new JButton("Alterar");

        btnInicio = new JButton("Inicio", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/first.png"))));
        btnInicio.setPreferredSize(new Dimension(65, 45));
        btnInicio.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnInicio.setHorizontalTextPosition(SwingConstants.CENTER);
        btnInicio.setFocusPainted(false);       //remove uma borda que fica dentro do último botão pressionado

        btnFinal = new JButton("Final", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources//last.png"))));
        btnFinal.setPreferredSize(new Dimension(65, 45));
        btnFinal.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnFinal.setHorizontalTextPosition(SwingConstants.CENTER);
        btnFinal.setFocusPainted(false);

        btnAnterior = new JButton("Voltar", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/prior.png"))));
        btnAnterior.setPreferredSize(new Dimension(65, 45));
        btnAnterior.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnAnterior.setHorizontalTextPosition(SwingConstants.CENTER);
        btnAnterior.setFocusPainted(false);

        btnProximo = new JButton("Avancar", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/next.png"))));
        btnProximo.setPreferredSize(new Dimension(65, 45));
        btnProximo.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnProximo.setHorizontalTextPosition(SwingConstants.CENTER);
        btnProximo.setFocusPainted(false);

        btnBuscar = new JButton("Buscar", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/find.png"))));
        btnBuscar.setPreferredSize(new Dimension(65, 45));
        btnBuscar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnBuscar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnBuscar.setFocusPainted(false);

        btnIncluir = new JButton("Incluir", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/add.png"))));
        btnIncluir.setPreferredSize(new Dimension(65, 45));
        btnIncluir.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnIncluir.setHorizontalTextPosition(SwingConstants.CENTER);
        btnIncluir.setFocusPainted(false);

        btnExcluir = new JButton("Excluir", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/minus.png"))));
        btnExcluir.setPreferredSize(new Dimension(65, 45));
        btnExcluir.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnExcluir.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcluir.setFocusPainted(false);

        add(tbBotoes, BorderLayout.NORTH); // Adiciona a barra de ferramentas no topo

        tbBotoes.setLayout(new FlowLayout());

        tbBotoes.add(btnInicio);
        tbBotoes.add(btnAnterior);
        tbBotoes.add(btnProximo);
        tbBotoes.add(btnFinal);
        tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

        tbBotoes.add(btnBuscar);
        tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

        tbBotoes.add(btnAlterar);
        tbBotoes.add(btnIncluir);
        tbBotoes.add(btnExcluir);
        tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

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

        btnInicio.addActionListener(new ActionListener() {
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

        btnFinal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ultimoLivro();
            }
        });
    }

    // Carregar os dados de livros do banco de dados
    private void carregarDados() throws SQLException {
        String sql = "SELECT Livro.* " +
                "FROM SisBib.Livro " +
                "JOIN SisBib.Exemplar ON Livro.codLivro = Exemplar.codLivro " +
                "WHERE Exemplar.idBiblioteca = " + idBiblioteca;

        Statement comandoSQL = conexaoDados.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE); // Importante para suportar operações de atualização
        dadosLivros = comandoSQL.executeQuery(sql);

        if (dadosLivros.first()) {
            exibirDados();
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum livro encontrado para a biblioteca.");
        }
    }


    // Exibe os dados do livro atual no formulário
    private void exibirDados() {
        try {
            txtCodLivro.setText(dadosLivros.getString("codLivro"));
            txtTitulo.setText(dadosLivros.getString("titulo"));
            txtIdAutor.setText(dadosLivros.getString("idAutor"));
            txtIdArea.setText(dadosLivros.getString("idArea"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exibir dados: " + e.getMessage());
        }
    }

    // Incluir novo livro no banco de dados
    private void incluirLivro() {
        try {
            dadosLivros.moveToInsertRow(); // Posiciona no registro fictício para inserir

            // Atualize os nomes das colunas conforme o banco de dados
            dadosLivros.updateString("codLivro", txtCodLivro.getText());
            dadosLivros.updateString("titulo", txtTitulo.getText());
            dadosLivros.updateString("idAutor", txtIdAutor.getText());  // Verifique se é 'autor'/
            dadosLivros.updateString("idArea", txtIdArea.getText()); // Verifique se é 'editora'

            dadosLivros.insertRow(); // Insere o registro
            JOptionPane.showMessageDialog(null, "Livro incluído com sucesso!");

            carregarDados(); // Recarrega os dados para refletir a inclusão
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao incluir livro: " + e.getMessage());
        }
    }


    // Excluir o livro atual do banco de dados
    private void excluirLivro() {
        try {
            if (JOptionPane.showConfirmDialog(null, "Deseja realmente excluir?") == JOptionPane.OK_OPTION) {
                dadosLivros.deleteRow();
                JOptionPane.showMessageDialog(null, "Livro excluído com sucesso!");
                carregarDados(); // Atualiza os dados após a exclusão
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir livro: " + e.getMessage());
        }
    }

    // Alterar o livro atual no banco de dados
    private void alterarLivro() {
        try {
            dadosLivros.updateString("codLivro", txtCodLivro.getText());
            dadosLivros.updateString("titulo", txtTitulo.getText());
            dadosLivros.updateString("idAutor", txtIdAutor.getText());
            dadosLivros.updateString("idArea", txtIdArea.getText());
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
            String sql = "SELECT Livro.* " +
                    "FROM SisBib.Livro " +
                    "JOIN SisBib.Exemplar ON Livro.codLivro = Exemplar.codLivro " +
                    "WHERE Exemplar.idBiblioteca = " + idBiblioteca +
                    " AND Livro.titulo LIKE '%" + titulo + "%'";

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
            } else{
                JOptionPane.showMessageDialog(null, "Não achou Primeiro registro!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Navegar para o livro anterior
    private void anteriorLivro() {
        try {
            if (dadosLivros.previous()) {
                exibirDados();
            } else {
                JOptionPane.showMessageDialog(null, "Não há registros anteriores!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Navegar para o próximo livro
    private void proximoLivro() {
        try {
            if (dadosLivros.next()) {
                exibirDados();
            } else {
                JOptionPane.showMessageDialog(null, "Não achou próximo registro!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Navegar para o último livro
    private void ultimoLivro() {
        try {
            if (dadosLivros.last()) {
                exibirDados();
            } else{
                JOptionPane.showMessageDialog(null, "Não achou Último registro!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}