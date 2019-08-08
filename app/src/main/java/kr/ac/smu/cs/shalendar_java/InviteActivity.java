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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/*
    공유캘린더 멤버 초대 Actvity

 */

public class InviteActivity extends AppCompatActivity {

    private EditText userEamil;
    private TextView addEmail;
    private Button toEmailInviteButton;

    //사용자 토큰 값
    private String userToken;

    //서버 연동
    private NetWorkUrl url = new NetWorkUrl();

    //서버로 넘길 값
    private String inputEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);


        final RecyclerView recyclerView = findViewById(R.id.invite_email_RecycleView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final UserEmailAdapter adapter = new UserEmailAdapter();

        //dummy Data
//        adapter.addItem(new UserEmail("jacob456@hanmail.net"));
//        adapter.addItem(new UserEmail("novojoon@naver.net"));
//        adapter.addItem(new UserEmail("esp5538@naver.com"));
//        recyclerView.setAdapter(adapter);

        /*
          '추가' TextView리스너 구현
         */
        userEamil = findViewById(R.id.register_title_EditText);
        addEmail = findViewById(R.id.invite_addEamil_TextView);

        if(userEamil != null) {
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
                inputEmail = userEamil.getText().toString().trim();

                JsonObject json = new JsonObject();
                json.addProperty("id", inputEmail);
                json.addProperty("cid", 20);

                Ion.with(getApplicationContext())

                        .load("POST", url.getServerUrl() + "/addUserCal")
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", userToken)
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {

                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                if(e != null) {
                                    Toast.makeText(getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                                }

                                else {
                                    String message = result.get("message").getAsString();
                                    if(message.equals("success")) {
                                        adapter.addItem(new UserEmail(inputEmail));
                                        recyclerView.setAdapter(adapter);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), message + "해당 유저 없음", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
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
                /*
                Intent intent = new Intent(getApplicationContext(), InviteByEmailActivity.class);
                startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);
                */

            }
        });
    }
}

