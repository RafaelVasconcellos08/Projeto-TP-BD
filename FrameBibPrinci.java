import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.*;

public class FrameBibPrinci extends JFrame {
    private JToolBar tbBotoes;
    private JButton btnConectar;

    private static JTextField tSer, tUsu, tSen, tBD;

    private static JLabel lbSer, lbUsu, lbSen, lbBD, lbBib;

    private static JPanel mainPanel, formPanel;

    private static JComboBox<String> cbBib;

    private static ResultSet dadosDoSelect;

    private static JMenuBar mBar;
    private static JMenu mLiv, mExe, mEmp, mDev;
    private static JMenuItem liv, exe, emp, dev;

    protected static Connection conexaoDados = null;

    protected static int idBiblioteca;

    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameBibPrinci form = new FrameBibPrinci();
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

    private static void preencherDados() {
        String sql = "SELECT idBiblioteca, nome FROM SisBib.Biblioteca";
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            dadosDoSelect = comandoSQL.executeQuery(sql);
            while (dadosDoSelect.next()) {
                exibirRegistro();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Bibliotecas não encontradas!");
        }
    }



    public FrameBibPrinci() {
        setTitle("Sistema de Bibliotecas");
        setSize(1400, 600);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        tbBotoes = new JToolBar();

        btnConectar = new JButton("Conectar", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/Oeil2.png"))));
        btnConectar.setPreferredSize(new Dimension(150, 45));
        btnConectar.setFocusPainted(false);

        tbBotoes.setLayout(new FlowLayout());

        tbBotoes.add(btnConectar);
        tbBotoes.addSeparator();

        tbBotoes.setRollover(true);

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
        cbBib.setEnabled(false);

        mBar = new JMenuBar();
        mLiv = new JMenu("Livros");
        mExe = new JMenu("Exemplares");
        mEmp = new JMenu("Empréstimos");
        mDev = new JMenu("Devoluções");

        liv = new JMenuItem("livros");
        emp = new JMenuItem("emprestimo");
        dev = new JMenuItem("devolucoes");

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
        mLiv.add(liv);
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

        liv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idBiblioteca == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione uma biblioteca antes de continuar!");
                    return;
                }
                try {
                    FrameLiv livros = new FrameLiv(idBiblioteca);
                    livros.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        exe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idBiblioteca == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione uma biblioteca antes de continuar!");
                    return;
                }
                try {
                    FrameExe exem = new FrameExe(idBiblioteca);
                    exem.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        dev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idBiblioteca == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione uma biblioteca antes de continuar!");
                    return;
                }
                try {
                    FrameDev devolucoes = new FrameDev(idBiblioteca);
                    devolucoes.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        btnConectar.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String SERVER = tSer.getText(); // regulus.cotuca.unicamp.br
                String BD = tBD.getText();
                String USER = tUsu.getText();
                String PASSWORD = tSen.getText();

                try {
                    conexaoDados = ConexaoBD.getConnection(SERVER, BD, USER, PASSWORD);
                    JOptionPane.showMessageDialog(null, "Conectado!!");
                    JOptionPane.showMessageDialog(null, idBiblioteca);
                    preencherDados();

                    cbBib.setEnabled(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}