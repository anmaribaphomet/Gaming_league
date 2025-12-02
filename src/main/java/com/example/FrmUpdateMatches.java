package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class FrmUpdateMatches extends JDialog {

    private JComboBox<String> cbJuego;
    private JComboBox<String> cbCapitanA;
    private JComboBox<String> cbCapitanB;
    private JTextField txtFecha;
    private JTextField txtResultado, txtResultadoteams;
    private Runnable onSuccess;
    private Database db;
    private Object matchID;

    public FrmUpdateMatches(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Match", true);
        this.db = db;
        this.matchID = id;

        JPanel panel = EstiloForm.estiloPanel();

        cbJuego = new JComboBox<>();
        cbCapitanA = new JComboBox<>();
        cbCapitanB = new JComboBox<>();
        txtFecha = new JTextField();
        txtResultado = new JTextField();
        txtResultadoteams = new JTextField();


        panel.add(new JLabel("Juego:"));
        panel.add(cbJuego);
        panel.add(new JLabel("Capitán A:"));
        panel.add(cbCapitanA);
        panel.add(new JLabel("Capitán B:"));
        panel.add(cbCapitanB);
        panel.add(new JLabel("Fecha:"));
        panel.add(txtFecha);
        panel.add(new JLabel("Resultado:"));
        panel.add(txtResultado);
        panel.add(new JLabel("Resultado Equipo:"));
        panel.add(txtResultadoteams);

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
            ResultSet rs = db.query("SELECT game_code, game_name FROM public.games ORDER BY game_code");
            while (rs.next()) {
                cbJuego.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
            rs = db.query("SELECT player_id, first_name || ' ' || last_name FROM public.players ORDER BY player_id");
            while (rs.next()) {
                String item = rs.getInt(1) + " - " + rs.getString(2);
                cbCapitanA.addItem(item);
                cbCapitanB.addItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        try {
            ResultSet rs = db.query("SELECT * FROM public.matches WHERE match_id = " + matchID);
            if (rs.next()) {
                int gameCode = rs.getInt("game_code");
                int captA = rs.getInt("player_1_id");
                int captB = rs.getInt("player_2_id");

                seleccionarPorID(cbJuego, gameCode);
                seleccionarPorID(cbCapitanA, captA);
                seleccionarPorID(cbCapitanB, captB);

                txtFecha.setText(rs.getString("match_date"));
                txtResultado.setText(rs.getString("result_match"));
                txtResultadoteams.setText(rs.getString("result_teams"));

            }
        } catch (Exception e) {}
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

        int gameCode = Integer.parseInt(cbJuego.getSelectedItem().toString().split(" - ")[0]);
        int capA     = Integer.parseInt(cbCapitanA.getSelectedItem().toString().split(" - ")[0]);
        int capB     = Integer.parseInt(cbCapitanB.getSelectedItem().toString().split(" - ")[0]);

        String fecha = txtFecha.getText(); // debe venir como YYYY-MM-DD
        String resultado = txtResultado.getText();
        String resultado2 = txtResultadoteams.getText();

        String sql = """
        UPDATE public.matches
        SET game_code = ?,
            player_1_id = ?,
            player_2_id = ?,
            match_date = ?,
            result_match = ?,
            result_teams = ?
        WHERE match_id = ?
    """;

        try {
            var conn = db.getConnection();
            var ps = conn.prepareStatement(sql);

            ps.setInt(1, gameCode);
            ps.setInt(2, capA);
            ps.setInt(3, capB);

            // convertir el String a java.sql.Date
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fecha);
            ps.setDate(4, fechaSQL);

            ps.setString(5, resultado);
            ps.setString(6, resultado2);
            ps.setInt(7, Integer.parseInt(matchID.toString()));

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
