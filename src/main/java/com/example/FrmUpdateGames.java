package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class FrmUpdateGames extends JDialog {

    private JTextField txtNomGame;
    private JTextField txtDescripcion;
    private JComboBox<String> cbPlataforma;
    private JComboBox<String> cbCategoria;

    private Runnable onSuccess;
    private Database db;
    private Object gameID;

    public FrmUpdateGames(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Juego", true);
        this.db = db;
        this.gameID = id;

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



    private void cargarDatos() {
        try {
            ResultSet rs = db.query(
                    "SELECT game_code, game_name, game_description, platform, category " +
                            "FROM public.games WHERE game_code = " + gameID
            );

            if (rs.next()) {
                txtNomGame.setText(rs.getString("game_name"));
                txtDescripcion.setText(rs.getString("game_description"));

                cbPlataforma.setSelectedItem(rs.getString("platform"));
                cbCategoria.setSelectedItem(rs.getString("category"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizar() {

        String nombreJuego = txtNomGame.getText();
        String descJuego = txtDescripcion.getText();
        String plataforma = cbPlataforma.getSelectedItem().toString();
        String categoria = cbCategoria.getSelectedItem().toString();

        String sql = """
            UPDATE public.games
            SET game_name = ?,
                game_description = ?,
                platform = ?,
                category = ?
            WHERE game_code = ?
        """;

        try {
            var conn = db.getConnection();
            var ps = conn.prepareStatement(sql);

            ps.setString(1, nombreJuego);
            ps.setString(2, descJuego);
            ps.setString(3, plataforma);
            ps.setString(4, categoria);
            ps.setInt(5, Integer.parseInt(gameID.toString()));

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

