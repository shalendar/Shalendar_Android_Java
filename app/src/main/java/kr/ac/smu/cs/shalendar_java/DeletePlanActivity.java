package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*

  등록된 일정을 삭제하는 Activity
  해당 일정을 삭제 하고 나서 다시 MainActivity로 돌아간다.

 */
public class DeletePlanActivity extends AppCompatActivity {

    private Button buttonCompleteDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_plan);


        buttonCompleteDelete = findViewById(R.id.deletePlan_toMain_button);

        /*
         일정 수정 화면으로 이동
        */
        buttonCompleteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
