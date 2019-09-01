package kr.ac.smu.cs.shalendar_java;

public class SearchPlanItem {
    String planname;
    int sid;

    public SearchPlanItem(String planname, int sid) {
        this.planname = planname;
        this.sid = sid;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
