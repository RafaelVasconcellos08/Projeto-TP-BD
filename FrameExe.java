import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class FrameExe extends JFrame {
    private static int idBiblioteca, idB;

    private static JToolBar tbBotoes;

    private static JTextField txtCodLivro, txtNumExem;

    private static JButton btnIncluir, btnExcluir, btnAlterar, btnBuscar, btnInicio, btnAnterior, btnProximo, btnFinal;

    private static ResultSet dadosDoSelect;
    protected static Connection conexaoDados;

    private static void preencherDados() {
        String sql = "";
        try {
            Statement comandoSQL = conexaoDados.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            try {
                dadosDoSelect = comandoSQL.executeQuery(sql);
                if (dadosDoSelect.next()) {
                    exibirRegistro();
                } else {
                    JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static private void exibirRegistro() throws SQLException
    {
        if (!dadosDoSelect.rowDeleted())
        {
            txtCodLivro.setText(dadosDoSelect.getString("codLivro"));
            txtNumExem.setText(dadosDoSelect.getString("numeroExemplar"));
        }
    }

    public FrameExe(int idBiblioteca) {
        setTitle("Exemplar" + idB);
        setSize(600, 400);
        setLayout(new BorderLayout());
        tbBotoes = new JToolBar();
        JPanel pnlCampos = new JPanel();

        pnlCampos.setLayout(new GridLayout(4, 2));
        txtCodLivro = new JTextField();
        txtNumExem = new JTextField();

        pnlCampos.add(new JLabel("CodLivro:"));
        pnlCampos.add(txtCodLivro);
        pnlCampos.add(new JLabel("numeroExemplar:"));
        pnlCampos.add(txtNumExem);

        btnAlterar = new JButton("Alterar");

        btnInicio = new JButton("Inicio", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/first.png"))));
        btnInicio.setPreferredSize(new Dimension(65, 45));
        btnInicio.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnInicio.setHorizontalTextPosition(SwingConstants.CENTER);
        btnInicio.setFocusPainted(false);

        btnFinal = new JButton("Final", new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/last.png"))));
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

        add(tbBotoes, BorderLayout.NORTH);

        tbBotoes.setLayout(new FlowLayout());

        tbBotoes.add(btnInicio);
        tbBotoes.add(btnAnterior);
        tbBotoes.add(btnProximo);
        tbBotoes.add(btnFinal);
        tbBotoes.addSeparator();

        tbBotoes.add(btnBuscar);
        tbBotoes.addSeparator();

        tbBotoes.add(btnAlterar);
        tbBotoes.add(btnIncluir);
        tbBotoes.add(btnExcluir);
        tbBotoes.addSeparator();

        btnIncluir.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try {
                            dadosDoSelect.moveToInsertRow();
                            dadosDoSelect.updateString("codLivro", txtCodLivro.getText());
                            dadosDoSelect.updateString("titulo", txtNumExem.getText());
                            dadosDoSelect.insertRow();
                            JOptionPane.showMessageDialog(null, "Livro incluído com sucesso!");
                            preencherDados();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        btnExcluir.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try
                        {
                            if (JOptionPane.showConfirmDialog(
                                    null, "Deseja realmente excluir?") ==
                                    JOptionPane.OK_OPTION)
                            {
                                dadosDoSelect.deleteRow();
                                JOptionPane.showMessageDialog(null, "Exclusão bem sucedida!");
                                exibirRegistro();
                            }
                        }
                        catch (SQLException ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                    }
                });

        btnAlterar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            dadosDoSelect.updateString("codLivro", txtCodLivro.getText());
                            dadosDoSelect.updateString("titulo", txtNumExem.getText());
                            dadosDoSelect.updateRow();
                            JOptionPane.showMessageDialog(null, "Livro alterado!");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        btnBuscar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try
                        {
                            int posicaoAnterior = dadosDoSelect.getRow();
                            int chaveProcurada = Integer.parseInt(txtCodLivro.getText());
                            dadosDoSelect.beforeFirst();
                            boolean achou = false;
                            while (! achou && dadosDoSelect.next())
                            {
                                if (dadosDoSelect.getInt("numDepto") == chaveProcurada)
                                    achou = true;
                            }
                            if (!achou)
                            {
                                JOptionPane.showMessageDialog(null, "Registro não encontrado!");
                                dadosDoSelect.absolute(posicaoAnterior);
                            }
                            exibirRegistro();
                        }
                        catch (SQLException exception)
                        {
                            throw new RuntimeException(exception);
                        }
                    }
                });

        btnInicio.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.first()) {
                                exibirRegistro();
                            } else{
                                JOptionPane.showMessageDialog(null, "Não achou Primeiro registro!");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        btnAnterior.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.previous()) {
                                exibirRegistro();
                            } else {
                                JOptionPane.showMessageDialog(null, "Não há registros anteriores!");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        btnProximo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.next()) {
                                exibirRegistro();
                            } else {
                                JOptionPane.showMessageDialog(null, "Não achou próximo registro!");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        btnFinal.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (dadosDoSelect.last()) {
                                exibirRegistro();
                            } else{
                                JOptionPane.showMessageDialog(null, "Não achou Último registro!");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

    }


}
