package kr.ac.smu.cs.shalendar_java;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
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

    public static String getDayOfWeek(int DAY_OF_WEEK) {

        String dayOfWeek = null;

        switch (DAY_OF_WEEK) {
            case 1: dayOfWeek = "(일)";
                break;
            case 2: dayOfWeek = "(월)";
                break;
            case 3: dayOfWeek = "(화)";
                break;
            case 4: dayOfWeek = "(수)";
                break;
            case 5: dayOfWeek = "(목)";
                break;
            case 6: dayOfWeek = "(금)";
                break;
            case 7: dayOfWeek = "(토)";
                break;
            default:
        }

        return dayOfWeek;
    }

    public void setProfileImage(ImageView userProfile, String profile_img) {
        if (!(profile_img.equals("DEFAULT :: profile_IMAGE"))) {

            userProfile.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                userProfile.setClipToOutline(true);
            }

            Ion.with(userProfile)
                    .centerCrop()
                    .resize(250, 250)
                    .load(profile_img);
        } else {
            Ion.with(userProfile)
                    .centerCrop()
                    .placeholder(R.drawable.profile_default)
                    .resize(250, 250);
        }
    }

}
