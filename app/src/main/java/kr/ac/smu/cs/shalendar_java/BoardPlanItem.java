package kr.ac.smu.cs.shalendar_java;

public class BoardPlanItem {

    String dateandtime;
    String planname;
    String location;
    String replynum;
    int sid;

    public BoardPlanItem(String dateandtime, String planname, String location, String replynum, int sid) {
        this.dateandtime = dateandtime;
        this.planname = planname;
        this.location = location;
        this.replynum = replynum;
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReplynum() {
        return replynum;
    }

    public void setReplynum(String replynum) {
        this.replynum = replynum;
    }
}
