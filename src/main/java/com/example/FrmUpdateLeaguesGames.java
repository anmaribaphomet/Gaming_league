package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class FrmUpdateLeaguesGames extends JDialog {

    private JComboBox<String> cbLeague;
    private JComboBox<String> cbGame;

    private Database db;
    private Object oldLeagueID; // Guardamos el ID original para saber a quién hacer el UPDATE

    public FrmUpdateLeaguesGames(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Liga - Juego", true);
        this.db = db;
        this.oldLeagueID = id; // Este es el league_id que seleccionaste en la tabla

        setLayout(new GridLayout(0, 2, 10, 10)); // Un poco más de espacio entre elementos

        // Inicializar componentes
        cbLeague = new JComboBox<>();
        cbGame = new JComboBox<>();

        // Agregar etiquetas y combos a la ventana
        add(new JLabel("Liga:"));
        add(cbLeague);

        add(new JLabel("Juego:"));
        add(cbGame);

        JButton btnActualizar = new JButton("Actualizar");
        add(btnActualizar);

        // Acción del botón
        btnActualizar.addActionListener(evt -> actualizar());

        // Cargar datos
        cargarCombos();
        cargarDatos();

        // Ajustes finales de la ventana
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void cargarCombos() {
        try {
            // 1. Cargar lista de Ligas
            ResultSet rs = db.query("SELECT league_id, league_name FROM leagues ORDER BY league_id");
            while (rs.next()) {
                cbLeague.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }

            // 2. Cargar lista de Juegos
            rs = db.query("SELECT game_code, game_name FROM games ORDER BY game_code");
            while (rs.next()) {
                cbGame.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando listas: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            // Buscamos el registro usando el ID de la liga que recibimos
            // NOTA: Según tus imágenes, las columnas son 'league_id' y 'game_code'
            String sql = "SELECT league_id, game_code FROM leagues_game WHERE league_id = " + oldLeagueID;

            ResultSet rs = db.query(sql);
            if (rs.next()) {
                int currentLeague = rs.getInt("league_id");
                int currentGame = rs.getInt("game_code");

                // Seleccionar los valores actuales en los combos
                seleccionarPorID(cbLeague, currentLeague);
                seleccionarPorID(cbGame, currentGame);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando datos del registro: " + e.getMessage());
        }
    }

    // Método auxiliar para buscar y seleccionar el item correcto en el ComboBox
    private void seleccionarPorID(JComboBox<String> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            // Asume que el formato en el combo es "ID - Nombre"
            if (combo.getItemAt(i).toString().startsWith(id + " -")) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void actualizar() {
        try {
            // 1. Obtener los nuevos IDs seleccionados por el usuario
            // Hacemos split para separar el ID del nombre (ej: "4 - Liga Pro" -> "4")
            int newLeagueId = Integer.parseInt(cbLeague.getSelectedItem().toString().split(" - ")[0]);
            int newGameCode = Integer.parseInt(cbGame.getSelectedItem().toString().split(" - ")[0]);

            // 2. Preparar la consulta SQL
            // UPDATE: cambiamos los valores DONDE el league_id sea el original
            String sql = "UPDATE leagues_game SET league_id = ?, game_code = ? WHERE league_id = ?";

            var conn = db.getConnection();
            var ps = conn.prepareStatement(sql);

            ps.setInt(1, newLeagueId);
            ps.setInt(2, newGameCode);

            // Usamos el oldLeagueID para identificar cuál fila estamos modificando
            ps.setInt(3, Integer.parseInt(oldLeagueID.toString()));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
                dispose(); // Cerrar la ventana al terminar
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar. Verifique si el registro aún existe.");
            }

        } catch (Exception e) {
            // Es posible que de error si intentas poner una combinación (liga+juego) que YA existe,
            // ya que ambos son Primary Key.
            JOptionPane.showMessageDialog(this, "Error al actualizar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
