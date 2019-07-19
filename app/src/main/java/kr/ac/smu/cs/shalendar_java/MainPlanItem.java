package kr.ac.smu.cs.shalendar_java;

import android.widget.TextView;

public class MainPlanItem {

    String mainPlanname;
    String mainPlantime;

    public MainPlanItem() {
    }

    public MainPlanItem(String mainPlanname, String mainPlantime) {
        this.mainPlanname = mainPlanname;
        this.mainPlantime = mainPlantime;
    }

    public String getMainPlanname() {
        return mainPlanname;
    }

    public void setMainPlanname(String mainPlanname) {
        this.mainPlanname = mainPlanname;
    }

    public String getMainPlantime() {
        return mainPlantime;
    }

    public void setMainPlantime(String mainPlantime) {
        this.mainPlantime = mainPlantime;
    }
}
