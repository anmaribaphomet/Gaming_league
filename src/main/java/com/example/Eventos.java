package com.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Eventos {
    private Database db;
    private javax.swing.JDesktopPane desktopPane;
    private static final String CLASS_NAME = Main.class.getSimpleName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    // 🔹 Constructor verdadero (sin void)
    public Eventos(javax.swing.JDesktopPane desktopPane) {
        this.desktopPane = desktopPane;

        db = new Database(); // inicializa la conexión una vez
        if (db.testConnection()) {
            System.out.println("Conexión exitosa a la base de datos");
        } else {
            System.out.println("Error al conectar con la base de datos");
        }
    }

    //----------------------MATCHES-------------------
    //Select
    public void evtSelectMatches(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
                 SELECT\s
              m.match_id,
             g.game_name AS Juego,
             p1.first_name || ' ' || p1.last_name AS Jugador1,
             p2.first_name || ' ' || p2.last_name AS Jugador2,
             m.match_date,
             m.result_match,
             m.result_teams
             FROM public.matches m
             JOIN public.games g ON m.game_code = g.game_code
             JOIN public.players p1 ON m.player_1_id = p1.player_id
            JOIN public.players p2 ON m.player_2_id = p2.player_id
            ORDER BY m.match_id;
                    
            """;

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "matches", "match_id"));

            TablaSelects browser = new TablaSelects("Matches", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    //Insertar con transacciones

    public void evtInsertMatches(java.awt.event.ActionEvent evt) {
        //TODO
    }


    //Eliminar con transacciones

    //Eliminar por id (matches_id)
    public void evtEliminarClaveMatches(java.awt.event.ActionEvent evt) {

        // Pedir al usuario el ID del match a eliminar
        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID del match que deseas eliminar:",
                "Eliminar match por ID",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return; // Si presiona Cancelar, salir

        int matchId;
        try {
            matchId = Integer.parseInt(input); // Convertir input a entero
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) { // Abrir conexión a la base de datos
            conn.setAutoCommit(false); // Iniciamos la transacción manualmente, todo lo que hagamos después de esto hasta
            // commit() o rollback() estará dentro de la transacción

            try {
                // Preparar DELETE usando match_id
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM matches WHERE match_id = ?"
                );
                stmt.setInt(1, matchId);

                int rows = stmt.executeUpdate(); // Ejecutamos la eliminación dentro de la transacción

                if (rows > 0) {
                    conn.commit(); // Confirmamos la transacción: los cambios se aplican permanentemente
                    JOptionPane.showMessageDialog(null,
                            "Match eliminado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    conn.rollback(); // Si no se eliminó nada, revertimos la transacción
                    JOptionPane.showMessageDialog(null,
                            "No existe un match con ese ID.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException e) {
                conn.rollback(); // Si ocurre algún error dentro de la transacción, revertimos todo
                JOptionPane.showMessageDialog(null,
                        "Error al eliminar el match:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                conn.setAutoCommit(true); // Restauramos el modo autocommit para futuras operaciones
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error de conexión a la base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //eliminar por codigo de juego (game_code)
    public void evtEliminarJuegoMatches(java.awt.event.ActionEvent evt) {

        // Pedir al usuario el código del juego
        String input = JOptionPane.showInputDialog(null,
                "Ingresa el código del juego cuyos matches deseas eliminar:",
                "Eliminar matches por código de juego",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        int gameCode;
        try {
            gameCode = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El código debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) { // Abrimos conexión
            conn.setAutoCommit(false); // Iniciamos transacción, todas las operaciones siguientes se ejecutarán dentro de esta
            // transacción

            try {
                // Preparar DELETE para eliminar todos los matches con el game_code especificado
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM matches WHERE game_code = ?"
                );
                stmt.setInt(1, gameCode);

                int rows = stmt.executeUpdate(); // Ejecutamos DELETE dentro de la transacción

                if (rows > 0) {
                    conn.commit(); // Confirmamos la transacción: los matches se eliminan definitivamente
                    JOptionPane.showMessageDialog(null,
                            "Matches eliminados correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    conn.rollback(); // Si no hubo registros afectados, revertimos la transacción
                    JOptionPane.showMessageDialog(null,
                            "No existen matches con ese código de juego.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException e) {
                conn.rollback(); // Si ocurre algún error durante la transacción, se revierte todo
                JOptionPane.showMessageDialog(null,
                        "Error al eliminar los matches:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                conn.setAutoCommit(true); // Restauramos autocommit para otras operaciones fuera de la transacción
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error de conexión a la base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }




    //-------------------------SELECT (BUSCAR)  eventos

    //Evento del Select de players
    public void evtTablaPlayers(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
            SELECT player_id, first_name, last_name, gender, address, telephone_number, email, age
            FROM public.players
            ORDER BY player_id;
            """;

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "players", "player_id"));

            TablaSelects browser = new TablaSelects("Jugadores", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    //Evento de rankings
    public void evtTablaRankings(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
                    SELECT
                        pgr.player_id,
                        pl.first_name || ' ' || pl.last_name AS player_name,
                        g.game_name,
                        pgr.ranking
                    FROM public.players_game_ranking AS pgr
                    JOIN public.players AS pl
                        ON pgr.player_id = pl.player_id
                    JOIN public.games AS g
                        ON pgr.game_code = g.game_code
                    ORDER BY pgr.player_id;
                    
                    """;

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "players_game_ranking", "player_id"));

            TablaSelects browser = new TablaSelects("Rankings", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectgames(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
                    SELECT game_code, game_name, game_description, platform, category
                                            	FROM public.games order by game_code;""";

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "games", "game_code"));

            TablaSelects browser = new TablaSelects("Juegos", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectLeagues(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
                    SELECT league_id, league_name, rank, category, league_duration
                                                               	FROM public.leagues order by league_id;""";

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "leagues", "league_id"));

            TablaSelects browser = new TablaSelects("Ligas", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectLeaguesgames(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
                    SELECT\s
                     lg.league_id,
                     l.league_name,
                     lg.game_code,
                     g.game_name
                     FROM public.leagues_game AS lg
                     JOIN public.leagues AS l
                     ON lg.league_id = l.league_id
                     JOIN public.games AS g
                     ON lg.game_code = g.game_code
                     ORDER BY lg.league_id;
                    """;

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "leagues_game", "league_id"));

            TablaSelects browser = new TablaSelects("Ligas de Juegos", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectTeams(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
                    SELECT team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id
                    	FROM public.teams order by team_id;""";

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "teams", "team_id"));

            TablaSelects browser = new TablaSelects("Equipos", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectTeamsPlayers(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
                    SELECT\s
                        tp.team_id,
                        t.team_name,
                        tp.player_id,
                        pl.first_name || ' ' || pl.last_name AS player_name,
                        tp.date_from,
                        tp.date_to
                    FROM public.team_players AS tp
                    JOIN public.teams AS t
                        ON tp.team_id = t.team_id
                    JOIN public.players AS pl
                        ON tp.player_id = pl.player_id
                    ORDER BY tp.team_id;
                    """;

            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria
            modelo.addTableModelListener(new GenericTableListener(db, "teams_players", "team_id"));

            TablaSelects browser = new TablaSelects("Jugadores del Equipo", modelo);
            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }





    //-------------------------INSERT eventos
    public void evtInsertPlayers(java.awt.event.ActionEvent evt) {
        //TODO
    }

    public void evtInsertRankings(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtInsertgames(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtInsertLeagues(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtInsertLeaguesgames(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtInsertTeamsPlayers(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtInsertTeams(java.awt.event.ActionEvent evt) {
        //TODO
    }








    //-------------------------DELETE------------------------------------

    // Método para eliminar jugadores
    public void evtEliminarPlayers(java.awt.event.ActionEvent evt) {

        // Mostrar un cuadro de diálogo para que el usuario ingrese el ID del jugador a eliminar
        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID del jugador que deseas eliminar:",
                "Eliminar jugador",
                JOptionPane.QUESTION_MESSAGE);

        // Si el usuario presiona "Cancelar", input será null y se sale del método
        if (input == null) {
            return;
        }

        // Convertir el input a un número entero, validando que sea un número válido
        int playerId;
        try {
            playerId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // Si no es un número, mostrar mensaje de error y salir
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear instancia de la clase Database para manejar la conexión
        Database db = new Database();

        try (Connection conn = db.getConnection()) { // Abrir conexión a la base de datos

            // Preparar la sentencia SQL DELETE con un parámetro (?)
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM players WHERE player_id = ?"
            );
            stmt.setInt(1, playerId); // Asignar el valor del ID al parámetro

            // Ejecutar la eliminación y obtener el número de filas afectadas
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                // Si se eliminó algún  registro, mostrar mensaje de éxito
                JOptionPane.showMessageDialog(null,
                        "Jugador eliminado correctamente.\n" +
                                "Los registros relacionados también fueron eliminados automáticamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Si no se encontró ningún registro con ese ID, mostrar advertencia
                JOptionPane.showMessageDialog(null,
                        "No existe un jugador con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            // Capturar errores de SQL y mostrarlos en un mensaje
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar jugador:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar rankings
    public void evtEliminarRankings(java.awt.event.ActionEvent evt) {

        // Solicitar al usuario el ID del ranking a eliminar
        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID del ranking que deseas eliminar:",
                "Eliminar ranking",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return; // Si canceló, salir

        int rankingId;
        try {
            rankingId = Integer.parseInt(input); // Convertir a entero
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {

            // Preparar la sentencia DELETE para eliminar el ranking
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM players_game_ranking WHERE player_id = ?"
            );
            stmt.setInt(1, rankingId);

            int rows = stmt.executeUpdate(); // Ejecutar eliminación

            if (rows > 0) {
                JOptionPane.showMessageDialog(null,
                        "Ranking eliminado correctamente.\n" +
                                "Los registros relacionados también fueron eliminados automáticamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe un ranking con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar ranking:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar juegos
    public void evtEliminargames(java.awt.event.ActionEvent evt) {

        // Solicitar ID del juego a eliminar
        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID del juego que deseas eliminar:",
                "Eliminar juego",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        int gameId;
        try {
            gameId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {

            // Preparar DELETE para eliminar el juego por su código
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM games WHERE game_code = ?"
            );
            stmt.setInt(1, gameId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null,
                        "Juego eliminado correctamente.\n" +
                                "Los registros relacionados también fueron eliminados automáticamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe un juego con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar juego:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar ligas
    public void evtEliminarLeagues(java.awt.event.ActionEvent evt) {

        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID de la liga que deseas eliminar:",
                "Eliminar liga",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        int leagueId;
        try {
            leagueId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {

            // DELETE en la tabla leagues usando el ID de liga
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM leagues WHERE league_id = ?"
            );
            stmt.setInt(1, leagueId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null,
                        "Liga eliminada correctamente.\n" +
                                "Los registros relacionados también fueron eliminados automáticamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe una liga con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar liga:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar registros de liga-juego
    public void evtEliminarLeaguesgames(java.awt.event.ActionEvent evt) {

        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID del registro de liga-juego que deseas eliminar:",
                "Eliminar registro liga-juego",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        int lgId;
        try {
            lgId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM leagues_game WHERE league_id = ?"
            );
            stmt.setInt(1, lgId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null,
                        "Registro de liga-juego eliminado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe un registro con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar el registro:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar registros de equipo-jugador
    public void evtEliminarTeamsPlayers(java.awt.event.ActionEvent evt) {

        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID del registro de equipo-jugador que deseas eliminar:",
                "Eliminar registro equipo-jugador",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        int tpId;
        try {
            tpId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM team_players WHERE team_id = ?"
            );
            stmt.setInt(1, tpId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null,
                        "Registro de equipo-jugador eliminado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe un registro con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar el registro:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar equipos
    public void evtEliminarTeams(java.awt.event.ActionEvent evt) {

        String input = JOptionPane.showInputDialog(null,
                "Ingresa el ID del equipo que deseas eliminar:",
                "Eliminar equipo",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        int teamId;
        try {
            teamId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM teams WHERE team_id = ?"
            );
            stmt.setInt(1, teamId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null,
                        "Equipo eliminado correctamente.\n" +
                                "Los registros relacionados también fueron eliminados automáticamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe un equipo con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar el equipo:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
