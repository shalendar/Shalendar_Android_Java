package kr.ac.smu.cs.shalendar_java;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class MainPlanAdapter extends RecyclerView.Adapter<MainPlanAdapter.ItemRowHolder> {

    private Context mContext;
    private ArrayList<MainPlanItem> mainPlanList;

    public MainPlanAdapter(ArrayList<MainPlanItem> mainPlanList, Context mContext) {
        this.mainPlanList = mainPlanList;
        this.mContext = mContext;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycleritem, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(MainPlanAdapter.ItemRowHolder holder, int position) {

        String planName = mainPlanList.get(position).getMainPlanname();
        String planTime = mainPlanList.get(position).getMainPlantime();
        String planDday = mainPlanList.get(position).getMainPlanDday();
        ArrayList picList = mainPlanList.get(position).getTeamPicList();

        holder.main_planname.setText(planName);
        holder.main_plantime.setText(planTime);
        holder.main_planDday.setText(planDday);


        if(position % 3 == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ff6067"));
        }

        else if(position % 3 == 1) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#f8c930"));
        }

        else if(position % 3 == 2) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#cdcdcd"));
        }


        MainPlanTeamAdapter mt_adapter = new MainPlanTeamAdapter(picList, mContext);

        holder.mainteam_recyclerView.setHasFixedSize(true);
        holder.mainteam_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.mainteam_recyclerView.setAdapter(mt_adapter);

    }

    @Override
    public int getItemCount() {
        return (null != mainPlanList ? mainPlanList.size() : 0);
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder {


        protected TextView main_planname;
        protected TextView main_plantime;
        protected TextView main_planDday;
        protected LinearLayout linearLayout;
        protected RecyclerView mainteam_recyclerView;
        protected NetWorkUrl url;


        public ItemRowHolder(final View itemView) {
            super(itemView);

            this.url = new NetWorkUrl();
            this.mainteam_recyclerView = itemView.findViewById(R.id.mainteam_recyclerView);
            this.main_planname = itemView.findViewById(R.id.main_planname);
            this.main_plantime = itemView.findViewById(R.id.main_plantime);
            this.main_planDday = itemView.findViewById(R.id.main_planDday);
            this.linearLayout = itemView.findViewById(R.id.linearLayout_color);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), main_planname.getText(), Toast.LENGTH_SHORT).show();


//                    sid오류
                    Global.setSid(mainPlanList.get(getAdapterPosition()).getSid());
                    Log.d("어댑터sid","sid는"+Global.getSid());

                    JsonObject json = new JsonObject();

                    json.addProperty("sid", Global.getSid());

                    final ProgressDialog progressDialog = new ProgressDialog(itemView.getContext());
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("해당 댓글로 이동중~" + mainPlanList.get(getAdapterPosition()).getSid());
                    progressDialog.show();


                    Global global = new Global();
                    global.netWork_ShowSche(itemView.getContext(), progressDialog, json, url);

//                    Ion.with(itemView.getContext())
//                            .load("POST", url.getServerUrl() + "/showSche")
//                            .setHeader("Content-Type", "application/json")
//                            .setJsonObjectBody(json)
//                            .asJsonObject() //응답
//                            .setCallback(new FutureCallback<JsonObject>() {
//                                @Override
//                                public void onCompleted(Exception e, JsonObject result) {
//                                    //응답 받을 변수
//                                    String userProfile, userName, schedTitle, aboutSched, schedLocation;
//                                    String startDate, startTime, endDate, endTime, startToEnd;
//
//                                    if (e != null) {
//                                        Toast.makeText(itemView.getContext(), "Server Connection Error!", Toast.LENGTH_LONG).show();
//                                    } else {
//
//                                        String message = result.get("message").getAsString();
//                                        //서버로 부터 응답 메세지가 success이면...
//
//                                        if (message.equals("success")) {
//                                            //서버 응답 오면 로딩 창 해제
//                                            progressDialog.dismiss();
//
//                                            //data: {} 에서 {}안에 있는 것들도 JsonObject
//                                            JsonObject data = result.get("data").getAsJsonObject();
//
//                                            userName = data.get("userName").getAsString();
//                                            schedTitle = data.get("title").getAsString();
//                                            aboutSched = data.get("sContent").getAsString();
//                                            schedLocation = data.get("area").getAsString();
//                                            startDate = data.get("startDate").getAsString();
//                                            //startTime = data.get("startTime").getAsString();
//                                            endDate = data.get("endDate").getAsString();
//                                            //endTime = data.get("endTime").getAsString();
//
//                                            //뒤에 0.000 잘라내기
//                                            startDate = startDate.substring(0, 16);
//                                            endDate = endDate.substring(0, 16);
//                                            startToEnd = startDate + " ~ " + endDate;
//
//                                            if (data.get("img_url").isJsonNull())
//                                                userProfile = "DEFAULT :: profile_IMAGE";
//                                            else
//                                                userProfile = data.get("img_url").getAsString();
//
//                                            Intent intent = new Intent(itemView.getContext(), PlanDetailActivity.class);
//
//                                            intent.putExtra("userProfile", userProfile);
//                                            intent.putExtra("userName", userName);
//                                            intent.putExtra("schedTitle", schedTitle);
//                                            intent.putExtra("aboutSched", aboutSched);
//                                            intent.putExtra("area", schedLocation);
//                                            intent.putExtra("startToEnd", startToEnd);
//
//                                            itemView.getContext().startActivity(intent);
//
//                                            Log.i("result", data.get("id").getAsString());
//                                        } else {
//
//                                            Toast.makeText(itemView.getContext(), "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
//                                        }
//
//                                    }
//                                }
//                            });


                }
            });
        }
    }
}
