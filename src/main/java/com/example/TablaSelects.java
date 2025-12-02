package com.example;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TablaSelects extends javax.swing.JInternalFrame {
    private String sqlOriginal;

    private String currentCommand;
    private Eventos event;
    private JTable tabla;

    private JButton btnInsertar;
    private JButton btnEliminar;
    private JButton btnUpdate;

    public TablaSelects(String title, TableModel modelo, String command, Eventos event) {
        super(title, true, true, true, true);
        this.currentCommand = command;
        this.event = event;
        initComponents(modelo);
    }

    private void initComponents(TableModel modelo) {

        setLayout(new BorderLayout());

        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        btnInsertar = new JButton("Agregar");
        btnEliminar = new JButton("Eliminar");
        btnUpdate   = new JButton("Actualizar");

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        panelBotones.add(btnInsertar);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnUpdate);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.EAST);

        pack();

        btnInsertar.addActionListener(evt -> ejecutarInsert());
        btnEliminar.addActionListener(evt -> ejecutarDelete());
        btnUpdate.addActionListener(evt -> ejecutarUpdate());
    }

    public Object getIDSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return null;
        return tabla.getValueAt(fila, 0);
    }

    /** INSERT **/
    private void ejecutarInsert() {
        switch (currentCommand) {
            case "matches":       event.evtInsertMatches(null); break;
            case "players":       event.evtInsertPlayers(null); break;
            case "rankings":      event.evtInsertRankings(null); break;
            case "games":         event.evtInsertgames(null); break;
            case "leagues":       event.evtInsertLeagues(null); break;
            case "leagues_games": event.evtInsertLeaguesgames(null); break;
            case "teams":         event.evtInsertTeams(null); break;
            case "teams_players": event.evtInsertTeamsPlayers(null); break;

            default:
                JOptionPane.showMessageDialog(this,
                        "No hay insert configurado para: " + currentCommand);
        }
    }

    /** DELETE **/
    private void ejecutarDelete() {

        Object id = getIDSeleccionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.");
            return;
        }

        if (JOptionPane.showConfirmDialog(this,
                "¿Eliminar registro ID: " + id + "?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        switch (currentCommand) {
            case "players": event.evtEliminarPlayers(id); break;
            // agregar los demás si los vas implementando
            default:
                JOptionPane.showMessageDialog(this, "DELETE no configurado para: " + currentCommand);
        }
    }

    /** UPDATE **/
    private void ejecutarUpdate() {

        Object id = getIDSeleccionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para actualizar.");
            return;
        }

        switch (currentCommand) {
            case "matches":
                event.evtUpdateMatches(id);
                break;

            case "players":
                event.evtUpdatePlayers(id);
                break;

            case "rankings":
                event.evtUpdateRankings(id);
                break;

            case "games":
                event.evtUpdateGames(id);
                break;

            case "leagues":
                event.evtUpdateLeagues(id);
                break;

            case "leagues_games":
                event.evtUpdateLeaguesGames(id);
                break;

            case "teams":
                event.evtUpdateTeams(id);
                break;

            case "teams_players":
                event.evtUpdateTeamsPlayers(id);
                break;

            default:
                JOptionPane.showMessageDialog(this,
                        "UPDATE no configurado para: " + currentCommand);
        }
    }

    public String getCurrentCommand() {
        return currentCommand;
    }

    public void recargarTabla() {
        try {
            TableModel nuevoModelo = event.obtenerModeloRefrescado(currentCommand);
            actualizarModelo(nuevoModelo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al refrescar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarModelo(TableModel nuevoModelo) {
        tabla.setModel(nuevoModelo);
        if (nuevoModelo instanceof AbstractTableModel atm) {
            atm.fireTableDataChanged(); // Fuerza que JTable se redibuje
        }
    }

}
