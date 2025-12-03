package com.example;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class FrmInsertTeamsPlayers extends JDialog {

    private JComboBox<String> cbEquipo;
    private JComboBox<String> cbJugador;
    private JTextField txtFechaIngreso;
    private JTextField txtFechaSalida;
    private Runnable onSuccess;
    private Database db;

    public FrmInsertTeamsPlayers(Frame parent, Database db) {
        super(parent, "Insertar Miembro de Equipo", true);
        this.db = db;

        JPanel panel = EstiloForm.estiloPanel();

        cbEquipo = new JComboBox<>();
        cbJugador = new JComboBox<>();
        txtFechaIngreso = new JTextField();
        txtFechaSalida = new JTextField();

        panel.add(new JLabel("Equipo:"));
        panel.add(cbEquipo);

        panel.add(new JLabel("Jugador:"));
        panel.add(cbJugador);

        panel.add(new JLabel("Fecha de Ingreso (YYYY-MM-DD):"));
        panel.add(txtFechaIngreso);

        panel.add(new JLabel("Fecha de Salida (YYYY-MM-DD):"));
        panel.add(txtFechaSalida);

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
            // Cargar equipos
            ResultSet rs = db.query(
                    "SELECT team_id, team_name FROM public.teams ORDER BY team_id"
            );
            while (rs.next()) {
                cbEquipo.addItem(
                        rs.getInt("team_id") + " - " + rs.getString("team_name")
                );
            }

            // Cargar jugadores
            rs = db.query(
                    "SELECT player_id, first_name || ' ' || last_name AS nombre " +
                            "FROM public.players ORDER BY player_id"
            );
            while (rs.next()) {
                cbJugador.addItem(
                        rs.getInt("player_id") + " - " + rs.getString("nombre")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertar() {

        int teamID = Integer.parseInt(cbEquipo.getSelectedItem().toString().split(" - ")[0]);
        int playerID = Integer.parseInt(cbJugador.getSelectedItem().toString().split(" - ")[0]);

        String fechaIngreso = txtFechaIngreso.getText();
        String fechaSalida = txtFechaSalida.getText().trim();

        String sql = """
            INSERT INTO public.team_players(
                team_id, player_id, date_from, date_to
            )
            VALUES (?, ?, ?, ?)
        """;

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false); // iniciar transacción

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, teamID);
            ps.setInt(2, playerID);
            ps.setDate(3, java.sql.Date.valueOf(fechaIngreso));
            ps.setDate(4, java.sql.Date.valueOf(fechaSalida));

            ps.executeUpdate();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Miembro agregado al equipo correctamente");
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
