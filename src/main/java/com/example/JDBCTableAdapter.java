package com.example;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class JDBCTableAdapter extends DefaultTableModel {

    private static final String CLASS_NAME = JDBCTableAdapter.class.getSimpleName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private String tableName;
    private String sqlColumnNames[];
    private int sqlColumnTypes[];

    public JDBCTableAdapter(ResultSet rs, String name) {
        super();
        cargarDatos(rs, true);
        tableName = name;
    }

    public JDBCTableAdapter(ResultSet rs) {
        super();
        cargarDatos(rs, false);
    }

    private void cargarDatos(ResultSet rs, boolean ignoreTableMetaName) {

        int rows = 0;
        int cols = 0;

        Object data[][] = null;
        Object visibleColumnNames[] = null;

        try {
            ResultSetMetaData meta = rs.getMetaData();

            if (!ignoreTableMetaName) {
                tableName = meta.getTableName(1);
            }

            cols = meta.getColumnCount();

            visibleColumnNames = new Object[cols];
            sqlColumnNames = new String[cols];
            sqlColumnTypes = new int[cols];


            int c = 0;
            for (int i = 1; i <= cols; i++) {

                String alias = meta.getColumnLabel(i);   // ej: "first_name:Nombre"

                if (alias.contains(":")) {
                    String[] parts = alias.split(":", 2);

                    sqlColumnNames[c] = parts[0];   // <-- nombre SQL
                    visibleColumnNames[c] = parts[1]; // <-- nombre visible
                } else {
                    sqlColumnNames[c] = alias;
                    visibleColumnNames[c] = alias;
                }

                sqlColumnTypes[c] = meta.getColumnType(i);
                c++;
            }


            rs.last();
            rows = rs.getRow();

            data = new Object[rows][cols];

            rs.beforeFirst();


            int r = 0;
            while (rs.next()) {
                c = 0;
                for (int i = 1; i <= cols; i++) {
                    data[r][c++] = rs.getString(i);
                }
                r++;
            }

        } catch (SQLException ex) {
            LOGGER.severe("Error: " + ex.getMessage());
        }

        this.setDataVector(data, visibleColumnNames);
    }

    public String getSQLColumnName(int i) {
        if (i >= 0 && i < sqlColumnNames.length) {
            return sqlColumnNames[i];
        }
        return null;
    }

    public int getSQLColumnType(int i) {
        if (i >= 0 && i < sqlColumnTypes.length) {
            return sqlColumnTypes[i];
        }
        return -1;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
