package com.example;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class FrmInsertTeams extends JDialog {

    private JTextField txtTeamNombre;
    private JTextField txtDateCreated;
    private JTextField txtDateDisbanded;
    private JTextField txtNumMiembros;
    private JTextField txtUsuariosNom;
    private JTextField txtGanadas;
    private JTextField txtEmpates;
    private JTextField txtDerrotas;
    private JComboBox<String> cbCreadorJuego;
    private Runnable onSuccess;
    private Database db;

    public FrmInsertTeams(Frame parent, Database db) {
        super(parent, "Insertar Equipo", true);
        this.db = db;

        JPanel panel = EstiloForm.estiloPanel();

        txtTeamNombre = new JTextField();
        txtDateCreated = new JTextField();
        txtDateDisbanded = new JTextField();
        txtNumMiembros = new JTextField();
        txtUsuariosNom = new JTextField();
        txtGanadas = new JTextField();
        txtEmpates = new JTextField();
        txtDerrotas = new JTextField();
        cbCreadorJuego = new JComboBox<>();

        panel.add(new JLabel("Nombre del Equipo:"));
        panel.add(txtTeamNombre);
        panel.add(new JLabel("Fecha de Creación (YYYY-MM-DD):"));
        panel.add(txtDateCreated);
        panel.add(new JLabel("Fecha de Disolución (YYYY-MM-DD):"));
        panel.add(txtDateDisbanded);
        panel.add(new JLabel("Número de Miembros:"));
        panel.add(txtNumMiembros);
        panel.add(new JLabel("Nombre del Usuario:"));
        panel.add(txtUsuariosNom);
        panel.add(new JLabel("Victorias:"));
        panel.add(txtGanadas);
        panel.add(new JLabel("Empates:"));
        panel.add(txtEmpates);
        panel.add(new JLabel("Derrotas:"));
        panel.add(txtDerrotas);
        panel.add(new JLabel("Jugador Creador:"));
        panel.add(cbCreadorJuego);

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
            ResultSet rs = db.query(
                    "SELECT player_id, first_name || ' ' || last_name AS nombre " +
                            "FROM public.players ORDER BY player_id"
            );

            while (rs.next()) {
                cbCreadorJuego.addItem(
                        rs.getInt("player_id") + " - " + rs.getString("nombre")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertar() {

        String teamName = txtTeamNombre.getText();
        String dateCreated = txtDateCreated.getText();
        String dateDisbanded = txtDateDisbanded.getText().trim();
        String numberMembers = txtNumMiembros.getText();
        String usersName = txtUsuariosNom.getText();
        int wins = Integer.parseInt(txtGanadas.getText());
        int ties = Integer.parseInt(txtEmpates.getText());
        int defeats = Integer.parseInt(txtDerrotas.getText());

        // ID del jugador
        int playerID = Integer.parseInt(cbCreadorJuego.getSelectedItem().toString().split(" - ")[0]);

        String sql = """
            INSERT INTO public.teams(
                team_name,
                date_created,
                date_disbanded,
                number_members,
                users_name,
                wins,
                ties,
                defeats,
                created_by_player_id
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, teamName);
            ps.setDate(2, java.sql.Date.valueOf(dateCreated));
            ps.setDate(3, java.sql.Date.valueOf(dateDisbanded));
            ps.setInt(4, Integer.parseInt(numberMembers));
            ps.setString(5, usersName);
            ps.setInt(6, wins);
            ps.setInt(7, ties);
            ps.setInt(8, defeats);
            ps.setInt(9, playerID);

            ps.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Equipo insertado correctamente");
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
