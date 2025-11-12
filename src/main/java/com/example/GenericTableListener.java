package com.example;

import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class GenericTableListener implements TableModelListener {

    private static final Logger LOGGER = Logger.getLogger(GenericTableListener.class.getName());

    private final Database db;
    private final String tableName;       // nombre de la tabla
    private final String primaryKeyName;  // columna de clave primaria

    public GenericTableListener(Database db, String tableName, String primaryKeyName) {
        this.db = db;
        this.tableName = tableName;
        this.primaryKeyName = primaryKeyName;
    }

    @Override
    public void tableChanged(TableModelEvent event) {
        JDBCTableAdapter modelo = (JDBCTableAdapter) event.getSource();
        int row = event.getFirstRow();
        int column = event.getColumn();

        // No modificar la clave primaria
        if (column == 0) {
            return;
        }

        String colSQLName = modelo.getSQLColumnName(column);
        Object newValue = modelo.getValueAt(row, column);
        Object pkValue = modelo.getValueAt(row, 0);

        String sql = String.format(
                "UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                tableName, colSQLName, newValue, primaryKeyName, pkValue
        );

        LOGGER.info("Ejecutando: " + sql);

        try {
            db.update(sql);
        } catch (SQLException ex) {
            LOGGER.severe("Error en UPDATE: " + ex.getMessage());
        }
    }
}
