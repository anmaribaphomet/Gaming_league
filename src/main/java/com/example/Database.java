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

    public ResultSet query(String sql) throws SQLException {

        ResultSet rs = null;
        Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        rs = statement.executeQuery(sql);

        return rs;
    }

    public ResultSet query(String sql, int scroll, int concur) throws SQLException {

        ResultSet rs = null;

        Statement statement = getConnection().createStatement(scroll, concur);
        rs = statement.executeQuery(sql);

        return rs;
    }

    public int update(String sql) throws SQLException {
        int result = -1;

        Statement statement = getConnection().createStatement(ResultSet.CONCUR_UPDATABLE,
                ResultSet.TYPE_FORWARD_ONLY);
        result = statement.executeUpdate(sql);
        return result;
    }

}
