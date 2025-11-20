package com.example;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TableInsert {

    private static final Logger LOGGER = Logger.getLogger(TableInsert.class.getName());

    public static void mostrar(Database db, String titulo, String[] labels, String sql, int[] intColumns ) {

        //Diseño y recoleccion de los datos
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        List<JTextField> textFields = new ArrayList<>();

        for (String label : labels) {
            panel.add(new JLabel(label));
            JTextField tf = new JTextField(15);
            panel.add(tf);
            textFields.add(tf);
        }

        int result = JOptionPane.showConfirmDialog(null, panel,
                titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return; // El usuario canceló
        }


        for (int i = 0; i < labels.length; i++) {
            String text = textFields.get(i).getText().trim();

            // Si el campo obligatorio está vacío, mostramos un error y salimos
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "ERROR: El campo '" + labels[i] + "' es obligatorio y no puede estar vacío.",
                        "Datos Faltantes", JOptionPane.ERROR_MESSAGE);
                return; // Termina el método sin intentar la transacción
            }
        }
        Connection conn = null; // Inicializamos la conexión fuera del try

        try {
            //Recoleccion y extraccion de los datos que se encuentran en los labels
            Object[] parametros = new Object[labels.length];//parametros por medio de un Objeto , el cual realiza un objeto del tamaño de la cantidad de labels que hay en el formulario
            for (int i = 0; i < labels.length; i++) {
                String text = textFields.get(i).getText().trim();

                boolean entero = false;
                if (entero) {
                    //Conversion a enteros
                    parametros[i] = Integer.parseInt(text);
                } else {
                    parametros[i] = text;//Campo de texto normal
                }
            }

            //TRANSACCION
            conn = db.getConnection();//Conexion a la base de datos
            conn.setAutoCommit(false); // Se inicia la transacción

            // Se ejecuta  INSERT usando el metodo en la clase de DATABASE
            int rows = db.executePstmt(conn, sql, parametros);

            if (rows > 0) {
                conn.commit(); // Si hay exito se confirman los cambios
                JOptionPane.showMessageDialog(null, "Registro guardado exitosamente (Transacción completada).");
            } else {
                conn.rollback(); // Si Falla se deshace los cambios
                JOptionPane.showMessageDialog(null, "No se pudo guardar el registro (Transacción deshecha).");
            }

        } catch (NumberFormatException e) {
            try {
                if (conn != null) conn.rollback(); // Siempre hacer rollback en caso de error de datos
            } catch (SQLException rollbackEx) {
                LOGGER.severe("Error al intentar ROLLBACK: " + rollbackEx.getMessage());
            }
            LOGGER.severe("Error de formato numérico: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error: Un campo numérico tiene texto inválido.");
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback en caso de error SQL
            } catch (SQLException rollbackEx) {
                LOGGER.severe("Error al intentar ROLLBACK: " + rollbackEx.getMessage());
            }
            LOGGER.severe("Error SQL en INSERT: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error en base de datos: " + e.getMessage());
        } finally {
            // Asegurarse de que la conexión se cierre
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    LOGGER.severe("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }
    }
