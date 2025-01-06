/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;

/**
 * 2180609157
 * NGUYEN THI HONG VI
 */
public class User implements Serializable
{
    private int id;
    private String ten;
    private String matKhau;
   
    public User(int id, String ten, String matKhau) {
        this.id = id;
        this.ten = ten;
        this.matKhau = matKhau;
    }

    public User() {
    }
    
    public boolean laUser(String ten){
        return ten.equals(this.ten);
    }
    
    public boolean laPass(String matKhau){
        return matKhau.equals(this.matKhau);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return ten;
    }
}
