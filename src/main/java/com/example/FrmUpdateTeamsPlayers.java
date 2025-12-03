package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FrmUpdateTeamsPlayers extends JDialog {

    private JComboBox<String> cbTeam;
    private JComboBox<String> cbPlayer;
    private JTextField txtDateFrom, txtDateTo;
    private Database db;
    private Object currentTeamId;
    private int originalPlayerId;
    private String originalDateFrom;

    // Variable para callback
    private Runnable onSuccess;

    public FrmUpdateTeamsPlayers(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Equipo-Jugador", true);
        this.db = db;
        this.currentTeamId = id;

        setLayout(new GridLayout(0, 2, 5, 5));

        cbTeam = new JComboBox<>();
        cbPlayer = new JComboBox<>();
        txtDateFrom = new JTextField();
        txtDateTo = new JTextField();

        add(new JLabel("Equipo:")); add(cbTeam);
        add(new JLabel("Jugador:")); add(cbPlayer);
        add(new JLabel("Fecha Desde:")); add(txtDateFrom);
        add(new JLabel("Fecha Hasta:")); add(txtDateTo);

        JButton btnActualizar = new JButton("Actualizar");
        add(btnActualizar);
        btnActualizar.addActionListener(evt -> actualizar());

        cargarCombos();
        cargarDatos();

        pack();
        setLocationRelativeTo(parent);
        // setVisible(true); // REMOVIDO
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    private void cargarCombos() {
        try {
            ResultSet rs = db.query("SELECT team_id, team_name FROM teams ORDER BY team_id");
            while (rs.next()) cbTeam.addItem(rs.getInt(1) + " - " + rs.getString(2));
            rs = db.query("SELECT player_id, first_name || ' ' || last_name FROM players ORDER BY player_id");
            while (rs.next()) cbPlayer.addItem(rs.getInt(1) + " - " + rs.getString(2));
        } catch (Exception e) {}
    }

    private void cargarDatos() {
        try {
            ResultSet rs = db.query("SELECT * FROM team_players WHERE team_id = " + currentTeamId + " LIMIT 1");
            if (rs.next()) {
                originalPlayerId = rs.getInt("player_id");
                originalDateFrom = rs.getString("date_from");
                seleccionarPorID(cbTeam, rs.getInt("team_id"));
                seleccionarPorID(cbPlayer, originalPlayerId);
                txtDateFrom.setText(originalDateFrom);
                String dTo = rs.getString("date_to");
                txtDateTo.setText(dTo != null ? dTo : "");
            }
        } catch (Exception e) {}
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
            int newTeamId = Integer.parseInt(cbTeam.getSelectedItem().toString().split(" - ")[0]);
            int newPlayerId = Integer.parseInt(cbPlayer.getSelectedItem().toString().split(" - ")[0]);
            String newDateFrom = txtDateFrom.getText();
            String newDateTo = txtDateTo.getText();

            String sql = """
                UPDATE team_players SET team_id=?, player_id=?, date_from=?, date_to=? 
                WHERE team_id=? AND player_id=? AND date_from=?
            """;

            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, newTeamId);
            ps.setInt(2, newPlayerId);
            ps.setDate(3, java.sql.Date.valueOf(newDateFrom));

            if (newDateTo == null || newDateTo.trim().isEmpty()) ps.setNull(4, java.sql.Types.DATE);
            else ps.setDate(4, java.sql.Date.valueOf(newDateTo));

            ps.setInt(5, Integer.parseInt(currentTeamId.toString()));
            ps.setInt(6, originalPlayerId);
            ps.setDate(7, java.sql.Date.valueOf(originalDateFrom));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                if (onSuccess != null) onSuccess.run(); // Callback
                JOptionPane.showMessageDialog(this, "Actualizado correctamente");
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}