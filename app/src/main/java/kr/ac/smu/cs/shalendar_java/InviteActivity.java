package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
    공유캘린더 멤버 초대 Actvity

 */

public class InviteActivity extends AppCompatActivity {

    private Button toEmailInviteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        toEmailInviteButton = findViewById(R.id.Invite_email_button);

         /*
           버튼 클릭시 초대장 보낼 이메일 입력하는 액티비티로 이동
           이메일 입력 액티비티->InviteByEmailActivity
         */
        toEmailInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteByEmailActivity.class);
                startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);

            }
        });
    }
}
