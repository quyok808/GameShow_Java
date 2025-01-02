/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Le Cuong
 */
public class Round {
    private int Id;
    private String RoundName;

    public Round() {
    }

    public Round(int Id, String RoundName) {
        this.Id = Id;
        this.RoundName = RoundName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getRoundName() {
        return RoundName;
    }

    public void setRoundName(String RoundName) {
        this.RoundName = RoundName;
    }
}
