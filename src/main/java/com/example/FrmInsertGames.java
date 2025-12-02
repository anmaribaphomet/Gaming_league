package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FrmInsertGames extends JDialog {
    private JTextField txtNomGame;
    private JTextField txtDescripcion;
    private JComboBox<String> cbPlataforma;
    private JComboBox<String> cbCategoria;


    private Database db;


    public FrmInsertGames(Frame parent, Database db) {
        super(parent, "Insertar Juegos", true);
        this.db = db;

        JPanel panel = EstiloForm.estiloPanel();

        txtNomGame = new JTextField();
        txtDescripcion = new JTextField();
        cbPlataforma = new JComboBox<>();
        cbCategoria = new JComboBox<>();


        panel.add(new JLabel("Nombre del juego:"));
        panel.add(txtNomGame);
        panel.add(new JLabel("Descripcion:"));
        panel.add(txtDescripcion);
        panel.add(new JLabel("Plataforma:"));
        panel.add(cbPlataforma);
        panel.add(new JLabel("Categoria:"));
        panel.add(cbCategoria);

        JButton btnInsertar = EstiloForm.crearBoton("Insertar");
        btnInsertar.addActionListener(evt -> insertar());
        panel.add(new JLabel()); // celda vacía para centrar
        panel.add(EstiloForm.centrar(btnInsertar));

        cargarCombos();
        add(panel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void cargarCombos() {
        try {

            // Cargar plataformas
            ResultSet rs = db.query("SELECT DISTINCT platform FROM public.games ORDER BY platform");
            while (rs.next()) {
                cbPlataforma.addItem(rs.getString("platform"));
            }

            // Cargar categorías
            rs = db.query("SELECT DISTINCT category FROM public.games ORDER BY category");
            while (rs.next()) {
                cbCategoria.addItem(rs.getString("category"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertar() {

        String nombreJuego = txtNomGame.getText();
        String descJuego = txtDescripcion.getText();
        String plataforma = cbPlataforma.getSelectedItem().toString();
        String categoria = cbCategoria.getSelectedItem().toString();

        String sql = """
        INSERT INTO public.games 
            (game_name, game_description, platform, category)
        VALUES ( ?, ?, ?, ?)
    """;

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false); // INICIA TRANSACCIÓN

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nombreJuego);
            ps.setString(2, descJuego);
            ps.setString(3, plataforma);
            ps.setString(4, categoria);
            ps.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(this, "Insertado correctamente");
            dispose();

        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception ex3) {
                ex3.printStackTrace();
            }
        }
    }
}
