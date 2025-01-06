package Models;

import ApplicationDbContext.DBAccess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 2180609157
 * NGUYEN THI HONG VI
 */
public class QLUser {
    private ArrayList<User> usArrL = new ArrayList<>();

    public QLUser() {
        usArrL = LayDanhSachUser();
    }

    public ArrayList<User> LayDanhSachUser() {
        ArrayList<User> usArr = new ArrayList<>();
        try {
            DBAccess access = new DBAccess();
            ResultSet rs = access.Query("SELECT * FROM users");
            while (rs.next()){
                int id = rs.getInt("id");
                String ten = rs.getString("username");
                String pass = rs.getString("password");
                
                User user = new User(id,ten,pass);
                usArr.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return usArr;
    }
    
    public User TimKiem(String ten){
        usArrL = LayDanhSachUser();
        if (!ten.isEmpty()){
            
            for (User us : usArrL){
                if (us.getUsername().equals(ten)){
                    return us;
                }
            }
        } 
        return null;
    }
    
    public boolean KiemTraDangNhap(String ten, String pass){
        for (User us : usArrL){
            if (us.laPass(pass) && us.laUser(ten)){
                return true;
            }
        }
        return false;
    }
}
