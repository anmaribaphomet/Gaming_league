package com.example;

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
        //TODO
    }
//eliminar por codigo de juego (game_code)
    public void evtEliminarJuegoMatches(java.awt.event.ActionEvent evt) {
        //TODO
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








    //-------------------------DELETE
    public void evtEliminarPlayers(java.awt.event.ActionEvent evt) {
         //TODO
    }

    public void evtEliminarRankings(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtEliminargames(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtEliminarLeagues(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtEliminarLeaguesgames(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtEliminarTeamsPlayers(java.awt.event.ActionEvent evt) {
        //TODO
    }
    public void evtEliminarTeams(java.awt.event.ActionEvent evt) {
        //TODO
    }
}
