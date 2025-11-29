package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Main extends JFrame {
    //Inicializo la clase de eventos para usar los metodos
    private Eventos event;
    // Componentes UI
    private JDesktopPane desktopPane;//escritorio interno donde se abren las ventanas de las tablas
    private JMenuBar menuBar;//barra superior con opciones

    // Menús principales
    private JMenu  gestionDeJugadores, gestionDeLigas, gestionDeEquipos;

    // Items
    private JMenuItem SelectPlayers, SelectRankingjugadores;
    private JMenuItem Selectleagues, Selectleaguesgames;
    private JMenuItem TeamsPlayers, SelectTeams;


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

        // --- MATCHES --- Visualizar
        JMenu TablaMatches = new JMenu("Matches");
        TablaMatches.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionEvent fake = new ActionEvent(TablaMatches, ActionEvent.ACTION_PERFORMED, "click");
                event.evtSelectMatches(fake);
            }
        });
        menuBar.add(TablaMatches);

        // ---------------------------- Jugadores
        gestionDeJugadores = new JMenu("Clasificación de jugadores");

        SelectPlayers = new JMenuItem("Jugadores");
        SelectPlayers.addActionListener(evt -> event.evtTablaPlayers(evt));
        gestionDeJugadores.add(SelectPlayers);

        SelectRankingjugadores = new JMenuItem("Rankings");
        SelectRankingjugadores.addActionListener(evt -> event.evtTablaRankings(evt));
        gestionDeJugadores.add(SelectRankingjugadores);

        menuBar.add(gestionDeJugadores);

        //-------------------------------- Juegos
        JMenu Selectgames = new JMenu("Juegos");

        Selectgames.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionEvent fake = new ActionEvent(Selectgames, ActionEvent.ACTION_PERFORMED, "click");
                event.evtSelectgames(fake);
            }
        });
        menuBar.add(Selectgames);

        //------------------------- Ligas
        gestionDeLigas = new JMenu("Gestión de Ligas");

        Selectleagues = new JMenuItem("Ligas");
        Selectleagues.addActionListener(evt -> event.evtSelectLeagues(evt));
        gestionDeLigas.add(Selectleagues);

        Selectleaguesgames = new JMenuItem("Ligas de Juegos");
        Selectleaguesgames.addActionListener(evt -> event.evtSelectLeaguesgames(evt));
        gestionDeLigas.add(Selectleaguesgames);

        menuBar.add(gestionDeLigas);

        //----------------- Equipos
        gestionDeEquipos = new JMenu("Equipos");

        TeamsPlayers = new JMenuItem("Jugadores del Equipo");
        TeamsPlayers.addActionListener(evt -> event.evtSelectTeamsPlayers(evt));
        gestionDeEquipos.add(TeamsPlayers);

        SelectTeams = new JMenuItem("Información del Equipo");
        SelectTeams.addActionListener(evt -> event.evtSelectTeams(evt));
        gestionDeEquipos.add(SelectTeams);

        menuBar.add(gestionDeEquipos);


        // --- FINAL: Configuración general ---
        setJMenuBar(menuBar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        pack();
    }
}
