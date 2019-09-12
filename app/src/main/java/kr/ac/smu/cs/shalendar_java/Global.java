package kr.ac.smu.cs.shalendar_java;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
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
            userProfile.setImageResource(R.drawable.profile_default);
        }
    }

    public void netWork_ShowSche(final Context context, final ProgressDialog progressDialog, JsonObject json, NetWorkUrl url) {

        Future ion = Ion.with(context)
                .load("POST", url.getServerUrl() + "/showSche")
                .setHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject() //응답
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //응답 받을 변수
                        String userProfile, userName, schedTitle, aboutSched, schedLocation;
                        String startDate, startTime, endDate, endTime, startToEnd;

                        if (e != null) {
                            Toast.makeText(context, "Server Connection Error!", Toast.LENGTH_LONG).show();
                        } else {
                            //응답 형식이 { "data":{"id":"jacob456@hanmail.net", "cid":1, "sid":10, "title":"korea"}, "message":"success"}
                            //data: 다음에 나오는 것들도 JsonObject형식.
                            //따라서 data를 JsonObject로 받고, 다시 이 data를 이용하여(어찌보면 JsonObject안에 또다른 JsonObject가 있는 것이다.
                            //JSONArray가 아님. 얘는 [,]로 묶여 있어야 함.

                            String message = result.get("message").getAsString();
                            //서버로 부터 응답 메세지가 success이면...

                            if (message.equals("success")) {
                                //서버 응답 오면 로딩 창 해제
                                progressDialog.dismiss();

                                //data: {} 에서 {}안에 있는 것들도 JsonObject
                                JsonObject data = result.get("data").getAsJsonObject();

                                userName = data.get("userName").getAsString();
                                schedTitle = data.get("title").getAsString();
                                aboutSched = data.get("sContent").getAsString();
                                schedLocation = data.get("area").getAsString();
                                startDate = data.get("startDate").getAsString();
                                //startTime = data.get("startTime").getAsString();
                                endDate = data.get("endDate").getAsString();
                                //endTime = data.get("endTime").getAsString();

                                //뒤에 0.000 잘라내기
                                startDate = startDate.substring(0, 16);
                                endDate = endDate.substring(0, 16);
                                startToEnd = startDate + " ~ " + endDate;

                                if (data.get("img_url").isJsonNull())
                                    userProfile = "DEFAULT :: profile_IMAGE";
                                else
                                    userProfile = data.get("img_url").getAsString();

                                Intent intent = new Intent(context, PlanDetailActivity.class);
                                intent.putExtra("userProfile", userProfile);
                                intent.putExtra("userName", userName);
                                intent.putExtra("schedTitle", schedTitle);
                                intent.putExtra("aboutSched", aboutSched);
                                intent.putExtra("area", schedLocation);
                                intent.putExtra("startToEnd", startToEnd);

                                context.startActivity(intent);

                                Log.i("result", data.get("id").getAsString());
                            } else {

                                Toast.makeText(context, "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

        try {
            ion.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
