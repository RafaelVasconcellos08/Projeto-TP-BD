import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.Objects;

public class FrameBibPrinci extends JFrame {
    private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
    private JButton btnConectar;

    private static JTextField tSer, tUsu, tSen, tBD;

    private static JTable tabDepto;    // controle que exibe dados em formato tabular (linhas e colunas)

    private static JLabel lbSer, lbUsu, lbSen, lbBD, lbBib;

    private static JPanel mainPanel, formPanel;

    private static JComboBox<String> cbBib;

    private static ResultSet dadosDoSelect;

    private static JMenuBar mBar;
    private static JMenu mLiv, mExe, mEmp, mDev;
    private static JMenuItem teste;

    protected static Connection conexaoDados = null;

    protected static int idBiblioteca;
    private static String nome;

    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameBibPrinci form = new FrameBibPrinci();
                // Adaptador para o fechamento da janela, matando o processo
                form.addWindowListener(
                        new WindowAdapter() {
                            public void windowClosing(WindowEvent e) {
                                try {
                                    conexaoDados.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.exit(0);
                            }
                        }
                );
                form.pack();
                form.setVisible(true);
            }
        });
    }

    static private void exibirRegistro() throws SQLException
    {
        if (!dadosDoSelect.rowDeleted())
        {
            cbBib.addItem(dadosDoSelect.getString("nome"));
            idBiblioteca = dadosDoSelect.getInt("idBiblioteca");
        }
    }

    // Método para carregar bibliotecas no JComboBox
    private static void preencherDados() {
        String sql = "SELECT idBiblioteca, nome FROM SisBib.Biblioteca";
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,	// permite navegação
                    ResultSet.CONCUR_UPDATABLE        // ResultSet é atualizável
            );
            dadosDoSelect = comandoSQL.executeQuery(sql);
            while (dadosDoSelect.next()) {
                exibirRegistro();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Bibliotecas não encontradas!");
        }
    }


    // construtor do formulário
    public FrameBibPrinci() {
        setTitle("Sistema de Bibliotecas");
        setSize(1400, 600);
        setMinimumSize(new Dimension(800, 600));
        // apenas chame o evento windowClosing
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // BOTÕES

        // Adiciorenamos os botões ao JToolBar que os conterá
        tbBotoes = new JToolBar();  // orientação padrão é HORIZONTAL

        btnConectar = new JButton("Conectar", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/Oeil2.png"))));
        btnConectar.setPreferredSize(new Dimension(150, 45));
        btnConectar.setFocusPainted(false);       //remove uma borda que fica dentro do último botão pressionado

        // Os botões serão dispostos um ao lado do outro, fluindo da esquerda para a direita, de cima para baixo
        // para isso usamos um gerenciador de layout da classe FlowLayout:
        // estabelecemos o layout do tbBotoes como flowLayout
        tbBotoes.setLayout(new FlowLayout());

        tbBotoes.add(btnConectar);
        tbBotoes.addSeparator();    // coloca um separador entre esses botões e os seguintes

        // os botões apenas serão enfatizados visualmente quando o mouse passar sobre eles
        tbBotoes.setRollover(true);

        // LABEL

        lbSer = new JLabel("Servidor: ");
        tSer = new JTextField();

        lbBD = new JLabel("Banco de Dados: ");
        tBD = new JTextField();

        lbUsu = new JLabel("Usuário: ");
        tUsu = new JTextField();

        lbSen = new JLabel("Senha: ");
        tSen = new JTextField();

        lbBib = new JLabel("Biblioteca: ");
        cbBib = new JComboBox<>();
        cbBib.setEnabled(false);  // Inicialmente desabilitado

        mBar = new JMenuBar();
        mLiv = new JMenu("Livros");
        mExe = new JMenu("Exemplares");
        mEmp = new JMenu("Empréstimos");
        mDev = new JMenu("Devoluções");

        teste = new JMenuItem("teste");

        // ADICIONAR NA TELA

        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 4, 5, 5));
        formPanel.add(lbSer);
        formPanel.add(tSer);
        formPanel.add(lbBD);
        formPanel.add(tBD);
        formPanel.add(lbUsu);
        formPanel.add(tUsu);
        formPanel.add(lbSen);
        formPanel.add(tSen);
        formPanel.add(lbBib);
        formPanel.add(cbBib);
        formPanel.add(tbBotoes);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.NORTH);

        mBar.add(mLiv);
        mBar.add(mExe);
        mBar.add(mEmp);
        mBar.add(mDev);
        mLiv.add(teste);
        setJMenuBar(mBar);

        add(mainPanel);

        cbBib.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedIndex = cbBib.getSelectedIndex();
                    if (selectedIndex >= 0 && dadosDoSelect.absolute(selectedIndex + 1)) {
                        idBiblioteca = dadosDoSelect.getInt("idBiblioteca");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao selecionar biblioteca!");
                }
            }
        });

        teste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idBiblioteca == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione uma biblioteca antes de continuar!");
                    return;
                }
                try {
                    FrameLivros livros = new FrameLivros(idBiblioteca);
                    livros.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao abrir o FrameLivros: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });


        // ActionListener para o botão de conexão
        btnConectar.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pega os valores dos campos de texto
                String SERVER = tSer.getText(); // regulus.cotuca.unicamp.br
                String BD = tBD.getText();
                String USER = tUsu.getText();
                String PASSWORD = tSen.getText();

                try {
                    conexaoDados = ConexaoBD.getConnection(SERVER, BD, USER, PASSWORD);
                    JOptionPane.showMessageDialog(null, "Conectado!!");
                    JOptionPane.showMessageDialog(null, idBiblioteca);
                    preencherDados();

                    // Habilitar a seleção de biblioteca e o menu
                    cbBib.setEnabled(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}