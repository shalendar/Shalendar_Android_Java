package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
    공유캘린더 멤버 초대 Actvity

 */

public class InviteActivity extends AppCompatActivity {

    private EditText userEamil;
    private TextView addEmail;
    private Button toEmailInviteButton;

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

        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input_Email = userEamil.getText().toString().trim();
                adapter.addItem(new UserEmail(input_Email));

                recyclerView.setAdapter(adapter);
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
