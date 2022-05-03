package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/*

  공유 일정 작성 경우
  개인 일정의 달력들을 모두 검토 한후
  팀원들이 모두 가능한 시간대를 추천해주는 Activity이다.
  추천시간을 서버로 부터 받으면 다시 RegisterPlanActivity로 돌아간다.

 */
public class RecommandTimeActivity extends AppCompatActivity {

    private Button buttonGetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand_time);

        buttonGetTime = findViewById(R.id.recommandTime_getTime_button);
        buttonGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RegisterPlanActivity.class);
                startActivity(intent);

            }
        });
    }
}
