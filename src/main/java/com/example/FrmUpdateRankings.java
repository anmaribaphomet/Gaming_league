package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class FrmUpdateRankings extends JDialog {

    private JComboBox<String> cbNomJugador;
    private JComboBox<String> cbJuego;
    private JTextField txtRanking;
    private Runnable onSuccess;
    private Database db;
    private Object rankingID;

    public FrmUpdateRankings(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Ranking", true);
        this.db = db;
        this.rankingID = id;

        JPanel panel = EstiloForm.estiloPanel();

        cbNomJugador = new JComboBox<>();
        cbJuego = new JComboBox<>();
        txtRanking = new JTextField();

        panel.add(new JLabel("Nombre del Jugador:"));
        panel.add(cbNomJugador);
        panel.add(new JLabel("Nombre del Juego:"));
        panel.add(cbJuego);
        panel.add(new JLabel("Ranking:"));
        panel.add(txtRanking);

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
            // Cargar juegos
            ResultSet rs = db.query("SELECT game_code, game_name FROM public.games ORDER BY game_code");
            while (rs.next()) {
                cbJuego.addItem(rs.getInt("game_code") + " - " + rs.getString("game_name"));
            }

            // Cargar jugadores
            rs = db.query("SELECT player_id, first_name || ' ' || last_name AS nombre FROM public.players ORDER BY player_id");
            while (rs.next()) {
                cbNomJugador.addItem(rs.getInt("player_id") + " - " + rs.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        try {
            ResultSet rs = db.query(
                    "SELECT player_id, game_code, ranking " +
                            "FROM public.players_game_ranking WHERE player_id = " + rankingID
            );

            if (rs.next()) {
                int playerId = rs.getInt("player_id");
                int gameCode = rs.getInt("game_code");
                int ranking  = rs.getInt("ranking");

                seleccionarPorID(cbNomJugador, playerId);
                seleccionarPorID(cbJuego, gameCode);

                txtRanking.setText(String.valueOf(ranking));
            }

        } catch (Exception e) {
            e.printStackTrace();
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

        int playerId = Integer.parseInt(cbNomJugador.getSelectedItem().toString().split(" - ")[0]);
        int gameCode = Integer.parseInt(cbJuego.getSelectedItem().toString().split(" - ")[0]);
        int ranking = Integer.parseInt(txtRanking.getText());

        String sql = """
            UPDATE public.players_game_ranking
            SET game_code = ?, ranking = ?
            WHERE player_id = ?
        """;

        try {
            var conn = db.getConnection();
            var ps = conn.prepareStatement(sql);

            ps.setInt(1, gameCode);
            ps.setInt(2, ranking);
            ps.setInt(3, playerId);

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
