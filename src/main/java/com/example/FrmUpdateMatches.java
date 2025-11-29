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

    private Database db;
    private Object matchID;

    public FrmUpdateMatches(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Match", true);
        this.db = db;
        this.matchID = id;

        setLayout(new GridLayout(0, 2, 5, 5));

        cbJuego = new JComboBox<>();
        cbCapitanA = new JComboBox<>();
        cbCapitanB = new JComboBox<>();
        txtFecha = new JTextField();
        txtResultado = new JTextField();
        txtResultadoteams = new JTextField();


        add(new JLabel("Juego:"));
        add(cbJuego);
        add(new JLabel("Capitán A:"));
        add(cbCapitanA);
        add(new JLabel("Capitán B:"));
        add(cbCapitanB);
        add(new JLabel("Fecha:"));
        add(txtFecha);
        add(new JLabel("Resultado:"));
        add(txtResultado);
        add(new JLabel("Resultado Equipo:"));
        add(txtResultadoteams);

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

        String fecha = txtFecha.getText();
        String resultado = txtResultado.getText();
        String resultado2 = txtResultadoteams.getText();


        String sql = String.format("""
    UPDATE public.matches
    SET game_code = %d,
        player_1_id = %d,
        player_2_id = %d,
        match_date = '%s',
        result_match = '%s',
        result_teams = '%s'
    WHERE match_id = %d
    """,
                gameCode,
                capA,
                capB,
                fecha,
                resultado,
                resultado2,
                matchID
        );



        try {
            db.update(sql);
            JOptionPane.showMessageDialog(this, "Actualizado correctamente");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
