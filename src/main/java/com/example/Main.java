package com.example;

import javax.swing.*;
import java.util.logging.Logger;

public class Main extends JFrame {
    //Inicializo la clase de eventos para usar los metodos
    private Eventos event;
    //Para captar las excepciones del sql
    private static final String CLASS_NAME = Main.class.getSimpleName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    // Componentes UI
    private JDesktopPane desktopPane;//escritorio interno donde se abren las ventanas de las tablas
    private JMenuBar menuBar;//barra superior con opciones

    // Menús principales
    private JMenu matches, menuBrowse, menuInsertar, menuEliminar;

    // Items de Matches
    private JMenu EliminarMatches;
    private JMenuItem Clave, GameCode;
    private JMenuItem TablaMatches, InsertarMatches;

    // Items de Browse (Buscar)
    private JMenu gestionDeJugadores, gestionDeLigas, gestionDeEquipos;
    private JMenuItem SelectPlayers, SelectRankingjugadores, Selectgames;
    private JMenuItem Selectleagues, Selectleaguesgames;
    private JMenuItem TeamsPlayers, SelectTeams;

    //Items de Insertar
    private JMenu IgestionDeJugadores, IgestionDeLigas, IgestionDeEquipos;
    private JMenuItem InsertPlayers, InsertRankingjugadores,Insertgames;
    private JMenuItem Insertleagues, Insertleaguesgames;
    private JMenuItem InsertTeamsPlayers, InsertTeams;

    //Items de Eliminar
    private JMenu EgestionDeJugadores, EgestionDeLigas, EgestionDeEquipos;
    private JMenuItem EliminarPlayers,  EliminarRankingjugadores, Eliminargames;
    private JMenuItem  Eliminarleagues,  Eliminarleaguesgames;
    private JMenuItem  EliminarTeamsPlayers,  EliminarTeams;

    //Constructor que genera la ventana, inicializa a eventos y ejecuta a los botones con sus acciones
    public Main() {
        initComponents();
        event = new Eventos(desktopPane);
        this.setSize(800, 600);
        this.setTitle("Gestor de Torneos");

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));//hace visible la UI
    }

    private void initComponents() {
        menuBar = new JMenuBar();
        desktopPane = new JDesktopPane();

        // --- MATCHES ---
        matches = new JMenu("Matches");

        TablaMatches = new JMenuItem("Ver Tabla");
        TablaMatches.addActionListener(evt -> event.evtSelectMatches(evt));
        matches.add(TablaMatches);

        InsertarMatches = new JMenuItem("Insertar");
        InsertarMatches.addActionListener(evt -> event.evtInsertMatches(evt));
        matches.add(InsertarMatches);

//Eliminar matches menu
        EliminarMatches = new JMenu("Eliminar");

        Clave = new JMenuItem("Por clave");
        Clave.addActionListener(evt -> event.evtEliminarClaveMatches(evt));
        EliminarMatches.add(Clave);

        GameCode = new JMenuItem("Por Juego");
        GameCode.addActionListener(evt -> event.evtEliminarJuegoMatches(evt));
        EliminarMatches.add(GameCode);

        matches.add(EliminarMatches);
        menuBar.add(matches);

        // --- BROWSE (Buscar) ---
        menuBrowse = new JMenu("Buscar");

        // Jugadores
        gestionDeJugadores = new JMenu("Clasificación de jugadores");

        SelectPlayers = new JMenuItem("Jugadores");
        SelectPlayers.addActionListener(evt -> event.evtTablaPlayers(evt));
        gestionDeJugadores.add(SelectPlayers);

        SelectRankingjugadores = new JMenuItem("Rankings");
        SelectRankingjugadores.addActionListener(evt -> event.evtTablaRankings(evt));
        gestionDeJugadores.add(SelectRankingjugadores);

        menuBrowse.add(gestionDeJugadores);

        // Juegos
        Selectgames = new JMenuItem("Juegos");
        Selectgames.addActionListener(evt -> event.evtSelectgames(evt));
        menuBrowse.add(Selectgames);

        // Ligas
        gestionDeLigas = new JMenu("Gestión de Ligas");

        Selectleagues = new JMenuItem("Ligas");
        Selectleagues.addActionListener(evt -> event.evtSelectLeagues(evt));
        gestionDeLigas.add(Selectleagues);

        Selectleaguesgames = new JMenuItem("Ligas de Juegos");
        Selectleaguesgames.addActionListener(evt -> event.evtSelectLeaguesgames(evt));
        gestionDeLigas.add(Selectleaguesgames);

        menuBrowse.add(gestionDeLigas);

        // Equipos
        gestionDeEquipos = new JMenu("Equipos");

        TeamsPlayers = new JMenuItem("Jugadores del Equipo");
        TeamsPlayers.addActionListener(evt -> event.evtSelectTeamsPlayers(evt));
        gestionDeEquipos.add(TeamsPlayers);

        SelectTeams = new JMenuItem("Información del Equipo");
        SelectTeams.addActionListener(evt -> event.evtSelectTeams(evt));
        gestionDeEquipos.add(SelectTeams);

        menuBrowse.add(gestionDeEquipos);

        menuBar.add(menuBrowse);

       // --- Insertar ---
        menuInsertar = new JMenu("Insertar");

        // Jugadores
        IgestionDeJugadores = new JMenu("Clasificación de jugadores");

        InsertPlayers = new JMenuItem("Jugadores");
        InsertPlayers.addActionListener(evt -> event.evtInsertPlayers(evt));
        IgestionDeJugadores.add(InsertPlayers);

        InsertRankingjugadores = new JMenuItem("Rankings");
        InsertRankingjugadores.addActionListener(evt -> event.evtInsertRankings(evt));
        IgestionDeJugadores.add(InsertRankingjugadores);

        menuInsertar.add(IgestionDeJugadores);

        // Juegos
        Insertgames = new JMenuItem("Juegos");
        Insertgames.addActionListener(evt -> event.evtInsertgames(evt));
        menuInsertar.add(Insertgames);

        // Ligas
        IgestionDeLigas = new JMenu("Gestión de Ligas");

        Insertleagues = new JMenuItem("Ligas");
        Insertleagues.addActionListener(evt -> event.evtInsertLeagues(evt));
        IgestionDeLigas.add(Insertleagues);

        Insertleaguesgames = new JMenuItem("Ligas de Juegos");
        Insertleaguesgames.addActionListener(evt -> event.evtInsertLeaguesgames(evt));
        IgestionDeLigas.add(Insertleaguesgames);

        menuInsertar.add(IgestionDeLigas);

        // Equipos
        IgestionDeEquipos = new JMenu("Equipos");

        InsertTeamsPlayers = new JMenuItem("Jugadores del Equipo");
        InsertTeamsPlayers.addActionListener(evt -> event.evtInsertTeamsPlayers(evt));
        IgestionDeEquipos.add(InsertTeamsPlayers);

        InsertTeams = new JMenuItem("Información del Equipo");
        InsertTeams.addActionListener(evt -> event.evtInsertTeams(evt));
        IgestionDeEquipos.add(InsertTeams);

        menuInsertar.add(IgestionDeEquipos);

        menuBar.add(menuInsertar);

        //---ELIMINAR---------
        menuEliminar = new JMenu("Eliminar");

        // Jugadores
        EgestionDeJugadores = new JMenu("Clasificación de jugadores");

        EliminarPlayers = new JMenuItem("Jugadores");
        EliminarPlayers.addActionListener(evt -> event.evtEliminarPlayers(evt));
        EgestionDeJugadores.add(EliminarPlayers);

        EliminarRankingjugadores = new JMenuItem("Rankings");
        EliminarRankingjugadores.addActionListener(evt -> event.evtEliminarRankings(evt));
        EgestionDeJugadores.add(EliminarRankingjugadores);

        menuEliminar.add(EgestionDeJugadores);

        // Juegos
        Eliminargames = new JMenuItem("Juegos");
        Eliminargames.addActionListener(evt -> event.evtEliminargames(evt));
        menuEliminar.add(Eliminargames);

        // Ligas
        EgestionDeLigas = new JMenu("Gestión de Ligas");

        Eliminarleagues = new JMenuItem("Ligas");
        Eliminarleagues.addActionListener(evt -> event.evtEliminarLeagues(evt));
        EgestionDeLigas.add(Eliminarleagues);

        Eliminarleaguesgames = new JMenuItem("Ligas de Juegos");
        Eliminarleaguesgames.addActionListener(evt -> event.evtEliminarLeaguesgames(evt));
        EgestionDeLigas.add(Eliminarleaguesgames);

        menuEliminar.add(EgestionDeLigas);

        // Equipos
        EgestionDeEquipos = new JMenu("Equipos");

        EliminarTeamsPlayers = new JMenuItem("Jugadores del Equipo");
        EliminarTeamsPlayers.addActionListener(evt -> event.evtEliminarTeamsPlayers(evt));
        EgestionDeEquipos.add(EliminarTeamsPlayers);

        EliminarTeams = new JMenuItem("Información del Equipo");
        EliminarTeams.addActionListener(evt -> event.evtEliminarTeams(evt));
        EgestionDeEquipos.add(EliminarTeams);

        menuEliminar.add(EgestionDeEquipos);

        menuBar.add(menuEliminar);
        // --- FINAL: Configuración general ---
        setJMenuBar(menuBar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        pack();
    }
}
