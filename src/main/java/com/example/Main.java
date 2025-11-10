package com.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //este lambda llama al hilo que maneja la interfaz grafica en el swing y le manda las caracteristicas
        SwingUtilities.invokeLater(() -> {

            // la conexion con la base de datos
            Database db = new Database();

            // Crea la interfaz grafica del menu pasandole la conexion a la base de datos
            // para  que el menu pueda hacer consultas a la BD
            MenuUI menu = new MenuUI(db);

            // Hace visible la ventana del menu en pantalla
            menu.setVisible(true);

// cierra el lambda
        });
    }
}
