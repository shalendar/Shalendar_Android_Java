package kr.ac.smu.cs.shalendar_java;

import android.widget.TextView;

import java.util.ArrayList;

public class MainPlanItem {

    String mainPlanname;
    String mainPlantime;
    String mainPlanDday;
    int sid;

    private ArrayList<MainPlanTeamIteam> teamPicList;

    public MainPlanItem() {
    }

    public MainPlanItem(String mainPlanname, String mainPlantime, ArrayList<MainPlanTeamIteam> teamPicList) {
        this.mainPlanname = mainPlanname;
        this.mainPlantime = mainPlantime;
        this.teamPicList = teamPicList;
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

    public String getMainPlanDday() {
        return this.mainPlanDday;
    }

    public void setMainPlanDday(String mainPlanDday) {
        this.mainPlanDday = mainPlanDday;
    }

    public int getSid() {
        return this.sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public ArrayList<MainPlanTeamIteam> getTeamPicList() {
        return teamPicList;
    }

    public void setTeamPicList(ArrayList<MainPlanTeamIteam> teamPicList) {
        this.teamPicList = teamPicList;
    }
}
