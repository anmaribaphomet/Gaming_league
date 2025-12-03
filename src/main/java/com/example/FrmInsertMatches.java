package com.example;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class FrmInsertMatches extends JDialog {

    private JComboBox<String> cbJuego;
    private JComboBox<String> cbCapitanA;
    private JComboBox<String> cbCapitanB;
    private JTextField txtFecha;
    private JTextField txtResultado, txtResultadoTeams;
    private Runnable onSuccess;
    private Database db;

    public FrmInsertMatches(Frame parent, Database db) {
        super(parent, "Insertar Match", true);
        this.db = db;

        JPanel panel = EstiloForm.estiloPanel();

        cbJuego = new JComboBox<>();
        cbCapitanA = new JComboBox<>();
        cbCapitanB = new JComboBox<>();
        txtFecha = new JTextField();
        txtResultado = new JTextField();
        txtResultadoTeams = new JTextField();

        panel.add(new JLabel("Juego:"));
        panel.add(cbJuego);
        panel.add(new JLabel("Capitán A:"));
        panel.add(cbCapitanA);
        panel.add(new JLabel("Capitán B:"));
        panel.add(cbCapitanB);
        panel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panel.add(txtFecha);
        panel.add(new JLabel("Resultado:"));
        panel.add(txtResultado);
        panel.add(new JLabel("Resultado Equipos:"));
        panel.add(txtResultadoTeams);

        JButton btnInsertar = EstiloForm.crearBoton("Insertar");
        btnInsertar.addActionListener(evt -> insertar());
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

    private void insertar() {

        int gameCode = Integer.parseInt(cbJuego.getSelectedItem().toString().split(" - ")[0]);
        int capA     = Integer.parseInt(cbCapitanA.getSelectedItem().toString().split(" - ")[0]);
        int capB     = Integer.parseInt(cbCapitanB.getSelectedItem().toString().split(" - ")[0]);

        String fecha = txtFecha.getText();
        String resultado = txtResultado.getText();
        String resultadoTeams = txtResultadoTeams.getText();

        String sql = """
        INSERT INTO public.matches 
            (game_code, player_1_id, player_2_id, match_date, result_match, result_teams)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false); // INICIA TRANSACCIÓN

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, gameCode);
            ps.setInt(2, capA);
            ps.setInt(3, capB);

            java.sql.Date fechaSQL = java.sql.Date.valueOf(fecha);
            ps.setDate(4, fechaSQL);

            ps.setString(5, resultado);
            ps.setString(6, resultadoTeams);

            ps.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(this, "Insertado correctamente");
            if(onSuccess != null){
                onSuccess.run();
            }
            dispose();

        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception ex3) {
                ex3.printStackTrace();
            }
        }
    }

}
