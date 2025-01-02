package Models;

import ApplicationDbContext.DBAccess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 2180603884 NGUYEN CONG QUY
 */
public class ListCauHoi {

    private ArrayList<CauHoi> _listCauHoi = new ArrayList<>();

    public ListCauHoi() {
        _listCauHoi = getAllCauHoi();
    }

    public ArrayList<CauHoi> getAllCauHoi() {
        ArrayList<CauHoi> listCauHoi = new ArrayList<>();
        try {
            DBAccess access = new DBAccess();
            ResultSet rs = access.Query("SELECT * FROM question");
            while (rs.next()) {
                String question = rs.getString("question");
                String answers = rs.getString("answers");
                String correctAnswer = rs.getString("correctanswer");
                String linkvideo = rs.getString("linkvideo");
                String roundid = rs.getString("roundid");

                CauHoi cauHoi = new CauHoi(linkvideo, question, answers, correctAnswer, roundid);
                listCauHoi.add(cauHoi);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListCauHoi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listCauHoi;
    }
    
    public ArrayList<CauHoi> getQuestion(String RoundName){
        ArrayList<CauHoi> listCauHoi = new ArrayList<>();
        Round round = ListRound.FindByName(RoundName);
        try {
            DBAccess access = new DBAccess();
            ResultSet rs = access.Query("SELECT * FROM question WHERE roundid = '"+round.getId()+"'");
            while (rs.next()) {
                String question = rs.getString("question");
                String answers = rs.getString("answers");
                String correctAnswer = rs.getString("correctanswer");
                String linkvideo = rs.getString("linkvideo");
                String roundid = rs.getString("roundid");

                CauHoi cauHoi = new CauHoi(linkvideo, question, answers, correctAnswer, roundid);
                listCauHoi.add(cauHoi);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListCauHoi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listCauHoi;
    }
}
