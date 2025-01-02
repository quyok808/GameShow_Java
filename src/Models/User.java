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
    private String ten;
    private String matKhau;
   
    public User(String ten, String matKhau) {
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
}
