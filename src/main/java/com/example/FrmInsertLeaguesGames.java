package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class FrmInsertLeaguesGames extends JDialog {

    private JComboBox<String> cbLeague;
    private JComboBox<String> cbGame;
    private Runnable onSuccess;
    private Database db;

    public FrmInsertLeaguesGames(Frame parent, Database db) {
        super(parent, "Asignar Juego a Liga", true);
        this.db = db;

        JPanel panel = EstiloForm.estiloPanel();
        cbLeague = new JComboBox<>();
        cbGame = new JComboBox<>();

        panel.add(new JLabel("Liga:"));
        panel.add(cbLeague);

        panel.add(new JLabel("Juego:"));
        panel.add(cbGame);


        JButton btnInsertar = EstiloForm.crearBoton("Insertar");
        btnInsertar.addActionListener(e -> insertar());
        panel.add(new JLabel()); // celda vacía para centrar
        panel.add(EstiloForm.centrar(btnInsertar));

        cargarCombos();
        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }


    private void cargarCombos() {
        try {
            // Cargar ligas
            ResultSet rs = db.query("SELECT league_id, league_name FROM public.leagues ORDER BY league_name");
            while (rs.next()) {
                cbLeague.addItem(rs.getInt("league_id") + " - " + rs.getString("league_name"));
            }

            // Cargar juegos
            rs = db.query("SELECT game_code, game_name FROM public.games ORDER BY game_name");
            while (rs.next()) {
                cbGame.addItem(rs.getInt("game_code") + " - " + rs.getString("game_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertar() {

        Connection conn = null;

        try {
            String leagueItem = cbLeague.getSelectedItem().toString();
            int leagueID = Integer.parseInt(leagueItem.split(" - ")[0]);

            String gameItem = cbGame.getSelectedItem().toString();
            int gameCode = Integer.parseInt(gameItem.split(" - ")[0]);

            String sql = """
            INSERT INTO public.leagues_game(league_id, game_code)
            VALUES (?, ?)
            """;

            conn = db.getConnection();
            conn.setAutoCommit(false); // INICIA TRANSACCIÓN

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, leagueID);
            ps.setInt(2, gameCode);

            ps.executeUpdate();

            conn.commit(); // CONFIRMA

            JOptionPane.showMessageDialog(this, "Juego asignado a la liga correctamente");
            if(onSuccess != null){
                onSuccess.run();
            }
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
