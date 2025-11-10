package com.example;

import java.util.List;

public class Players {

    private final Database db;//referencia de la db

    public Players(Database db) {
        this.db = db;
    }

    // Se debe establecer como lista
    public List<String[]> listarPlayers() {
    //le paso la consulta que necesito
        String sql = """
                SELECT player_id, first_name, last_name, gender, address, telephone_number, email, age
                FROM public.players
                ORDER BY player_id;
                """;
        return db.selectQuery(sql, 8);//y ya lo envio al SELECT del db
    }

  //  public int insertarPlayer(){

   // }

   // public int actualizarPlayer(){

   // }

    // public int eliminarPlayer(){

    // }

}
