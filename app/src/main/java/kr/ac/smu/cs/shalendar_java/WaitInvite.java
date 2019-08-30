package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class WaitInvite extends AppCompatActivity implements View.OnClickListener {

    //UserToken
    private String userToken;

    ArrayList<WaitListItem> waitRecyclerList;
    private Context mContext = WaitInvite.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역
    private ViewGroup calendarLayout; //달력레이아웃 부분

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    //통신
    NetWorkUrl url = new NetWorkUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_invite);

        waitRecyclerList = new ArrayList<>();

        RecyclerView waitInviteRecyclerView = (RecyclerView) findViewById(R.id.waitListRecycler);
        waitInviteRecyclerView.setHasFixedSize(true);
        final WaitlistAdapter w_adapter = new WaitlistAdapter(waitRecyclerList, this);
        waitInviteRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        waitInviteRecyclerView.setAdapter(w_adapter);

        //SharedPreference에 저장된 userToken가져오기.
        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", MODE_PRIVATE);
        //값이 없으면 default로 0
        userToken = pref.getString("userToken", "NO_TOKEN");
        Log.i("넘겨받은 토큰", userToken);

        //통신 준비
        Ion.getDefault(getApplicationContext()).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(getApplicationContext()).getConscryptMiddleware().enable(false);

        final JsonObject json = new JsonObject();

        json.addProperty("cid", Global.getCid());


        Ion.with(getApplicationContext())
                .load("POST", url.getServerUrl() + "/showInvitation")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", userToken)
                //.progressDialog(progressDialog)
                .setJsonObjectBody(json)
                .asJsonObject() //응답
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //받을 변수
                        String sender, receiver, senderName, sender_Img, cNAME;
                        int cid;

                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        } else {
                            //응답 형식이 { "data":{"id":"jacob456@hanmail.net", "cid":1, "sid":10, "title":"korea"}, "message":"success"}
                            //data: 다음에 나오는 것들도 JsonObject형식.
                            //따라서 data를 JsonObject로 받고, 다시 이 data를 이용하여(어찌보면 JsonObject안에 또다른 JsonObject가 있는 것이다.
                            //JSONArray가 아님. 얘는 [,]로 묶여 있어야 함.

                            String message = result.get("message").getAsString();

                            //서버로 부터 응답 메세지가 success이면...

                            if (message.equals("success")) {
                                //서버 응답 오면 로딩 창 해제

                                //shareuserdata: {} 에서 {}안에 있는 것들도 JsonObject
                                JsonArray invitation = result.get("invitation").getAsJsonArray();

                                for (int i = 0; i < invitation.size(); i++) {
                                    JsonObject innerData = invitation.get(i).getAsJsonObject();
                                    sender = innerData.get("sender").getAsString();
                                    receiver = innerData.get("receiver").getAsString();
                                    sender_Img = innerData.get("sender_Img").getAsString();
                                    senderName = innerData.get("senderName").getAsString();
                                    cid = innerData.get("cid").getAsInt();
                                }


                                w_adapter.addItem(new WaitListItem());

                                w_adapter.notifyDataSetChanged();
                            } else

                            {
                                Toast.makeText(getApplicationContext(), "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


        Log.i("누가 먼저 실행되는 거임??333", "BoardActivity Ion 통신 끝");
    }


    private void addSideView() {

        Sidebar sidebar = new Sidebar(mContext);
        sideLayout.addView(sidebar);
        //sidebar.setUserID(userName);

        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sidebar.setEventListener(new Sidebar.EventListener() {

            @Override
            public void btnCancel() {
                closeMenu();
            }

            @Override
            public void btnLevel1() {
                Intent intent2 = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivityForResult(intent2, CodeNumber.TO_NOTICE_ACTIVITY);
            }

            @Override
            public void btnLevel2() {
                Intent intent2 = new Intent(getApplicationContext(), SettingActivity.class);
                startActivityForResult(intent2, CodeNumber.TO_SETTING_ACTIVITY);
            }

            @Override
            public void btnLevel3() {
                Intent intent2 = new Intent(getApplicationContext(), CreateCalendarActivity.class);
                startActivityForResult(intent2, CodeNumber.TO_CREATE_CALENDAR_ACTIVITY);
            }

            @Override
            public void btnInvited() {
                Intent intent3 = new Intent(getApplicationContext(), WaitInvite.class);
                startActivityForResult(intent3, CodeNumber.TO_CREATE_CALENDAR_ACTIVITY);
            }


        });
    }

    public void closeMenu() {

        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(mContext, R.anim.siderbar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewLayout.setVisibility(View.GONE);
                viewLayout.setEnabled(false);
                mainLayout.setEnabled(true);
            }
        }, 450);
    }

    public void showMenu() {

        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                showMenu();
                break;
            case R.id.btn_search:
                Intent intent2 = new Intent(getApplicationContext(), SearchPlanActivity.class);
                startActivityForResult(intent2, CodeNumber.TO_SEARCH_PLAN_ACTIVITY);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isMenuShow) {
            closeMenu();
        } else {

            if (isExitFlag) {
                finish();
            } else {

                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitFlag = false;
                    }
                }, 2000);
            }
        }
    }
}
