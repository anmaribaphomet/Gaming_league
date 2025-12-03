package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Main extends JFrame {

    private Eventos event;
    private JDesktopPane desktopPane;
    private JMenuBar menuBar;

    // Menús principales
    private JMenu gestionDeJugadores, gestionDeLigas, gestionDeEquipos;

    // Items
    private JMenuItem SelectPlayers, SelectRankingjugadores;
    private JMenuItem Selectleagues, Selectleaguesgames;
    private JMenuItem TeamsPlayers, SelectTeams, JugadoresEquipos, Rendimiento, JugadorRanking ;
    private JMenu Informes;


    public Main() {
        // Creamos el DesktopPane con fondo
        desktopPane = new DesktopPaneConFondo("/foto.png"); // La imagen debe estar en el mismo paquete o en classpath
        event = new Eventos(desktopPane);//Modificado para inicializar

        initComponents();

        this.setTitle("Gestor de Torneos");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null); // Centrar ventana//Modificado
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);//Modificado
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));//Hace visible la UI
    }

    private void initComponents() {
        menuBar = new JMenuBar();

        // --- MATCHES --- Visualizar
        JMenu TablaMatches = new JMenu("Matches");
        TablaMatches.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                event.evtSelectMatches(new ActionEvent(TablaMatches, ActionEvent.ACTION_PERFORMED, "click"));
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
                event.evtSelectgames(new ActionEvent(Selectgames, ActionEvent.ACTION_PERFORMED, "click"));
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

        Informes = new JMenu("Informes");

        JugadoresEquipos = new JMenuItem("Informe General de Jugadores y Equipos");
        JugadoresEquipos.addActionListener(evt -> event.evtInformeJugadorEquipo(evt));
        Informes.add(JugadoresEquipos);

        Rendimiento = new JMenuItem("Estadisticas de Encuentros y Rendimiento Competitivo");
        Rendimiento.addActionListener(evt -> event.evtInformeRendimiento(evt));
        Informes.add(Rendimiento);

        JugadorRanking = new JMenuItem("Ranking Global de Jugadores por Juego");
        JugadorRanking.addActionListener(evt -> event.evtInformeRankingJugadores(evt));
        Informes.add(JugadorRanking);

        menuBar.add(Informes);

        // --- FINAL: Configuración general ---
        setJMenuBar(menuBar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(desktopPane, BorderLayout.CENTER);

        pack();
    }

    // Clase para DesktopPane con fondo
    private static class DesktopPaneConFondo extends JDesktopPane {
        private Image fondo;

        public DesktopPaneConFondo(String rutaImagen) {
            try {
                fondo = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + rutaImagen);
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fondo != null) {
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
