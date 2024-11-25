import javax.swing.*;

public class FrameExe extends JFrame {
    private int idBiblioteca;

    public FrameExe(int idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
        setTitle("Gerenciar Leitores - Biblioteca " + idBiblioteca);
        setSize(600, 400);
        // Adicionar componentes e lógica de gerenciamento de leitores
    }
}