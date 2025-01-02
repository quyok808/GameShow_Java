/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApplicationDbContext;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 2180603884 - 2180609157
 * NGUYEN CONG QUY - NGUYEN THI HONG VI
 */
public class DBAccess {
    private Connection con;
    private Statement stmt;

    public DBAccess() {
        try {
            MyConnection mycon = new MyConnection();
            con = mycon.getConnection();
            stmt = con.createStatement();
        } catch (Exception ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int InsertAndUpdate (String str){
        try {
            int i = stmt.executeUpdate(str);
            return i;
        } catch (SQLException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    public ResultSet Query(String srt){
        try {
            ResultSet rs = stmt.executeQuery(srt);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }  
    
    public int RegisterUser (String str){
        try {
            int i = stmt.executeUpdate(str);
            return i;
        } catch (SQLException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
