package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FrmUpdateTeams extends JDialog {

    private JTextField txtTeamName, txtDateCreated, txtDateDisbanded, txtNumMembers, txtUsersName, txtWins, txtTies, txtDefeats;
    private JComboBox<String> cbCreator;
    private Database db;
    private Object teamID;

    // Variable para callback
    private Runnable onSuccess;

    public FrmUpdateTeams(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Equipo", true);
        this.db = db;
        this.teamID = id;

        setLayout(new GridLayout(0, 2, 10, 10));

        txtTeamName = new JTextField();
        txtDateCreated = new JTextField();
        txtDateDisbanded = new JTextField();
        txtNumMembers = new JTextField();
        txtUsersName = new JTextField();
        txtWins = new JTextField();
        txtTies = new JTextField();
        txtDefeats = new JTextField();
        cbCreator = new JComboBox<>();

        add(new JLabel("Nombre del Equipo:")); add(txtTeamName);
        add(new JLabel("Fecha Creación (YYYY-MM-DD):")); add(txtDateCreated);
        add(new JLabel("Fecha Disolución (YYYY-MM-DD):")); add(txtDateDisbanded);
        add(new JLabel("Número Miembros:")); add(txtNumMembers);
        add(new JLabel("Nombre Usuario:")); add(txtUsersName);
        add(new JLabel("Victorias:")); add(txtWins);
        add(new JLabel("Empates:")); add(txtTies);
        add(new JLabel("Derrotas:")); add(txtDefeats);
        add(new JLabel("Creado por ID:")); add(cbCreator);

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
            ResultSet rs = db.query("SELECT player_id, first_name || ' ' || last_name FROM players ORDER BY player_id");
            while (rs.next()) cbCreator.addItem(rs.getInt(1) + " - " + rs.getString(2));
        } catch (Exception e) {}
    }

    private void cargarDatos() {
        try {
            ResultSet rs = db.query("SELECT * FROM teams WHERE team_id = " + teamID);
            if (rs.next()) {
                txtTeamName.setText(rs.getString("team_name"));
                txtDateCreated.setText(rs.getString("date_created"));
                String disband = rs.getString("date_disbanded");
                txtDateDisbanded.setText(disband != null ? disband : "");
                txtNumMembers.setText(String.valueOf(rs.getInt("number_members")));
                txtUsersName.setText(rs.getString("users_name"));
                txtWins.setText(String.valueOf(rs.getInt("wins")));
                txtTies.setText(String.valueOf(rs.getInt("ties")));
                txtDefeats.setText(String.valueOf(rs.getInt("defeats")));
                seleccionarPorID(cbCreator, rs.getInt("created_by_player_id"));
            }
        } catch (Exception e) {}
    }

    private void seleccionarPorID(JComboBox<String> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).startsWith(id + " -")) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void actualizar() {
        try {
            String sql = """
                UPDATE teams SET team_name=?, date_created=?, date_disbanded=?, 
                number_members=?, users_name=?, wins=?, ties=?, defeats=?, created_by_player_id=? 
                WHERE team_id=?
            """;
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtTeamName.getText());
            ps.setDate(2, java.sql.Date.valueOf(txtDateCreated.getText()));

            String disband = txtDateDisbanded.getText();
            if (disband.trim().isEmpty()) ps.setNull(3, java.sql.Types.DATE);
            else ps.setDate(3, java.sql.Date.valueOf(disband));

            ps.setInt(4, Integer.parseInt(txtNumMembers.getText()));
            ps.setString(5, txtUsersName.getText());
            ps.setInt(6, Integer.parseInt(txtWins.getText()));
            ps.setInt(7, Integer.parseInt(txtTies.getText()));
            ps.setInt(8, Integer.parseInt(txtDefeats.getText()));
            ps.setInt(9, Integer.parseInt(cbCreator.getSelectedItem().toString().split(" - ")[0]));
            ps.setInt(10, Integer.parseInt(teamID.toString()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                if (onSuccess != null) onSuccess.run(); // Callback
                JOptionPane.showMessageDialog(this, "Actualizado correctamente.");
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}