package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateMember1 extends AppCompatActivity {

    private EditText editTextUserEmail;
    private Button buttonToMember2;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member1);

        editTextUserEmail = (EditText)findViewById(R.id.create_member1_editText1);
        buttonToMember2 = findViewById(R.id.create_member2_button);


        buttonToMember2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원 가입 2번째로 보낼 userEmail
                userEmail = editTextUserEmail.getText().toString().trim();
                Log.d("Email", userEmail);

                if(userEmail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Email을 입력하세요~", Toast.LENGTH_LONG).show();
                }
                else if(!userEmail.contains("@")) {
                    Toast.makeText(getApplicationContext(), "잘못된 Email형식입니다", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), CreateMember2.class);
                    intent.putExtra("userEmail", userEmail);
                    startActivityForResult(intent, CodeNumber.TO_CREATE_MEMBER2);
                }
            }
        });
    }
}
