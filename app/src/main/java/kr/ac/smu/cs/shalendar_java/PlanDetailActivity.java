package kr.ac.smu.cs.shalendar_java;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import static kr.ac.smu.cs.shalendar_java.CodeNumber.PICK_IMAGE_REQUEST;

/*
  일정 상세보기 Activity
  app bar의 메뉴에서 '일정 수정', '일정 삭제' 선택시
  UpdatePlan, DeletePlanActivity로 각각 넘어간다.
 */
public class PlanDetailActivity extends AppCompatActivity  {

    private Button buttonToUpdate;
    private Button buttonToDelete;

    //js
    private Context mContext = PlanDetailActivity.this;


    //이미지 절대 경로
    private String imageURL;

    //통신 위한 url
    private NetWorkUrl url = new NetWorkUrl();


    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);



        //리스트뷰
        ListView plandetail_Listview = (ListView) findViewById(R.id.planDetail_listView);

        //헤더 삽입
        View header = getLayoutInflater().inflate(R.layout.activity_plandetailreply_header, null, false);
        plandetail_Listview.addHeaderView(header);

        final TextView schedTitle = findViewById(R.id.planDetail_title_textView);
        final TextView userName = findViewById(R.id.plandetail_header_profile_name);
        final TextView location = findViewById(R.id.plandetail_header_location);
        final TextView aboutSched = findViewById(R.id.plandetail_header_aboutSchedule);
        final TextView startToEndTime = findViewById(R.id.plandetail_header_startToEndTime);


        Intent intent = getIntent();
        intent.getStringExtra("title");

        schedTitle.append(intent.getStringExtra("schedTitle"));
        userName.setText(intent.getStringExtra("userName"));
        location.setText(intent.getStringExtra("area"));
        aboutSched.setText(intent.getStringExtra("aboutSched"));
        startToEndTime.setText(intent.getStringExtra("startToEnd"));

        final PlandetailAdapter plandetailAdapter = new PlandetailAdapter();


        //댓글 받아오는 통시 시작
        //서버와 통신. 헤더 부분의 정보를 서버응답으로 부터 온 정보들을 파싱하여 set한다.
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this).getConscryptMiddleware().enable(false);

        JsonObject json = new JsonObject();
        json.addProperty("cid",20);
        json.addProperty("sid", 10);

//        final ProgressDialog progressDialog = new ProgressDialog(PlanDetailActivity.this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("잠시만 기다려주세요. 해당 일정 등록 중 입니다~");
//        progressDialog.show();
//

        Ion.with(getApplicationContext())
                .load("POST", url.getServerUrl() + "/readComments")
                .setHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String comments, id, rdate;
                        int commentNum;

                        if(e != null) {
                            Toast.makeText(getApplicationContext(), "Server Connection Error!", Toast.LENGTH_LONG).show();
                        }

                        else {
                            //응답 형식이 { "data":{"id":"jacob456@hanmail.net", "cid":1, "sid":10, "title":"korea"}, "message":"success"}
                            //data: 다음에 나오는 것들도 JsonObject형식.
                            //따라서 data를 JsonObject로 받고, 다시 이 data를 이용하여(어찌보면 JsonObject안에 또다른 JsonObject가 있는 것이다.
                            //JSONArray가 아님. 얘는 [,]로 묶여 있어야 함.

                            String message = result.get("message").getAsString();
                            //서버로 부터 응답 메세지가 success이면...

                            if(message.equals("success")) {
                                //서버 응답 오면 로딩 창 해제
                               // progressDialog.dismiss();

                                //data: {} 에서 {}안에 있는 것들도 JsonObject

                                JsonArray data = result.getAsJsonArray("data");
                                for(int i=0; i<data.size(); i++){
                                    commentNum = data.getAsInt();
                                    comments = data.getAsString();
                                    id = data.getAsString();
                                    rdate = data.getAsString();
                                    plandetailAdapter.addItem(new PlandetailItem(id, rdate, comments));
                                }


                                //Log.i("result",data.get("id").getAsString());
                            } else {

                                Toast.makeText(getApplicationContext(), "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

        plandetail_Listview.setAdapter(plandetailAdapter);



//        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
//        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
//        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
//        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
//        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
//        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));



        //길게 눌렀을 때
        plandetail_Listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("댓글 수정/삭제");

                dialog.setMessage("댓글 수정, 삭제하십니까?")
                        .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })

                        .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                return false;
            }
        });

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}