package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class FrmUpdateTeams extends JDialog {

    // Componentes de la interfaz
    private JTextField txtTeamName;
    private JTextField txtDateCreated;
    private JTextField txtDateDisbanded;
    private JTextField txtNumMembers;
    private JTextField txtUsersName; // Columna 'users_name'
    private JTextField txtWins;
    private JTextField txtTies;
    private JTextField txtDefeats;
    private JComboBox<String> cbCreator; // Para 'created_by_player_id'

    private Database db;
    private Object teamID;

    public FrmUpdateTeams(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Equipo", true);
        this.db = db;
        this.teamID = id;

        // Usamos un GridLayout de 0 filas y 2 columnas para organizar etiqueta-campo
        setLayout(new GridLayout(0, 2, 10, 10));

        // --- Inicializar componentes ---
        txtTeamName = new JTextField();
        txtDateCreated = new JTextField();      // Formato YYYY-MM-DD
        txtDateDisbanded = new JTextField();    // Formato YYYY-MM-DD
        txtNumMembers = new JTextField();
        txtUsersName = new JTextField();
        txtWins = new JTextField();
        txtTies = new JTextField();
        txtDefeats = new JTextField();
        cbCreator = new JComboBox<>();

        // --- Añadir a la ventana ---
        add(new JLabel("Nombre del Equipo (team_name):"));
        add(txtTeamName);

        add(new JLabel("Fecha Creación (date_created):"));
        add(txtDateCreated);

        add(new JLabel("Fecha Disolución (date_disbanded):"));
        add(txtDateDisbanded);

        add(new JLabel("Número Miembros (number_members):"));
        add(txtNumMembers);

        add(new JLabel("Nombre Usuario (users_name):"));
        add(txtUsersName);

        add(new JLabel("Victorias (wins):"));
        add(txtWins);

        add(new JLabel("Empates (ties):"));
        add(txtTies);

        add(new JLabel("Derrotas (defeats):"));
        add(txtDefeats);

        add(new JLabel("Creado por (Player ID):"));
        add(cbCreator);

        // Botón de acción
        JButton btnActualizar = new JButton("Actualizar");
        add(btnActualizar);

        // Evento del botón
        btnActualizar.addActionListener(evt -> actualizar());

        // Cargar datos iniciales
        cargarCombos();
        cargarDatos();

        // Ajustar tamaño y mostrar
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void cargarCombos() {
        try {
            // Cargar lista de jugadores para asignar el 'created_by_player_id'
            ResultSet rs = db.query("SELECT player_id, first_name || ' ' || last_name FROM players ORDER BY player_id");
            while (rs.next()) {
                cbCreator.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando jugadores: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            // Consulta exacta basada en tu imagen
            String sql = "SELECT * FROM teams WHERE team_id = " + teamID;
            ResultSet rs = db.query(sql);

            if (rs.next()) {
                // Llenar campos de texto
                txtTeamName.setText(rs.getString("team_name"));
                txtDateCreated.setText(rs.getString("date_created"));

                // Manejo de fecha nula (por si el equipo no se ha disuelto)
                String disband = rs.getString("date_disbanded");
                txtDateDisbanded.setText(disband != null ? disband : "");

                txtNumMembers.setText(String.valueOf(rs.getInt("number_members")));
                txtUsersName.setText(rs.getString("users_name"));

                txtWins.setText(String.valueOf(rs.getInt("wins")));
                txtTies.setText(String.valueOf(rs.getInt("ties")));
                txtDefeats.setText(String.valueOf(rs.getInt("defeats")));

                // Seleccionar el creador en el ComboBox
                int creatorId = rs.getInt("created_by_player_id");
                seleccionarPorID(cbCreator, creatorId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando datos del equipo: " + e.getMessage());
        }
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
            // 1. Obtener datos de los campos
            String name = txtTeamName.getText();
            java.sql.Date dateCreated = java.sql.Date.valueOf(txtDateCreated.getText());

            // Manejo de fecha nula para disbanded
            java.sql.Date dateDisbanded = null;
            if (!txtDateDisbanded.getText().trim().isEmpty()) {
                dateDisbanded = java.sql.Date.valueOf(txtDateDisbanded.getText());
            }

            int members = Integer.parseInt(txtNumMembers.getText());
            String userName = txtUsersName.getText();
            int wins = Integer.parseInt(txtWins.getText());
            int ties = Integer.parseInt(txtTies.getText());
            int defeats = Integer.parseInt(txtDefeats.getText());

            // Obtener ID del jugador seleccionado
            int creatorId = Integer.parseInt(cbCreator.getSelectedItem().toString().split(" - ")[0]);

            // 2. Consulta SQL Actualizada
            String sql = """
                UPDATE teams
                SET team_name = ?,
                    date_created = ?,
                    date_disbanded = ?,
                    number_members = ?,
                    users_name = ?,
                    wins = ?,
                    ties = ?,
                    defeats = ?,
                    created_by_player_id = ?
                WHERE team_id = ?
            """;

            var conn = db.getConnection();
            var ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setDate(2, dateCreated);
            ps.setDate(3, dateDisbanded); // Puede ser null
            ps.setInt(4, members);
            ps.setString(5, userName);
            ps.setInt(6, wins);
            ps.setInt(7, ties);
            ps.setInt(8, defeats);
            ps.setInt(9, creatorId);

            // WHERE team_id...
            ps.setInt(10, Integer.parseInt(teamID.toString()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Equipo actualizado correctamente.");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato de número: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error en formato de fecha (Use YYYY-MM-DD): " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar en BD: " + e.getMessage());
        }
    }
}
