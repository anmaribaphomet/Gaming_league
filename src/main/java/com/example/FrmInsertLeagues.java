package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FrmInsertLeagues extends JDialog {
    private JTextField txtNomLiga;
    private JComboBox<String> cbRank;
    private JComboBox<String> cbCategoria;
    private JTextField txtDuracionLiga;


    private Database db;
    private Object leagueID;

    public FrmInsertLeagues(Frame parent, Database db) {
        super(parent, "Insertar Liga", true);
        this.db = db;

        JPanel panel = EstiloForm.estiloPanel();

        txtNomLiga = new JTextField();
        txtDuracionLiga = new JTextField();
        cbRank = new JComboBox<>();
        cbCategoria = new JComboBox<>();


        panel.add(new JLabel("Nombre de la Liga:"));
        panel.add(txtNomLiga);
        panel.add(new JLabel("Rank:"));
        panel.add(cbRank);
        panel.add(new JLabel("Categoria:"));
        panel.add(cbCategoria);
        panel.add(new JLabel("Duracion:"));
        panel.add(txtDuracionLiga);


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

            // Cargar ranks
            ResultSet rs = db.query("SELECT DISTINCT rank FROM public.leagues ORDER BY rank");
            while (rs.next()) {
                cbRank.addItem(rs.getString("rank"));
            }

            // Cargar categorías
            rs = db.query("SELECT DISTINCT category FROM public.leagues ORDER BY category");
            while (rs.next()) {
                cbCategoria.addItem(rs.getString("category"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void insertar() {

        String nombreLiga = txtNomLiga.getText();
        String rank = cbRank.getSelectedItem().toString();
        String categoria = cbCategoria.getSelectedItem().toString();
        String duracionLiga = txtDuracionLiga.getText();

        String sql = """
        INSERT INTO public.leagues 
            (league_name, rank, category, league_duration)
        VALUES ( ?, ?, ? , ? )
    """;

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false); // INICIA TRANSACCIÓN

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombreLiga);
            ps.setString(2, rank);
            ps.setString(3, categoria);
            ps.setString(4, duracionLiga);
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
