package com.example;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FrmInsertRankings extends JDialog {

    private JComboBox<String> cbNomJugador;
    private JComboBox<String> cbJuego;
    private JTextField txtRanking;

    private Database db;

    public FrmInsertRankings(Frame parent, Database db) {
        super(parent, "Insertar Ranking", true);
        this.db = db;

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

        JButton btnInsertar = EstiloForm.crearBoton("Insertar");
        btnInsertar.addActionListener(e -> insertar());
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

    private void insertar() {

        int playerId = Integer.parseInt(cbNomJugador.getSelectedItem().toString().split(" - ")[0]);
        int gameCode = Integer.parseInt(cbJuego.getSelectedItem().toString().split(" - ")[0]);
        int ranking = Integer.parseInt(txtRanking.getText());

        String sql = """
            INSERT INTO public.players_game_ranking
            (player_id, game_code, ranking)
            VALUES (?, ?, ?)
        """;

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, playerId);
            ps.setInt(2, gameCode);
            ps.setInt(3, ranking);

            ps.executeUpdate();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Insertado correctamente");
            dispose();

        } catch (Exception e) {

            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}

            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }
}
