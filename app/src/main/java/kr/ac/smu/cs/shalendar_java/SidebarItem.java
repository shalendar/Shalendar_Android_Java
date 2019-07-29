package kr.ac.smu.cs.shalendar_java;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SidebarItem {

    int calendarImage;
    String calendarName;
    //cid추가
    int calendar_ID;
    ArrayList<SidebarTeamItem> teamImageList;

    public SidebarItem() {
    }

    public int getCalendarImage() {
        return calendarImage;
    }

    public void setCalendarImage(int calendarImage) {
        this.calendarImage = calendarImage;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public void setCalendar_ID(int cid) { this.calendar_ID = cid; }

    public int getCalendar_ID() { return this.calendar_ID; }

    public ArrayList<SidebarTeamItem> getTeamImageList() {
        return teamImageList;
    }

    public void setTeamImageList(ArrayList<SidebarTeamItem> teamImageList) {
        this.teamImageList = teamImageList;
    }
}
