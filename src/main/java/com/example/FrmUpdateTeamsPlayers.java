package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;        // Importación necesaria para evitar error en var
import java.sql.PreparedStatement; // Importación necesaria para evitar error en var
import java.sql.ResultSet;
import java.sql.SQLException;

public class FrmUpdateTeamsPlayers extends JDialog {

    // Componentes visuales igual que en tu ejemplo
    private JComboBox<String> cbTeam;
    private JComboBox<String> cbPlayer;
    private JTextField txtDateFrom;
    private JTextField txtDateTo;

    private Database db;

    // El ID principal que recibimos (team_id)
    private Object currentTeamId;

    // Variables para guardar los valores ORIGINALES (necesarios para el WHERE por la clave compuesta)
    private int originalPlayerId;
    private String originalDateFrom;

    public FrmUpdateTeamsPlayers(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Equipo-Jugador", true);
        this.db = db;
        this.currentTeamId = id; // Recibimos el ID del equipo

        setLayout(new GridLayout(0, 2, 5, 5));

        cbTeam = new JComboBox<>();
        cbPlayer = new JComboBox<>();
        txtDateFrom = new JTextField();
        txtDateTo = new JTextField();

        add(new JLabel("Equipo:"));
        add(cbTeam);
        add(new JLabel("Jugador:"));
        add(cbPlayer);
        add(new JLabel("Fecha Desde:"));
        add(txtDateFrom);
        add(new JLabel("Fecha Hasta:"));
        add(txtDateTo);

        JButton btnActualizar = new JButton("Actualizar");
        add(btnActualizar);

        btnActualizar.addActionListener(evt -> actualizar());

        cargarCombos();
        cargarDatos();

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void cargarCombos() {
        try {
            // Cargar Equipos
            ResultSet rs = db.query("SELECT team_id, team_name FROM public.teams ORDER BY team_id");
            while (rs.next()) {
                cbTeam.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
            // Cargar Jugadores
            rs = db.query("SELECT player_id, first_name || ' ' || last_name FROM public.players ORDER BY player_id");
            while (rs.next()) {
                cbPlayer.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        try {
            // Buscamos por el team_id (LIMIT 1 porque un equipo puede tener varios, tomamos el primero para editar)
            ResultSet rs = db.query("SELECT * FROM public.team_players WHERE team_id = " + currentTeamId + " LIMIT 1");

            if (rs.next()) {
                int tId = rs.getInt("team_id");
                int pId = rs.getInt("player_id");
                String dFrom = rs.getString("date_from");
                String dTo = rs.getString("date_to");

                // Guardamos referencias originales para el WHERE
                this.originalPlayerId = pId;
                this.originalDateFrom = dFrom;

                seleccionarPorID(cbTeam, tId);
                seleccionarPorID(cbPlayer, pId);

                txtDateFrom.setText(dFrom);
                txtDateTo.setText(dTo != null ? dTo : "");
            }
        } catch (Exception e) {
            // Manejo de error vacío como en tu ejemplo original
        }
    }

    private void seleccionarPorID(JComboBox<String> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (item.startsWith(id + " -")) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void actualizar() {

        // Obtener datos de los combos y cajas de texto
        int newTeamId = Integer.parseInt(cbTeam.getSelectedItem().toString().split(" - ")[0]);
        int newPlayerId = Integer.parseInt(cbPlayer.getSelectedItem().toString().split(" - ")[0]);

        String newDateFrom = txtDateFrom.getText(); // debe venir como YYYY-MM-DD
        String newDateTo = txtDateTo.getText();

        String sql = """
        UPDATE public.team_players
        SET team_id = ?,
            player_id = ?,
            date_from = ?,
            date_to = ?
        WHERE team_id = ? 
          AND player_id = ? 
          AND date_from = ?
        """;

        try {
            // CORRECCIÓN: Usamos tipos explícitos en lugar de 'var' y añadimos los imports arriba
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            // 1. Valores Nuevos (SET)
            ps.setInt(1, newTeamId);
            ps.setInt(2, newPlayerId);
            ps.setDate(3, java.sql.Date.valueOf(newDateFrom));

            // Manejo de fecha nula para date_to
            if (newDateTo == null || newDateTo.trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, java.sql.Date.valueOf(newDateTo));
            }

            // 2. Valores Antiguos (WHERE) - Usamos currentTeamId y las variables originales guardadas
            ps.setInt(5, Integer.parseInt(currentTeamId.toString()));
            ps.setInt(6, originalPlayerId);
            ps.setDate(7, java.sql.Date.valueOf(originalDateFrom));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Actualizado correctamente");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
