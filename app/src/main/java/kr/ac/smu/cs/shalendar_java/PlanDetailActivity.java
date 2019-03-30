package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*

  일정 상세보기 Activity
  app bar의 메뉴에서 '일정 수정', '일정 삭제' 선택시
  UpdatePlan, DeletePlanActivity로 각각 넘어간다.
 */

public class PlanDetailActivity extends AppCompatActivity {

    private Button buttonToUpdate;
    private Button buttonToDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        buttonToUpdate = findViewById(R.id.planDetail_toUpdate_button);
        buttonToDelete = findViewById(R.id.planDetail_toDelete_button);

        /*
         일정 수정 화면으로 이동
        */
        buttonToUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdatePlanActivity.class);
                startActivityForResult(intent, CodeNumber.TO_UPDATEPLAN_ACTIVITY);
            }
        });

          /*
         일정 삭제 화면으로 이동
        */
        buttonToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeletePlanActivity.class);
                startActivityForResult(intent, CodeNumber.TO_DELETEPLAN_ACTIVITY);
            }
        });
    }
}
