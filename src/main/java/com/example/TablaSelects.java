package com.example;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

    public class TablaSelects extends javax.swing.JInternalFrame {
        private  JTable tabla = null;

        public TablaSelects(String title, TableModel modelo) {
            super(title,true,true,true,true);

            this.initComponents(modelo);

        }

        private  void initComponents(TableModel modelo) {
            // Crear la tabla con los datos de la consulta
            tabla = new JTable(modelo);
            tabla.setPreferredScrollableViewportSize(new Dimension(640, 320));
            tabla.setFillsViewportHeight(true);

            //Crear un panel con scroll y agregar la tabla.
            JScrollPane scrollPane = new JScrollPane(tabla);

            this.getContentPane().add(scrollPane);
            this.pack();
        }
    }


