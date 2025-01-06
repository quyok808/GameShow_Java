/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.HashSet;
import java.util.Set;

/**
 * 2180603884
 * NGUYEN CONG QUY
 */
public class ScoreBoard {
    private int id;
    private int UserID;
    private String Username;
    private int Score;
    private boolean isAnswered; // Đã trả lời câu hỏi cuối cùng
    
    public ScoreBoard(int id, int UserID, String Username, int Score) {
        this.id = id;
        this.UserID = UserID;
        this.Username = Username;
        this.Score = Score;
        this.isAnswered = false;
    }
    
    public ScoreBoard(int UserID, String Username, int Score) {
        this.UserID = UserID;
        this.Username = Username;
        this.Score = Score;
        this.isAnswered = false;
    }

    public ScoreBoard() {
        this.isAnswered = false;
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

    public int getId() {
        return id;
    }
    
    // Constructor, getter, setter
    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }
}
