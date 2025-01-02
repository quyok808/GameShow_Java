/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 * 2180603884
 * NGUYEN CONG QUY
 */
public class ScoreBoard {
    private int UserID;
    private String Username;
    private int Score;

    public ScoreBoard(int UserID, String Username, int Score) {
        this.UserID = UserID;
        this.Username = Username;
        this.Score = Score;
    }

    public ScoreBoard() {
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }
    
    
}
