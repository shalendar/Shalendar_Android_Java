package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 일정 수정 화면
 수정 완료 버튼을 누르면 메인 으로 넘어간다.

 */
public class UpdatePlanActivity extends AppCompatActivity {

    private Button buttonCompleteUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plan);

        buttonCompleteUpdate = findViewById(R.id.updatePlan_toMain_button);

        /*
         일정 수정 화면으로 이동
        */
        buttonCompleteUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
