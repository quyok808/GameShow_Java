/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import ApplicationDbContext.DBAccess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Le Cuong
 */
public class ListRound {
    private static ArrayList<Round> rounds = new ArrayList<>();

    public ListRound() {
        rounds = getAll();
    }

    private ArrayList<Round> getAll() {
        ArrayList<Round> listRound = new ArrayList<>();
        try {
            DBAccess access = new DBAccess();
            ResultSet rs = access.Query("SELECT * FROM round");
            while (rs.next()) {
                int id = rs.getInt("id");
                String roundname = rs.getString("roundname");
                
                Round round = new Round(id, roundname);
                listRound.add(round);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListCauHoi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listRound;
    }   
    
    public static Round FindByName(String RoundName){
        for (Round item : rounds){
            if (item.getRoundName().equals(RoundName)){
                return item;
            }
        }
        return null;
    }
}
