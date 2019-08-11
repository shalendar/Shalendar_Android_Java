package kr.ac.smu.cs.shalendar_java;

/*
  통신 담당하는 class
  메소드 마다 응답처리한다.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


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


public class IonManager {

    private Context context;
    private NetWorkUrl url;

    public IonManager(Context context) {
        this.url = new NetWorkUrl();
        this.context = context;
    }

    /*
      일정 추가
     */
    public void createSchedule(JsonObject json) {

        Ion.getDefault(this.context).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this.context).getConscryptMiddleware().enable(false);


    }

    /*

      메인 에서 일정 표시
     */
    public void showAllSche() {


        Ion.getDefault(this.context).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this.context).getConscryptMiddleware().enable(false);

        JsonObject json = new JsonObject();
        json.addProperty("cid", 20);

        Ion.with(this.context)
                .load("POST", url.getServerUrl() + "/showAllSche")
                .setHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                       ScheduleData[] scheduleData;

                        if( e!= null) {
                            Toast.makeText(context, "Server Connection Error", Toast.LENGTH_LONG).show();
                        }

                        else {

                            String message = result.get("message").getAsString();
                            if(message.equals("success")) {
                                JsonArray data = result.get("data").getAsJsonArray();
                                scheduleData = new ScheduleData[data.size()];

                                Log.i("들어있는 일정 개수", Integer.toString(data.size()));

                                for(int i = 0; i< data.size(); i++) {
                                    JsonObject sched_data = data.get(i).getAsJsonObject();
                                    scheduleData[i] = new ScheduleData();

                                    scheduleData[i].sche_title = sched_data.get("title").getAsString();
                                    scheduleData[i].cid = sched_data.get("cid").getAsInt();
                                    scheduleData[i].sid = sched_data.get("sid").getAsInt();
                                    scheduleData[i].sche_content = sched_data.get("sContent").getAsString();
                                    scheduleData[i].startDate = sched_data.get("startDate").getAsString();
                                    scheduleData[i].endDate = sched_data.get("endDate").getAsString();
                                    scheduleData[i].area = sched_data.get("area").getAsString();
                                    scheduleData[i].numberofComment = sched_data.get("numOfComments").getAsInt();

//                                    Log.i("title", sche_title);
//                                    Log.i("cid", Integer.toString(cid));
//                                    Log.i("sid", Integer.toString(sid));
//                                    Log.i("sche_content", sche_content);
//                                    Log.i("startDate", startDate);
//                                    Log.i("endDate", endDate);
//                                    Log.i("area", area);
//                                    Log.i("# of comments", Integer.toString(numberofComment));

                                }

                            }

                            else {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
