package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FrmUpdateLeaguesGames extends JDialog {

    private JComboBox<String> cbLeague;
    private JComboBox<String> cbGame;
    private Database db;
    private Object oldLeagueID;

    // 1. Variable para el callback
    private Runnable onSuccess;

    public FrmUpdateLeaguesGames(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Liga - Juego", true);
        this.db = db;
        this.oldLeagueID = id;

        setLayout(new GridLayout(0, 2, 10, 10));

        cbLeague = new JComboBox<>();
        cbGame = new JComboBox<>();

        add(new JLabel("Liga:"));
        add(cbLeague);
        add(new JLabel("Juego:"));
        add(cbGame);

        JButton btnActualizar = new JButton("Actualizar");
        add(btnActualizar);

        btnActualizar.addActionListener(evt -> actualizar());

        cargarCombos();
        cargarDatos();

        pack();
        setLocationRelativeTo(parent);

        // 2. IMPORTANTE: ELIMINADO setVisible(true) de aquí.
        // Se llamará desde fuera después de configurar el setOnSuccess.
    }

    // 3. Método para configurar qué hacer al terminar
    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    private void cargarCombos() {
        try {
            ResultSet rs = db.query("SELECT league_id, league_name FROM leagues ORDER BY league_id");
            while (rs.next()) {
                cbLeague.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
            rs = db.query("SELECT game_code, game_name FROM games ORDER BY game_code");
            while (rs.next()) {
                cbGame.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        try {
            String sql = "SELECT league_id, game_code FROM leagues_game WHERE league_id = " + oldLeagueID;
            ResultSet rs = db.query(sql);
            if (rs.next()) {
                int currentLeague = rs.getInt("league_id");
                int currentGame = rs.getInt("game_code");
                seleccionarPorID(cbLeague, currentLeague);
                seleccionarPorID(cbGame, currentGame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seleccionarPorID(JComboBox<String> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).toString().startsWith(id + " -")) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void actualizar() {
        try {
            int newLeagueId = Integer.parseInt(cbLeague.getSelectedItem().toString().split(" - ")[0]);
            int newGameCode = Integer.parseInt(cbGame.getSelectedItem().toString().split(" - ")[0]);

            String sql = "UPDATE leagues_game SET league_id = ?, game_code = ? WHERE league_id = ?";

            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, newLeagueId);
            ps.setInt(2, newGameCode);
            ps.setInt(3, Integer.parseInt(oldLeagueID.toString()));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                // 4. Ejecutar el callback para recargar la tabla
                if (onSuccess != null) {
                    onSuccess.run();
                }
                JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}