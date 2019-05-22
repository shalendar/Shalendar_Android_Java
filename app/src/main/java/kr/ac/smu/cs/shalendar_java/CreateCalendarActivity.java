package kr.ac.smu.cs.shalendar_java;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
    캘린더 생성하는 Activity

 */

public class CreateCalendarActivity extends AppCompatActivity {


    private CreateCalendarActivityDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_calendar);

        Button dialog = (Button) findViewById(R.id.register_complete);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog();
            }
        });
    }


    public void Dialog() {
        dialog = new CreateCalendarActivityDialog(CreateCalendarActivity.this,
                leftListener); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    //다이얼로그 클릭이벤트
    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(CreateCalendarActivity.this, "버튼을 클릭하였습니다.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };
}



