package kr.ac.smu.cs.shalendar_java;

import com.prolificinteractive.materialcalendarview.CalendarDay;

class ScheduleData {

    String sche_title;
    int cid;
    int sid;
    String sche_content;
    String startDate;
    String startTime;
    String endDate;
    String endTime;
    String area;
    int numberofComment;

}

class CalendarScheduleData {

    String sche_title;
    int cid;
    int sid;
    String sche_content;
    CalendarDay startDate;
    String startTime;
    CalendarDay middleDate;
    CalendarDay endDate;
    String endTime;
    String area;
    int numberofComment;
}

public class Global {

    private static int sid = 0;

    public static int getSid() {
        return sid;
    }

    public static void setSid(int sid) {
        Global.sid = sid;
    }
}
