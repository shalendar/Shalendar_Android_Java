package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/*
    공유캘린더 멤버 초대 Actvity
 */

public class InviteActivity extends AppCompatActivity {

    private EditText userEmail;
    private TextView addEmail;
    private Button toEmailInviteButton;
    ImageButton backButton;

    //사용자 토큰 값
    private String userToken;

    //서버 연동
    private NetWorkUrl url = new NetWorkUrl();

    //서버로 넘길 값
    private String inputEmail;

    private RecyclerView recyclerView;

    private UserEmailAdapter adapter;

    //sideBar의 공유달력 intent로 넘긴 값.
    private String senderName;
    private String senderID;
    private String senderImg;
    private String calName;
    private int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.invite_email_RecycleView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserEmailAdapter();

        /*
          '추가' TextView리스너 구현
         */
        userEmail = findViewById(R.id.register_title_EditText);
        addEmail = findViewById(R.id.invite_addEamil_TextView);

        if (userEmail != null) {
            addEmail.setTextColor(Color.parseColor("#ef7172"));
        }


        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", MODE_PRIVATE);
        userToken = pref.getString("userToken", "NO_TOKEN");
        Log.i("Sharepref에 저장된 토큰", userToken);


        //통신 준비 --> ION
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this).getConscryptMiddleware().enable(false);


        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEmail = userEmail.getText().toString().trim();

                JsonObject json = new JsonObject();
                json.addProperty("id", inputEmail);

                Future ion = Ion.with(getApplicationContext())
                        .load("POST", url.getServerUrl() + "/emailCheck")
                        .setHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {

                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                if (e != null) {
                                    Toast.makeText(getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                                } else {
                                    parseFromServer(result);
                                }
                            }
                        });

                //응답 받을 때까지 대기.
                try {
                    ion.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //adapter.addItem(new UserEmail(input_Email));
                //recyclerView.setAdapter(adapter);
            }
        });

         /*
           버튼 클릭시 초대장 보낼 이메일 입력하는 액티비티로 이동
           이메일 입력 액티비티->InviteByEmailActivity
         */
        toEmailInviteButton = findViewById(R.id.invite_email_button);
        toEmailInviteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                JsonArray jsonArray = new JsonArray();
                JsonParser parser = new JsonParser();

                for (int i = 0; i < adapter.getItemCount(); i++) {

                    if (adapter.getItem(i).getIs_checked()) {

                        JsonPrimitive element = new JsonPrimitive(adapter.getItem(i).getUserEmail());
                        jsonArray.add(element);
                    }
                }


                Intent intent = getIntent();
                cid = intent.getIntExtra("cid", -1);
                calName = intent.getStringExtra("calName");
                senderID = Sidebar.userProfile_ID;
                senderName = Sidebar.userProfile_Name;
                senderImg = Sidebar.userProfile_ImgURL;


                Log.i("초대한 사람 ID", senderID);
                Log.i("초대 할 사람", jsonArray.toString());
                Log.i("초대한 사람 이름", senderName);
                Log.i("초대한 사람 이미지", senderImg);
                Log.i("초대 cid", Integer.toString(cid));
                Log.i("초대 달력", calName);


                JsonObject json = new JsonObject();
                json.addProperty("sender", senderID);
                json.add("receiver", jsonArray);
                json.addProperty("senderName", senderName);
                json.addProperty("sender_img", senderImg);
                json.addProperty("cid", cid);
                json.addProperty("cName", calName);


                //겹치는 달력있나 확인
                int result = findMember(jsonArray, senderID);
                if (result == 1) {

                } else {

                    Ion.with(getApplicationContext())
                            .load("POST", url.getServerUrl() + "/pushInvitation")
                            .setHeader("Content-Type", "application/json")
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {

                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        String message = result.get("message").getAsString();

                                        if (message.equals("success")) {
                                            Toast.makeText(getApplicationContext(), "초대 알림이 발송되었습니다.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), message + "초대 실패", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                            });
                }
            }
        });

    }


    public int findMember(JsonArray memberList, String invitedMember) {

        final ArrayList<String> calendarMember = new ArrayList<>();

        //해당 캘린더멤버 조회
        JsonObject json = new JsonObject();
        json.addProperty("cid", MainActivity.cid);
        Log.i("너가궁금해", Integer.toString(cid));

        Future ion = Ion.with(getApplicationContext())
                .load("POST", url.getServerUrl() + "/readUserCal")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", userToken)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                        } else {
                            String message = result.get("message").getAsString();
                            if (message.equals("success")) {
                                JsonArray data = result.get("data").getAsJsonArray();
                                for (int i = 0; i < data.size(); i++) {
                                    JsonObject jsonObject = data.get(i).getAsJsonObject();
                                    String calMember = jsonObject.get("id").getAsString();

                                    if (jsonObject.get("cid").isJsonNull())
                                        Log.i("cid는 널", "NULL임");
                                    calendarMember.add(calMember);
                                    Log.i("뭘까?용용용", calMember);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), message + "캘린더 멤버 불러오기 실패", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                });
        //응답 받을 때까지 대기.
        try {
            ion.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //초대된 회원 확인하는 코드
        for (int i = 0; i < memberList.size(); i++) {
            String jsonOb = memberList.get(i).getAsString();
            Log.i("뭘까?용", jsonOb);
            for (int j = 0; j < calendarMember.size(); j++) {
                if (jsonOb.equals(calendarMember.get(j))) {
                    Toast.makeText(getApplicationContext(), jsonOb + "는 이미 초대된 회원입니다.", Toast.LENGTH_SHORT).show();
                    return 1;
                }
                if (jsonOb.equals(senderID)) {
                    Toast.makeText(getApplicationContext(), "자신은 초대할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return 1;
                }
            }
        }
        return 0;
    }


    public void parseFromServer(JsonObject result) {
        String message = result.get("message").getAsString();
        if (message.equals("please check email")) {
            //Log.i("현재 초대 리사이클러뷰 인원", Integer.toString(adapter.getItemCount()));

            String imageURl;
            if (result.get("img_url").isJsonNull())
                imageURl = "DEFAULT :: profile_IMAGE";
            else
                imageURl = result.get("img_url").getAsString();

            adapter.addItem(new UserEmail(inputEmail, false, imageURl));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), message + "해당 사용자는 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
}

