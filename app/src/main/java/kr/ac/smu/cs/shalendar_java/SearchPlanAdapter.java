package kr.ac.smu.cs.shalendar_java;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class SearchPlanAdapter extends RecyclerView.Adapter<SearchPlanAdapter.ViewHolder> {

    private NetWorkUrl url = new NetWorkUrl();

    private ArrayList<SearchPlanItem> searchList;
    private Context context;

    public SearchPlanAdapter(ArrayList<SearchPlanItem> searchList, Context mContext) {
        this.searchList = searchList;
        this.context = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchplan_item, null);
        SearchPlanAdapter.ViewHolder sh = new SearchPlanAdapter.ViewHolder(v);

        return sh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String planname = searchList.get(position).getPlanname();
        holder.searchedPlan.setText(planname);
    }

    @Override
    public int getItemCount() {
        return (null !=  searchList? searchList.size() : 0);
    }


    public void addItem(SearchPlanItem item){
        searchList.add(item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView searchedPlan;


        public ViewHolder(View itemView) {

            super(itemView);
            this.searchedPlan = (TextView) itemView.findViewById(R.id.searchPlanitem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.setSid(searchList.get(getAdapterPosition()).getSid());
                    Toast.makeText(v.getContext(), searchList.get(getAdapterPosition()).getPlanname() + "여기로 간다잉", Toast.LENGTH_SHORT).show();

                    JsonObject json = new JsonObject();

                    json.addProperty("sid", Global.getSid());

                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("해당 댓글로 이동중~" + searchList.get(getAdapterPosition()).getSid());
                    progressDialog.show();

                    Global global = new Global();
                    global.netWork_ShowSche(context, progressDialog, json, url);


//                    Future ion = Ion.with(context)
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
//                                        Toast.makeText(context, "Server Connection Error!", Toast.LENGTH_LONG).show();
//                                    } else {
//                                        //응답 형식이 { "data":{"id":"jacob456@hanmail.net", "cid":1, "sid":10, "title":"korea"}, "message":"success"}
//                                        //data: 다음에 나오는 것들도 JsonObject형식.
//                                        //따라서 data를 JsonObject로 받고, 다시 이 data를 이용하여(어찌보면 JsonObject안에 또다른 JsonObject가 있는 것이다.
//                                        //JSONArray가 아님. 얘는 [,]로 묶여 있어야 함.
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
//                                            Intent intent = new Intent(context, PlanDetailActivity.class);
//                                            intent.putExtra("userProfile", userProfile);
//                                            intent.putExtra("userName", userName);
//                                            intent.putExtra("schedTitle", schedTitle);
//                                            intent.putExtra("aboutSched", aboutSched);
//                                            intent.putExtra("area", schedLocation);
//                                            intent.putExtra("startToEnd", startToEnd);
//
//                                            context.startActivity(intent);
//
//                                            Log.i("result", data.get("id").getAsString());
//                                        } else {
//
//                                            Toast.makeText(context, "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
//                                        }
//
//                                    }
//                                }
//                            });
//
//                    try {
//                        ion.get();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            });

        }

    }
}
