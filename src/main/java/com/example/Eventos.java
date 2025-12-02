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

    // 🔹 Constructor
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
    SELECT
        m.match_id AS "match_id:ID de Partido",
        g.game_name AS "game_name:Juego",
        p1.first_name || ' ' || p1.last_name AS "player_1_id:Capitan A",
        p2.first_name || ' ' || p2.last_name AS "player_2_id:Capitan B",
        m.match_date AS "match_date:Fecha",
        m.result_match AS "result_match:Resultado Encuentro",
        m.result_teams AS "result_teams:Resultado Equipos"
    FROM public.matches m
    JOIN public.games g ON m.game_code = g.game_code
    JOIN public.players p1 ON m.player_1_id = p1.player_id
    JOIN public.players p2 ON m.player_2_id = p2.player_id
    ORDER BY m.match_id;
""";


            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Listener para insertar/eliminar

            //Crear la vista
            TablaSelects browser = new TablaSelects("Emparejamiento", modelo, "matches", this);

            // agregar al desktopPane
            this.desktopPane.add(browser);
            browser.setVisible(true);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }

    //Insertar con transacciones
    public void evtInsertMatches(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertMatches frm = new FrmInsertMatches(null, db);
            frm.setVisible(true);

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }


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



    //-------------------------SELECT (BUSCAR)  eventos

    //Evento del Select de players
    public void evtTablaPlayers(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
    SELECT
        player_id AS "player_id:ID",
        first_name AS "first_name:Nombre",
        last_name AS "last_name:Apellido",
        
        CASE gender
            WHEN 'M' THEN 'Masculino'
            WHEN 'F' THEN 'Femenino'
            ELSE 'Otro'
        END AS "gender:Género",

        address AS "address:Dirección",
        telephone_number AS "telephone_number:Teléfono",
        email AS "email:Correo",
        age AS "age:Edad"
    FROM public.players
    ORDER BY player_id;
    """;




            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria


            TablaSelects browser = new TablaSelects("Jugadores", modelo, "players", this);
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
        pgr.player_id            AS "ID Jugador",
        pl.first_name || ' ' || pl.last_name AS "Nombre del Jugador",
        g.game_name              AS "Nombre del Juego",
        pgr.ranking              AS "Ranking"
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


            TablaSelects browser = new TablaSelects("Rankings", modelo, "rankings", this);

            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectgames(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
    SELECT
        game_code        AS "Código del Juego",
        game_name        AS "Nombre del Juego",
        game_description AS "Descripción",
        platform         AS "Plataforma",
        category         AS "Categoría"
    FROM public.games
    ORDER BY game_code;
    """;


            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria


            TablaSelects browser = new TablaSelects("Juegos", modelo, "games", this);

            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectLeagues(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
    SELECT
        league_id       AS "ID Liga",
        league_name     AS "Nombre de la Liga",
        rank            AS "Rango",
        category        AS "Categoría",
        league_duration AS "Duración de la Liga"
    FROM public.leagues
    ORDER BY league_id;
    """;


            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria


            TablaSelects browser = new TablaSelects("Ligas", modelo, "leagues", this);

            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectLeaguesgames(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
    SELECT
        lg.league_id   AS "ID Liga",
        l.league_name  AS "Nombre de la Liga",
        lg.game_code   AS "Código del Juego",
        g.game_name    AS "Nombre del Juego"
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


            TablaSelects browser = new TablaSelects("Ligas de Juegos", modelo, "leagues_games", this);

            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectTeams(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
    SELECT
        team_id              AS "ID Equipo",
        team_name            AS "Nombre del Equipo",
        date_created         AS "Fecha de Creación",
        date_disbanded       AS "Fecha de Disolución",
        number_members       AS "Número de Miembros",
        users_name           AS "Nombre del Usuario",
        wins                 AS "Victorias",
        ties                 AS "Empates",
        defeats              AS "Derrotas",
        created_by_player_id AS "ID Jugador Creador"
    FROM public.teams
    ORDER BY team_id;
    """;


            ResultSet rs = db.query(sql);
            JDBCTableAdapter modelo = new JDBCTableAdapter(rs);

            // Usa el listener genérico, indicando la tabla y su clave primaria


            TablaSelects browser = new TablaSelects("Equipos", modelo, "teams", this);


            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }
    public void evtSelectTeamsPlayers(java.awt.event.ActionEvent evt) {
        try {
            String sql = """
    SELECT
        tp.team_id                                   AS "ID Equipo",
        t.team_name                                  AS "Nombre del Equipo",
        tp.player_id                                 AS "ID Jugador",
        pl.first_name || ' ' || pl.last_name         AS "Nombre del Jugador",
        tp.date_from                                 AS "Fecha de Ingreso",
        tp.date_to                                   AS "Fecha de Salida"
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


            TablaSelects browser = new TablaSelects("Jugadores del Equipo", modelo, "teams_players", this);

            browser.setVisible(true);
            this.desktopPane.add(browser);

        } catch (SQLException ex) {
            LOGGER.severe("Error en SELECT: " + ex.getMessage());
        }
    }

    //-------------------------INSERT eventos
    public void evtInsertPlayers(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertPlayers frm = new FrmInsertPlayers(null, db );

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }

    public void evtInsertRankings(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertRankings frm = new FrmInsertRankings(null, db );

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }
    public void evtInsertgames(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertGames frm = new FrmInsertGames(null, db );

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }
    public void evtInsertLeagues(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertLeagues frm = new FrmInsertLeagues(null, db );

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }
    public void evtInsertLeaguesgames(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertLeaguesGames frm = new FrmInsertLeaguesGames(null, db );

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }
    public void evtInsertTeamsPlayers(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertTeamsPlayers frm = new FrmInsertTeamsPlayers(null, db );

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }
    public void evtInsertTeams(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertTeams frm = new FrmInsertTeams(null, db );

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }

    //-------------------------DELETE------------------------------------

    // Métoo para eliminar jugadores
    public void evtEliminarPlayers(Object id) {

        // Convertir el id a entero
        int playerId;
        try {
            playerId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM players WHERE player_id = ?"
            );
            stmt.setInt(1, playerId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null,
                        "Jugador eliminado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No existe un jugador con ese ID.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar jugador:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // Métdo para eliminar rankings
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

    // Métdo para eliminar juegos
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

    // Métdo para eliminar ligas
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

    // Métdo para eliminar registros de liga-juego
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

    // Métdo para eliminar registros de equipo-jugador
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

    // Métoo para eliminar equipos
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
    public void evtUpdateMatches(Object id) {
        new FrmUpdateMatches(null, db, id);
    }

    public void evtUpdatePlayers(Object id) {
       new FrmUpdatePlayers(null, db, id);
    }

    public void evtUpdateRankings(Object id) {
       new FrmUpdateRankings(null, db, id);
    }

    public void evtUpdateGames(Object id) {
        new FrmUpdateGames(null, db, id);
    }

    public void evtUpdateLeagues(Object id) {
       new FrmUpdateLeagues(null, db, id);
    }

    public void evtUpdateLeaguesGames(Object id) {
      //  new FrmUpdateLeaguesGames(null, db, id);
    }

    public void evtUpdateTeams(Object id) {
        //new FrmUpdateTeams(null, db, id);
    }

    public void evtUpdateTeamsPlayers(Object id) {
       // new FrmUpdateTeamsPlayers(null, db, id);
    }

}
