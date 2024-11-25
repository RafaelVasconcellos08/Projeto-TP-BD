import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FrameLivros extends JFrame  {
    private int idBiblioteca;

    public FrameLivros() {
        setTitle("Gerenciar Livros - Biblioteca " + idBiblioteca);
        setSize(600, 400);
        // Adicionar componentes e lógica de gerenciamento de livros
    }
}
