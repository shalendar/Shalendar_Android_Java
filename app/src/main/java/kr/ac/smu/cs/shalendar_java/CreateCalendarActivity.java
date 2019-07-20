package kr.ac.smu.cs.shalendar_java;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static kr.ac.smu.cs.shalendar_java.CodeNumber.PICK_IMAGE_REQUEST;

/*
    캘린더 생성하는 Activity
*/
public class CreateCalendarActivity extends AppCompatActivity {

    private EditText calendarName;
    private EditText aboutCalendar;
    private Button registerCal;
    private CreateCalendarActivityDialog dialog;
    private ImageView imageView;


    //이미지 절대 경로
    private String imageURL;

    //서버에 보낼 data
    private String calName;
    private String aboutCal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_calendar);

        imageView = findViewById(R.id.imageView_createCal);
        calendarName = findViewById(R.id.calTitle_EditText_createCal);
        aboutCalendar = findViewById(R.id.aboutCal_EditText_createCal);

        //갤러리에서 사진 가져오기 위한 ImageView리스너 구현
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPictureFromGallery();
            }
        });



        registerCal = (Button) findViewById(R.id.register_complete);
        registerCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calName = calendarName.getText().toString().trim();
                aboutCal = aboutCalendar.getText().toString().trim();

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


    //로컬 폰의 갤러리에서 사진 선택
    private void getPictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpg");
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*
     폰에서 사진을 지정하면 해당 사진 주소를 가져온다.
     */
    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Log.d("여기까지", "ㅇ5");
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Log.d("여기까지", "ㅇ6");

        return cursor.getString(column_index);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            switch (requestCode) {
                //사진등록
                case PICK_IMAGE_REQUEST:
                    Log.d("여기까지", "ㅇ3");
                    if (resultCode == RESULT_OK) {
                        Log.d("여기까지", "ㅇ4");
                        imageURL = getPathFromURI(data.getData());
                        Log.d("사진 경로", imageURL);
                        imageView.setImageURI(data.getData());
                    }

                    //주소받아오기

            }
        }catch (Exception e) {
            Toast.makeText(this, "오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}



