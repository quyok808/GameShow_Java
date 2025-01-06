package Models;

/**
 * 2180603884
 * NGUYEN CONG QUY
 */
public class CauHoi {
    private String LinkVideo;
    private String CauHoi;
    private String DapAn;
    private String DapAnDung;

    public CauHoi(String LinkVideo, String CauHoi, String DapAn, String DapAnDung) {
        this.LinkVideo = LinkVideo;
        this.CauHoi = CauHoi;
        this.DapAn = DapAn;
        this.DapAnDung = DapAnDung;
    }

    public CauHoi() {
    }

    public String getLinkVideo() {
        return LinkVideo;
    } 

    public String getCauHoi() {
        return CauHoi;
    }

    public String getDapAn() {
        return DapAn;
    }

    public String getDapAnDung() {
        return DapAnDung;
    }
}
