import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameDepto extends JFrame{
    private JToolBar tbBotoes; // armazenará os botões abaixo; será colocado no topo do formulári
    private JButton btnConectar, btnCancelar;

    private static JTextField txtServidor;

    private static JTable tabDepto;	// controle que exibe dados em formato tabular (linhas e colunas)

    // será usada para manter aberta uma conexão ao BD para
    // podermos navegar entre registros e, futuramente, realizar
    // operações CRUD
    static private Connection conexaoDados = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            FrameDepto form = new FrameDepto();
            // Adaptador para o fechamento da janela, matando o processo
            form.addWindowListener(
                new WindowAdapter()
                {
                  public void windowClosing (WindowEvent e)
                  {
                    /* 
                    try {
                      conexaoDados.close();
                    } catch (SQLException ex) {
                      throw new RuntimeException(ex);
                    }
                    */
                    System.exit(0);
                  }
                }
            );
    
            form.pack();
            form.setVisible(true);
          }
        });
      }

  // construtor do formulário 
  public FrameDepto() {
    setTitle("Sistema de Bibliotecas");
    setSize(1000, 300);
    // apenas chame o evento windowClosing
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    // Inicializa a barra de ferramentas
    tbBotoes = new JToolBar();
    btnConectar = new JButton("Conectar");
    btnCancelar = new JButton("Cancelar");

    
    // Adiciona a barra de ferramentas no topo
    add(tbBotoes, BorderLayout.SOUTH);
    
    // Adiciona os botões à barra de ferramentas
    tbBotoes.add(btnConectar);
    tbBotoes.add(btnCancelar);

    // Painel central com campos de texto
    JPanel painelCentral = new JPanel(new GridLayout(6, 2, 5, 5));
    painelCentral.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    painelCentral.add(new JLabel("Servidor:"));
    txtServidor = new JTextField();
    painelCentral.add(txtServidor);

    // Adiciona o painel central ao centro do formulário
    add(painelCentral, BorderLayout.CENTER);
}
}