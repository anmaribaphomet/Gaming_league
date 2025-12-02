package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class FrmUpdateLeagues extends JDialog {
    private JTextField txtNomLiga;
    private JComboBox<String> cbRank;
    private JComboBox<String> cbCategoria;
    private JTextField txtDuracionLiga;

    private Runnable onSuccess;
    private Database db;
    private Object leagueID;

    public FrmUpdateLeagues(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Liga", true);
        this.db = db;
        this.leagueID = id;

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

        JButton btnActualizar = EstiloForm.crearBoton("Actualizar");
        btnActualizar.addActionListener(evt -> actualizar());

        panel.add(new JLabel()); // celda vacía para centrar
        panel.add(EstiloForm.centrar(btnActualizar));

        cargarCombos();
        cargarDatos();

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
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



    private void cargarDatos() {
        try {
            ResultSet rs = db.query(
                    "SELECT  league_name, rank, category, league_duration " +
                            "FROM public.leagues WHERE league_id = " + leagueID
            );

            if (rs.next()) {
                txtNomLiga.setText(rs.getString("league_name"));
                cbRank.setSelectedItem(rs.getString("rank"));
                cbCategoria.setSelectedItem(rs.getString("category"));
                txtDuracionLiga.setText(rs.getString("league_duration"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizar() {

        String nombreLiga = txtNomLiga.getText();
        String rank = cbRank.getSelectedItem().toString();
        String categoria = cbCategoria.getSelectedItem().toString();
        String duracionLiga = txtDuracionLiga.getText();

        String sql = """
            UPDATE public.leagues
            SET league_name = ?,
                rank = ?,
                category = ?,
                league_duration = ?
            WHERE league_id = ?
        """;

        try {
            var conn = db.getConnection();
            var ps = conn.prepareStatement(sql);

            ps.setString(1, nombreLiga);
            ps.setString(2, rank);
            ps.setString(3, categoria);
            ps.setString(4, duracionLiga);
            ps.setInt(5, Integer.parseInt(leagueID.toString()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Actualizado correctamente");
            if (onSuccess != null) onSuccess.run();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
