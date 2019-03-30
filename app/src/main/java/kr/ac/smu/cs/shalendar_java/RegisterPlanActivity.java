package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
  일정 작성 Activity
  사용자로 부터 일정에 대한 상세 정보들을 입력 받는다.
  이때 '추천시간'을 받기 위해 '추처시간 받기 버튼을 누르면
  RecommandTimeActivity로 이동한다.
  개인 일정을 등록하는 경우에는 해당 기능 비활성화 시킨다.

 */
public class RegisterPlanActivity extends AppCompatActivity {

    private Button buttonToRecommandTime;
    private Button buttonCompleteRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_plan);

        buttonToRecommandTime = findViewById(R.id.registerPlan_toRecommand_button);
        buttonCompleteRegister = findViewById(R.id.registerPlan_completeRegister_button);

        /*
         추천시간 버튼 누를시 RecommandTimeActivity로 넘어간다.
         */
        buttonToRecommandTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RecommandTimeActivity.class);
                startActivityForResult(intent, CodeNumber.TO_RECOMMANDTIME_ACTIVITY);

            }
        });

        buttonCompleteRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
