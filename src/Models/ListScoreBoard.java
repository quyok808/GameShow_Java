/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import ApplicationDbContext.DBAccess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * 2180603884 NGUYEN CONG QUY
 */
public class ListScoreBoard {

    private List<ScoreBoard> _scoreBoards;
    DBAccess _context = new DBAccess();

    public ListScoreBoard() {
        this._scoreBoards = LayDanhSachDiem();

    }

    private ArrayList<ScoreBoard> LayDanhSachDiem() {
        ArrayList<ScoreBoard> scArr = new ArrayList<>();
        try {
            DBAccess access = new DBAccess();
            ResultSet rs = access.Query("SELECT * FROM scoreboard ORDER BY score DESC");
            while (rs.next()) {
                int id = rs.getInt("id");
                int userID = rs.getInt("userID");
                String username = rs.getString("username");
                int score = rs.getInt("score");

                ScoreBoard scoreboard = new ScoreBoard(id, userID, username, score);
                scArr.add(scoreboard);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return scArr;
    }

    public List<ScoreBoard> getScoreBoards() {
        return _scoreBoards;
    }

    public void setScoreBoards(List<ScoreBoard> _scoreBoards) {
        this._scoreBoards = _scoreBoards;
    }

    public void addNewPlayer(ScoreBoard newPlayer) {
        newPlayer.setScore(0);
        int kq = 0;
        kq = _context.InsertAndUpdate("INSERT INTO scoreboard VALUES('" + newPlayer.getId() + "','" + newPlayer.getUserID() + "','" + newPlayer.getUsername() + "'," + newPlayer.getScore() + ")");

        if (kq != 1) {
            JOptionPane.showMessageDialog(null, "Có lỗi khi lưu điểm vào database !!!");
        }
        _scoreBoards.add(newPlayer);
    }

    public int getScore(String username) {
        for (ScoreBoard temp : _scoreBoards) {
            if (temp.getUsername().equals(username)) {
                return temp.getScore();
            }
        }
        return -1;
    }

    public void saveDatabase() {

        int kq = 0;

        for (ScoreBoard item : _scoreBoards) {
            kq = _context.InsertAndUpdate("INSERT INTO scoreboard VALUES('" + LocalDateTime.now().getNano() + "','" + item.getUserID() + "','" + item.getUsername() + "'," + item.getScore() + ")");

            if (kq != 1) {
                JOptionPane.showMessageDialog(null, "Có lỗi khi lưu điểm vào database !!!");
            }
        }
    }

    public void updateDB(int id, int currentScore) {
        int kq = 0;
        kq = _context.InsertAndUpdate("UPDATE scoreboard SET score=" + currentScore + " WHERE id = " + id);
        if (kq != 1) {
            JOptionPane.showMessageDialog(null, "Có lỗi khi lưu điểm vào database !!!");
        }
    }

    public void updateScore(int id, int currentScore) {
        for (ScoreBoard item : _scoreBoards) {
            if (item.getId() == id) {
                item.setScore(currentScore);
                updateDB(id, currentScore);
                break;
            }
        }
    }

    public String getTop4() {
        List<ScoreBoard> scArr = LayDanhSachDiem();
        Set<String> uniqueUsernames = new HashSet<>();
        StringBuilder top4 = new StringBuilder();

        for (ScoreBoard score : scArr) {
            // Nếu username chưa có trong Set, thêm nó vào
            if (uniqueUsernames.add(score.getUsername())) {
                top4.append(score.getUsername()).append("@");
            }

            // Dừng vòng lặp khi đủ 4 phần tử
            if (uniqueUsernames.size() == 4) {
                break;
            }
        }

        // Xóa ký tự "@" cuối cùng nếu tồn tại
        if (top4.length() > 0) {
            top4.deleteCharAt(top4.length() - 1);
        }

        return top4.toString();
    }
    
    public String getDiemTop4() {
        List<ScoreBoard> scArr = LayDanhSachDiem();
        Set<String> uniqueUsernames = new HashSet<>();
        StringBuilder top4 = new StringBuilder();

        for (ScoreBoard score : scArr) {
            // Nếu username chưa có trong Set, thêm nó vào
            if (uniqueUsernames.add(score.getUsername())) {
                top4.append(score.getScore()).append("@");
            }

            // Dừng vòng lặp khi đủ 4 phần tử
            if (uniqueUsernames.size() == 4) {
                break;
            }
        }

        // Xóa ký tự "@" cuối cùng nếu tồn tại
        if (top4.length() > 0) {
            top4.deleteCharAt(top4.length() - 1);
        }

        return top4.toString();
    }
}
