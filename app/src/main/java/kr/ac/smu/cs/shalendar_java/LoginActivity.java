package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
    Login하는 Activity
    Login -> MainActivity로 넘어간다.

 */

public class LoginActivity extends AppCompatActivity {

    //EditText 멤버 변수
    private EditText id_Email;
    private EditText password;

    //button 멤버 변수
    private Button buttonToMain;
    private Button buttonToMember1;

    //EditText값을 String에 저장하기 위한 멤버변수.
    private String userEmail;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id_Email = findViewById(R.id.login_id_EditText);
        password = findViewById(R.id.login_password_EditText);


        buttonToMain = findViewById(R.id.login_toMain_button);
        buttonToMember1 = findViewById(R.id.login_signin_button);



        /*
           우선 버튼 클릭시 MainActivity로 넘어간다.
           - 나중에 CreateMemberActivity로 넘어가는 코드 짜야 한다.
        */


        //로그인 버튼
        buttonToMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                userEmail = id_Email.getText().toString().trim();
                userPassword = password.getText().toString().trim();

                Log.d("맞아?", Boolean.toString(userEmail.equals("jacob456@hanmail.net")));


                //입력하는 e-mail주소 형식 예외처리
                if(userEmail.equals("jacob") && userPassword.equals("456")) {
                    Toast.makeText(getApplicationContext(), "사용자정보 일치 메인화면으로이동", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);
                }

                else if(userEmail.equals("") || userPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                else {
                    Log.d("입력한 ID", userEmail);
                    Log.d("입력한 비밀번호", userPassword);
                    Toast.makeText(getApplicationContext(), "아이디/비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show();
                }
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
