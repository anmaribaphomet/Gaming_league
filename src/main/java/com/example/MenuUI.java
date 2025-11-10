package com.example;

import javax.swing.*;//el frame (ventana)
import java.awt.*;//componentes para la ventana

public class MenuUI extends JFrame {//extiende de la clase frame para poder usarla

    private Database db;//almacena la referencia que le pasa el main

    public MenuUI(Database db){
        this.db = db;

        setTitle("Gaming League - Menu");//titulo que se muestra arriba
        setSize(400, 400);//tamaño de ña ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//le da funcion al boton de salir (la X)
        setLocationRelativeTo(null);//centra la ventana en la pantalla

        setLayout(new GridLayout(8, 1)); // tabla con la opcion de las 8 tablas = 8 botones que llaman a 8 clases

        // botones para cada tabla los coloque en el orden en que aparecen el el posgress (los establece)
        JButton btnGames = new JButton("games");
        JButton btnLeagues = new JButton("leagues");
        JButton btnLeaguesGame = new JButton("leagues_Game");
        JButton btnMatches = new JButton("matches");
        JButton btnPlayers = new JButton("players");
        JButton btnPlayersGameRanking = new JButton("players_Game_Ranking");
        JButton btnTeamPlayers = new JButton("team_Players");
        JButton btnTeams = new JButton("teams");

        // agregar botones al frame (ya los agrega a la ventana)
        add(btnGames);
        add(btnLeagues);
        add(btnLeaguesGame);
        add(btnMatches);
        add(btnPlayers);
        add(btnPlayersGameRanking);
        add(btnTeamPlayers);
        add(btnTeams);

        // Esto son los eventos en java
        btnPlayers.addActionListener(e -> {//llama a la interfaz de players
            PlayersUI ui = new PlayersUI(db);//crea la referencia
            ui.setVisible(true);//la hace visible
        });



        // por ahora solo Players activa algo, ya seria cuestion de ir agregando los eventos de los demas
    }
}
