/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApplicationDbContext;

import java.sql.*;
import javax.swing.*;

/**
 * 2180603884 - 2180609157
 * NGUYEN CONG QUY - NGUYEN THI HONG VI
 */
public class MyConnection {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/doanmmt";
    static final String USER = "root";
    static final String PASS = ""; 
    
    public Connection getConnection(){
 
        try{
           Class.forName(JDBC_DRIVER);
           Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
           return conn;
        }    
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.toString(), "Loi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
   }
    
}