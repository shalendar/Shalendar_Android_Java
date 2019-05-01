package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
    Login하는 Activity
    Login -> MainActivity로 넘어간다.

 */

public class LoginActivity extends AppCompatActivity {

    //button 멤버 변수
    private Button buttonToMain;
    private Button buttonToMember1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        buttonToMain = findViewById(R.id.login_toMain_button);

        buttonToMember1 = findViewById(R.id.login_signin_button);
        /*
           우선 버튼 클릭시 MainActivity로 넘어간다.
           - 나중에 CreateMemberActivity로 넘어가는 코드 짜야 한다.
         */
        buttonToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);

            }
        });

        buttonToMember1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), CreateMember1.class);
                startActivityForResult(intent2, CodeNumber.TO_CREATE_MEMBER1);

            }
        });

    }
}
