package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final String url = "jdbc:postgresql://localhost:5432/Gaming_League";
    private final String user = "developer";
    private final String password = "2006";

    public Database() {
        try {
            Class.forName("org.postgresql.Driver"); // cargar driver
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC");
        }
    }

    // metodo para la conexion a la base de datos
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // prueba
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Select
    // este metdo sirve para cualquier consulta SELECT
    // recibe una consulta y cuantas columnas quieres leer
    public List<String[]> selectQuery(String sql, int columnas) {
        // Crea una lista vacia para almacenar los resultados
        // Cada elemento sera un array de Strings representando una fila
        List<String[]> resultados = new ArrayList<>();

        //  automaticamente cierra Connection, PreparedStatement y ResultSet
        try (Connection conn = getConnection();           // Obtiene conexion a la base de datos
             PreparedStatement ps = conn.prepareStatement(sql);  // Prepara la consulta SQL
             ResultSet rs = ps.executeQuery()) {          // Ejecuta la consulta y obtiene resultados


            // rs.next() avanza a la siguiente fila y devuelve false cuando no hay mas filas
            while (rs.next()) {
                // Crea un nuevo array de Strings para almacenar los valores de la fila actual
                // El tamaño del array depende del numero de columnas que se le hayan mandado
                String[] row = new String[columnas];

                // pasa sobre cada columna de la fila actual
                for (int i = 0; i < columnas; i++) {
                    // Obtiene el valor de cada columna como String
                    // i+1 porque JDBC numera las columnas empezando desde 1, no desde 0
                    row[i] = rs.getString(i + 1);
                }

                // Agrega el array completo (la fila) a la lista de resultados
                resultados.add(row);
            }

            // Captura cualquier error de SQL que pueda ocurrir durante la conexión o consulta
        } catch (SQLException e) {
            // Muestra el mensaje de error en consola
            System.out.println("Error en SELECT: " + e.getMessage());
        }

        // Devuelve la lista con todos los resultados
        // Si hubo error o no hay resultados, devuelve lista vacía
        return resultados;
    }
}
