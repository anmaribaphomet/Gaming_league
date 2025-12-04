package com.example;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JREmptyDataSource;


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


    public TableModel obtenerModeloRefrescado(String command) {
        String sql = switch (command) {
            case "players" -> """
            SELECT 
                player_id AS "ID",
                first_name AS "Nombre",
                last_name AS "Apellido",
                CASE gender
                    WHEN 'M' THEN 'Masculino'
                    WHEN 'F' THEN 'Femenino'
                    ELSE 'Otro'
                END AS "Género",
                address AS "Dirección",
                telephone_number AS "Teléfono",
                email AS "Correo",
                age AS "Edad"
            FROM public.players
            ORDER BY player_id;
        """;

            case "matches" -> """
            SELECT
                m.match_id AS "ID de Partido",
                g.game_name AS "Juego",
                p1.first_name || ' ' || p1.last_name AS "Capitán A",
                p2.first_name || ' ' || p2.last_name AS "Capitán B",
                m.match_date AS "Fecha",
                m.result_match AS "Resultado Encuentro",
                m.result_teams AS "Resultado Equipos"
            FROM public.matches m
            JOIN public.games g ON m.game_code = g.game_code
            JOIN public.players p1 ON m.player_1_id = p1.player_id
            JOIN public.players p2 ON m.player_2_id = p2.player_id
            ORDER BY m.match_id;
            """;

            case "games" -> """
            SELECT
                game_code AS "Código del Juego",
                game_name AS "Nombre del Juego",
                game_description AS "Descripción",
                platform AS "Plataforma",
                category AS "Categoría"
            FROM public.games
            ORDER BY game_code;
            """;

            case "teams" -> """
            SELECT
                team_id AS "ID Equipo",
                team_name AS "Nombre del Equipo",
                date_created AS "Fecha de Creación",
                date_disbanded AS "Fecha de Disolución",
                number_members AS "Número de Miembros",
                users_name AS "Nombre del Usuario",
                wins AS "Victorias",
                ties AS "Empates",
                defeats AS "Derrotas",
                created_by_player_id AS "ID Jugador Creador"
            FROM public.teams
            ORDER BY team_id;
            """;
            case "rankings" -> """
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

            case "leagues" ->
                    """
            SELECT
                league_id       AS "ID Liga",
                league_name     AS "Nombre de la Liga",
                rank            AS "Rango",
                category        AS "Categoría",
                league_duration AS "Duración de la Liga"
            FROM public.leagues
            ORDER BY league_id;
            """;

            case "teams_players" ->
                    """
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
              """
            ;

            case "leagues_games" ->
            """
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

                    // Agrega aquí los demás casos de tu aplicación
                    default -> throw new IllegalArgumentException("No hay SELECT para la tabla: " + command);
                };

                //Ejecutar consulta usando try-with-resources
                try (ResultSet rs = db.query(sql)) {
                    return new JDBCTableAdapter(rs);
                } catch (SQLException e) {
                    throw new RuntimeException("Error al refrescar modelo para " + command, e);
                }
            }


            public TablaSelects buscarTablaAbierta(String command) {
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    if (frame instanceof TablaSelects ts) {
                        if (ts.getCurrentCommand().equals(command)) {
                            return ts;
                        }
                    }
                }
                return null;
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
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("matches");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);


        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }


    //Eliminar por id (matches_id)
    public void evtEliminarMatches(Object id) {

        // 1. Convertir el id recibido a entero
        int matchId;
        try {
            matchId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID debe ser un número entero válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false); // Iniciamos la transacción

            try {
                // Preparar DELETE usando match_id
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM matches WHERE match_id = ?"
                );
                stmt.setInt(1, matchId);

                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    conn.commit(); // Confirmamos la transacción
                    JOptionPane.showMessageDialog(null,
                            "Match eliminado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    conn.rollback(); // Revertimos si no encontró nada
                    JOptionPane.showMessageDialog(null,
                            "No existe un match con ese ID.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException e) {
                conn.rollback(); // Revertimos si hubo error SQL
                JOptionPane.showMessageDialog(null,
                        "Error al eliminar el match:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                conn.setAutoCommit(true); // Restauramos el estado normal
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
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("players");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);
        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }

    public void evtInsertRankings(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertRankings frm = new FrmInsertRankings(null, db );
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("rankings");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }
    public void evtInsertgames(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertGames frm = new FrmInsertGames(null, db );
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("games");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }
    public void evtInsertLeagues(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertLeagues frm = new FrmInsertLeagues(null, db );
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("leagues");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }
    public void evtInsertLeaguesgames(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertLeaguesGames frm = new FrmInsertLeaguesGames(null, db );
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("leagues_games");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }
    public void evtInsertTeamsPlayers(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertTeamsPlayers frm = new FrmInsertTeamsPlayers(null, db );
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("teams_players");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }

    }
    public void evtInsertTeams(java.awt.event.ActionEvent evt) {
        try {
            FrmInsertTeams frm = new FrmInsertTeams(null, db );
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("teams");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);

        } catch (Exception ex) {
            LOGGER.severe("Error al abrir INSERT: " + ex.getMessage());
        }
    }

    //-------------------------DELETE------------------------------------
    public void evtEliminarPlayers(Object id) {
        int playerId;
        try {
            playerId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM players WHERE player_id = ?");
            stmt.setInt(1, playerId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Jugador eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No existe un jugador con ese ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar jugador:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void evtEliminarRankings(Object id) {
        int rankingId;
        try {
            rankingId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            // Asegúrate de que el ID que recibes sea el correcto para borrar
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM players_game_ranking WHERE player_id = ?");
            stmt.setInt(1, rankingId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Ranking eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No existe un ranking asociado a ese ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar ranking:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void evtEliminargames(Object id) {
        int gameId;
        try {
            gameId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            // Usamos game_code según tu código anterior
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM games WHERE game_code = ?");
            stmt.setInt(1, gameId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Juego eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No existe un juego con ese ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar juego:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void evtEliminarLeagues(Object id) {
        int leagueId;
        try {
            leagueId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM leagues WHERE league_id = ?");
            stmt.setInt(1, leagueId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Liga eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No existe una liga con ese ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar liga:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 6. LEAGUES_GAME (Tabla de relación)
    public void evtEliminarLeaguesgames(Object id) {
        int lgId;
        try {
            lgId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            // Verifica si debes borrar por 'league_id' o si la tabla tiene un ID propio
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM leagues_game WHERE league_id = ?");
            stmt.setInt(1, lgId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Registro de liga-juego eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No existe un registro con ese ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void evtEliminarTeamsPlayers(Object id) {
        int tpId;
        try {
            tpId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM team_players WHERE team_id = ?");
            stmt.setInt(1, tpId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Registro de equipo-jugador eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No existe un registro con ese ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void evtEliminarTeams(Object id) {
        int teamId;
        try {
            teamId = Integer.parseInt(id.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = new Database();
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM teams WHERE team_id = ?");
            stmt.setInt(1, teamId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Equipo eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No existe un equipo con ese ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el equipo:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //-------------------------UPDATE------------------------------------
    public void evtUpdateMatches(Object id) {
        FrmUpdateMatches frm =  new FrmUpdateMatches(null, db, id);
        frm.setOnSuccess(() -> {
            TablaSelects browser = buscarTablaAbierta("matches");
            if (browser != null) {
                browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
            }
        });
        frm.setVisible(true);

    }

    public void evtUpdatePlayers(Object id) {
            FrmUpdatePlayers frm = new FrmUpdatePlayers(null, db , id);
            frm.setOnSuccess(() -> {
                TablaSelects browser = buscarTablaAbierta("players");
                if (browser != null) {
                    browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
                }
            });
            frm.setVisible(true);
    }

    public void evtUpdateRankings(Object id) {
        FrmUpdateRankings frm = new FrmUpdateRankings(null, db, id);
        frm.setOnSuccess(() -> {
            // Busca la tabla abierta y recarga
            TablaSelects browser = buscarTablaAbierta("rankings");
            if (browser != null) browser.recargarTabla();
        });
        frm.setVisible(true);
    }

    public void evtUpdateGames(Object id) {
        FrmUpdateGames frm = new FrmUpdateGames(null, db , id);
        frm.setOnSuccess(() -> {
            TablaSelects browser = buscarTablaAbierta("games");
            if (browser != null) {
                browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
            }
        });
        frm.setVisible(true);
    }

    public void evtUpdateLeagues(Object id) {
        FrmUpdateLeagues frm = new FrmUpdateLeagues(null, db , id);
        frm.setOnSuccess(() -> {
            TablaSelects browser = buscarTablaAbierta("leagues");
            if (browser != null) {
                browser.recargarTabla(); // Esto llama a obtenerModeloRefrescado y actualiza JTable
            }
        });
        frm.setVisible(true);
    }

    public void evtUpdateLeaguesGames(Object id) {
        FrmUpdateLeaguesGames frm = new FrmUpdateLeaguesGames(null, db, id);
        frm.setOnSuccess(() -> {
            TablaSelects browser = buscarTablaAbierta("leagues_games");
            if (browser != null) {
                browser.recargarTabla();
            }
        });
        frm.setVisible(true);
    }

    public void evtUpdateTeamsPlayers(Object id) {
        FrmUpdateTeamsPlayers frm = new FrmUpdateTeamsPlayers(null, db, id);
        frm.setOnSuccess(() -> {
            TablaSelects browser = buscarTablaAbierta("teams_players");
            if (browser != null) {
                browser.recargarTabla();
            }
        });
        frm.setVisible(true);
    }

    public void evtUpdateTeams(Object id) {
        FrmUpdateTeams frm = new FrmUpdateTeams(null, db, id);
        frm.setOnSuccess(() -> {
            TablaSelects browser = buscarTablaAbierta("teams");
            if (browser != null) {
                browser.recargarTabla();
            }
        });
        frm.setVisible(true);
    }



    public void evtInformeJugadorEquipo(Object id) {

    }

    public void evtInformeRendimiento(Object id) {
        Database dbReporte = new Database();

        try (Connection conn = dbReporte.getConnection()) {

            // --- ESTILOS VISUALES ---
            StyleBuilder tituloStyle = stl.style()
                    .bold()
                    .setFontSize(14)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setPadding(10);

            StyleBuilder colStyle = stl.style()
                    .setBorder(stl.pen1Point())
                    .setPadding(5);

            // ======================================================================================
            // 1. RESUMEN DE PARTIDOS POR JUEGO
            // ======================================================================================
            String sql1 = """
                SELECT g.game_name, COUNT(m.match_id) as total_partidos
                FROM matches m
                JOIN games g ON m.game_code = g.game_code
                GROUP BY g.game_name
                ORDER BY total_partidos DESC
            """;

            String titulo1 = "1. Resumen de Actividad por Juego";

            // Filtro opcional
            if (id != null) {
                try {
                    int idFiltro = Integer.parseInt(id.toString());
                    sql1 = sql1.replace("GROUP BY", "WHERE m.game_code = " + idFiltro + " GROUP BY");
                    titulo1 = "1. Resumen de Actividad (Juego ID: " + idFiltro + ")";
                } catch(Exception e) {}
            }

            JasperReportBuilder reporte1 = report()
                    .title(cmp.text(titulo1).setStyle(tituloStyle))
                    .columns(
                            col.column("Videojuego", "game_name", type.stringType()).setStyle(colStyle),
                            col.column("Total Partidos", "total_partidos", type.integerType())
                                    .setStyle(colStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)
                    )
                    .setDataSource(sql1, conn);

            // ======================================================================================
            // 2. HISTORIAL DETALLADO DE ENCUENTROS
            // ======================================================================================
            String sql2 = """
                SELECT m.match_id, g.game_name, m.match_date,
                       p1.first_name || ' ' || p1.last_name as capitan_a,
                       p2.first_name || ' ' || p2.last_name as capitan_b,
                       m.result_match, m.result_teams
                FROM matches m
                JOIN games g ON m.game_code = g.game_code
                JOIN players p1 ON m.player_1_id = p1.player_id
                JOIN players p2 ON m.player_2_id = p2.player_id
                ORDER BY m.match_date DESC
            """;

            if (id != null) {
                try {
                    int idFiltro = Integer.parseInt(id.toString());
                    sql2 = sql2.replace("ORDER BY", "WHERE m.game_code = " + idFiltro + " ORDER BY");
                } catch(Exception e) {}
            }

            JasperReportBuilder reporte2 = report()
                    .title(cmp.text("\n\n2. Historial Reciente de Encuentros").setStyle(tituloStyle))
                    .columns(
                            col.column("Fecha", "match_date", type.dateType()).setPattern("yyyy-MM-dd").setStyle(colStyle),
                            col.column("Juego", "game_name", type.stringType()).setStyle(colStyle),
                            col.column("Capitán A", "capitan_a", type.stringType()).setStyle(colStyle),
                            col.column("Capitán B", "capitan_b", type.stringType()).setStyle(colStyle),
                            col.column("Resultado", "result_match", type.stringType()).setStyle(colStyle),
                            col.column("Ganador", "result_teams", type.stringType()).setStyle(colStyle)
                    )
                    .setDataSource(sql2, conn);

            // ======================================================================================
            // 3. JUGADORES MÁS ACTIVOS (Como Capitanes)
            // ======================================================================================
            String sql3 = """
                SELECT p.first_name || ' ' || p.last_name as jugador, COUNT(*) as apariciones
                FROM (
                    SELECT player_1_id as pid FROM matches
                    UNION ALL
                    SELECT player_2_id as pid FROM matches
                ) as combined
                JOIN players p ON combined.pid = p.player_id
                GROUP BY p.player_id, p.first_name, p.last_name
                ORDER BY apariciones DESC
                LIMIT 10
            """;

            JasperReportBuilder reporte3 = report()
                    .title(cmp.text("\n\n3. Top 10 Capitanes Más Activos").setStyle(tituloStyle))
                    .columns(
                            col.column("Jugador", "jugador", type.stringType()).setStyle(colStyle),
                            col.column("Partidos Liderados", "apariciones", type.integerType())
                                    .setStyle(colStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)
                    )
                    .setDataSource(sql3, conn);

            // ======================================================================================
            // 4. ESTADÍSTICAS DE RESULTADOS
            // ======================================================================================
            String sql4 = """
                SELECT result_match, COUNT(*) as cantidad
                FROM matches
                GROUP BY result_match
                ORDER BY cantidad DESC
            """;

            JasperReportBuilder reporte4 = report()
                    .title(cmp.text("\n\n4. Desglose de Resultados").setStyle(tituloStyle))
                    .columns(
                            col.column("Tipo de Resultado", "result_match", type.stringType()).setStyle(colStyle),
                            col.column("Cantidad", "cantidad", type.integerType())
                                    .setStyle(colStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)
                    )
                    .setDataSource(sql4, conn);

            // ======================================================================================
            // REPORTE MAESTRO (Contenedor Final)
            // ======================================================================================

            try {
                StyleBuilder mainTitleStyle = stl.style(tituloStyle).setFontSize(18);

                JasperReportBuilder reporteMaestro = report();

                reporteMaestro
                        .title(cmp.text("Informe de Rendimiento Competitivo")
                                .setStyle(mainTitleStyle))
                        .summary(
                                cmp.subreport(reporte1),      // Reporte 1
                                cmp.verticalGap(20),
                                cmp.pageBreak(),

                                cmp.subreport(reporte2),      // Reporte 2
                                cmp.verticalGap(20),
                                cmp.pageBreak(),

                                cmp.subreport(reporte3),      // Reporte 3
                                cmp.verticalGap(20),

                                cmp.subreport(reporte4)       // Reporte 4
                        )
                        .setDataSource(new net.sf.jasperreports.engine.JREmptyDataSource());

                // Generar y Mostrar
                JasperPrint print = reporteMaestro.toJasperPrint();
                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al unir reportes: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error general: " + e.getMessage());
        }
    }

    public void evtInformeRankingJugadores(Object id) {
        Database dbReporte = new Database();

        try (Connection conn = dbReporte.getConnection()) {

            // ======================================================================================
            // A) ESTILOS VISUALES
            // ======================================================================================
            StyleBuilder tituloStyle = stl.style()
                    .bold()
                    .setFontSize(14)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setPadding(10);

            StyleBuilder colStyle = stl.style()
                    .setBorder(stl.pen1Point())
                    .setPadding(5);

            // ======================================================================================
            // 1. REPORTE DE RANKING (Filtrable por Juego)
            // ======================================================================================
            String sql1 = """
            SELECT g.game_name, p.first_name || ' ' || p.last_name AS jugador, 
                   r.ranking, g.category
            FROM players_game_ranking r
            JOIN players p ON r.player_id = p.player_id
            JOIN games g ON r.game_code = g.game_code
            ORDER BY g.game_name ASC, r.ranking ASC
        """;

            String titulo1 = "1. Ranking Oficial de Jugadores (Global)";

            if (id != null) {
                try {
                    int idFiltro = Integer.parseInt(id.toString());
                    sql1 = sql1.replace("ORDER BY", "WHERE g.game_code = " + idFiltro + " ORDER BY");
                    titulo1 = "1. Ranking Oficial (Filtrado por Juego ID: " + idFiltro + ")";
                } catch(Exception e) {
                    System.out.println("ID inválido para filtro, se omite.");
                }
            }

            TextColumnBuilder<String> colJuego1 = col.column("Juego", "game_name", type.stringType());

            JasperReportBuilder reporte1 = report()
                    .title(cmp.text(titulo1).setStyle(tituloStyle))
                    .columns(
                            col.column("Jugador", "jugador", type.stringType()).setStyle(colStyle),
                            col.column("Posición", "ranking", type.integerType()).setStyle(colStyle).setHorizontalAlignment(HorizontalAlignment.CENTER),
                            col.column("Categoría", "category", type.stringType()).setStyle(colStyle)
                    )
                    .groupBy(colJuego1)
                    .setDataSource(sql1, conn);

            // ======================================================================================
            // 2. TOP 5 JUGADORES POR CATEGORÍA
            // ======================================================================================
            String sql2 = """
            WITH RankedPlayers AS (
                SELECT g.category, p.first_name || ' ' || p.last_name as jugador, r.ranking, g.game_name,
                       ROW_NUMBER() OVER (PARTITION BY g.category ORDER BY r.ranking ASC) as rn
                FROM players_game_ranking r
                JOIN players p ON r.player_id = p.player_id
                JOIN games g ON r.game_code = g.game_code
            )
            SELECT category, jugador, ranking, game_name
            FROM RankedPlayers
            WHERE rn <= 5
            ORDER BY category, ranking
        """;

            TextColumnBuilder<String> colCat2 = col.column("Categoría", "category", type.stringType());

            JasperReportBuilder reporte2 = report()
                    .title(cmp.text("\n\n2. Top 5 Jugadores por Categoría").setStyle(tituloStyle))
                    .columns(
                            col.column("Jugador", "jugador", type.stringType()).setStyle(colStyle),
                            col.column("Ranking", "ranking", type.integerType()).setStyle(colStyle).setHorizontalAlignment(HorizontalAlignment.CENTER),
                            col.column("Juego", "game_name", type.stringType()).setStyle(colStyle)
                    )
                    .groupBy(colCat2)
                    .setDataSource(sql2, conn);

            // ======================================================================================
            // 3. JUGADORES SIN RANKING ASIGNADO
            // ======================================================================================
            String sql3 = """
            SELECT p.player_id, p.first_name || ' ' || p.last_name AS jugador, p.email
            FROM players p
            LEFT JOIN players_game_ranking r ON p.player_id = r.player_id
            WHERE r.ranking IS NULL
            ORDER BY p.player_id
        """;

            JasperReportBuilder reporte3 = report()
                    .title(cmp.text("\n\n3. Jugadores Sin Ranking Asignado").setStyle(tituloStyle))
                    .columns(
                            col.column("ID", "player_id", type.integerType()).setStyle(colStyle).setFixedColumns(3),
                            col.column("Jugador", "jugador", type.stringType()).setStyle(colStyle),
                            col.column("Email", "email", type.stringType()).setStyle(colStyle)
                    )
                    .setDataSource(sql3, conn);

            // ======================================================================================
            // UNIR TODO EN UN REPORTE MAESTRO
            // ======================================================================================

            try {
                StyleBuilder mainTitleStyle = stl.style(tituloStyle).setFontSize(18);

                JasperReportBuilder reporteMaestro = report();

                reporteMaestro
                        // Aquí usamos el nuevo estilo 'mainTitleStyle'
                        .title(cmp.text("Informe Completo de Gestión de Torneos")
                                .setStyle(mainTitleStyle))
                        .summary(
                                cmp.subreport(reporte1),
                                cmp.verticalGap(20),
                                cmp.pageBreak(),

                                cmp.subreport(reporte2),
                                cmp.verticalGap(20),
                                cmp.pageBreak(),

                                cmp.subreport(reporte3),
                                cmp.verticalGap(20),
                                cmp.pageBreak()
                        )
                        .setDataSource(new net.sf.jasperreports.engine.JREmptyDataSource());

                // Generar y Mostrar
                JasperPrint print = reporteMaestro.toJasperPrint();
                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al unir reportes: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error general: " + e.getMessage());
        }
    }

}
