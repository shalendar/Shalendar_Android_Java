package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateMember3 extends AppCompatActivity {

    private String userEmail;
    private String userName;
    private String userPassword;
    private Button buttonToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member3);

        buttonToLogin = findViewById(R.id.loginpage_button);


        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userName = intent.getStringExtra("userName");
        userPassword = intent.getStringExtra("userPassword");

//        Log.i("사용자 이메일", userEmail);
//        Log.i("사용자 이름", userName);
//        Log.i("사용자 비밀번호", userPassword);


        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, CodeNumber.TO_LOGIN_ACTIVITY);

            }
        });
    }
}
