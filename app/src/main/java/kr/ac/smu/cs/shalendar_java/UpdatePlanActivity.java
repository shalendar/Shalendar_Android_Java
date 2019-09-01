package kr.ac.smu.cs.shalendar_java;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 일정 수정 화면
 수정 완료 버튼을 누르면 메인 으로 넘어간다.

 */
public class UpdatePlanActivity extends AppCompatActivity {

    private Button buttonCompleteUpdate;
    private EditText planTitle;
    private EditText aboutPlan;
    private EditText location;
    private TextView startDate;
    private TextView endDate;
    private TextView startTime;
    private TextView endTime;
    private Button buttonToRecommandTime;
    private Button buttonCompleteRegister;
    private TextView recommandedTime;


    //서버로 보낼 data
    private String scheTitle;
    private String strStartDate;
    private String strStartTime;
    private String strEndDate;
    private String strEndTime;
    private String aboutSched;
    private String strLocation;

    //시작 날짜, 종료 날짜 비교
    int intFirstday, intLastday, intFirstmon, intLastmon;
    int startHour, startMinute, endHour, endMinute;

    private NetWorkUrl url = new NetWorkUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plan);

        //buttonCompleteUpdate = findViewById(R.id.sendButton);
        planTitle = findViewById(R.id.update_title_EditText);
        aboutPlan = findViewById(R.id.update_aboutPlan_EditText);
        location = findViewById(R.id.update_location_EditText);
        startDate = findViewById(R.id.update_startDate_TextView);
        endDate = findViewById(R.id.update_endDate_TextView);
        startTime = findViewById(R.id.update_startTime_TextView);
        endTime = findViewById(R.id.update_endTime_TextView);
        buttonToRecommandTime = findViewById(R.id.update_getTime_Button);
        buttonCompleteRegister = findViewById(R.id.update_registerPlan_Button);
        recommandedTime = findViewById(R.id.update_getTime_TextView);


        //날짜 선택
        initDate();

        //시간 선택
        initTime();


        //일정 수정 화면으로 이동 서버 통신 준비.

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this).getConscryptMiddleware().enable(false);


        buttonCompleteRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "눌림", Toast.LENGTH_SHORT).show();

                scheTitle = planTitle.getText().toString().trim();
                aboutSched = aboutPlan.getText().toString().trim();
                //startDate
                //startTime
                //strEndDate
                //startEndTime
                strLocation = planTitle.getText().toString().trim();


                JsonObject json = new JsonObject();

                json.addProperty("sid", Global.getSid());
                json.addProperty("title", scheTitle);
                json.addProperty("sContent", aboutSched);
                json.addProperty("startDate", strStartDate + " " + strStartTime);
                //json.addProperty("startTime",strStartTime);
                json.addProperty("endDate", strEndDate + " " + strEndTime);
                //json.addProperty("endTime",strEndTime);
                json.addProperty("area", strLocation);

                final ProgressDialog progressDialog = new ProgressDialog(UpdatePlanActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("잠시만 기다려주세요. 해당 수정된 일정을 등록 중 입니다~");
                progressDialog.show();


                Ion.with(getApplicationContext())
                        .load("POST", url.getServerUrl() + "/updateSche")
                        .progressDialog(progressDialog)
                        //요청 헤더 지정
                        .setHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                if (e != null) {
                                    Toast.makeText(getApplicationContext(), "Error during Server Connection", Toast.LENGTH_LONG).show();
                                } else {
                                    progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(), result.get("message").getAsString(), Toast.LENGTH_LONG).show();
                                    //setResult(RESULT_OK);
                                    //finish();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }


    //시간 설정
    private void initTime() {
        final Calendar cal = Calendar.getInstance();
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(UpdatePlanActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        startHour = hourOfDay;
                        startMinute = minute;

                        Log.d("시작시간", Integer.toString(hourOfDay));
                        Log.d("시작 분", Integer.toString(minute));

                        /*
                          뷰에 보여주는 시간 형식 08:12 오후
                         */
                        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                        //SimpleDateFormat format2 = new SimpleDateFormat("HH-mm-ss");
                        try {
                            Date date1 = format1.parse("" + hourOfDay + ":" + minute);
                            startTime.setText(new SimpleDateFormat("hh:mm a").format(date1.getTime()));
                            strStartTime = new SimpleDateFormat("HH-mm-ss").format(date1.getTime());
                            Log.i("시작 시간", strStartTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                dialog.show();
            }
        });


        endTime.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(UpdatePlanActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endHour = hourOfDay;
                        endMinute = minute;

                        Log.d("끝 시간", Integer.toString(hourOfDay));
                        Log.d("끝 분", Integer.toString(minute));

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        try {
                            Date date2 = format.parse("" + hourOfDay + ":" + minute);
                            endTime.setText(new SimpleDateFormat("hh:mm a").format(date2.getTime()));
                            strEndTime = new SimpleDateFormat("HH-mm-ss").format(date2.getTime());
                            Log.i("종료시간", strEndTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                dialog.show();
            }
        });
    }

    private void initDate() {
        final Calendar cal = Calendar.getInstance();
        //시작 날짜 선택
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(UpdatePlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //month = 0부터 시작한다.
                        intFirstmon = month;
                        intFirstday = dayOfMonth;
                        strStartDate = dateFormatByUserCase(1, year, month, dayOfMonth);

                        Log.i("시작 날짜", strStartDate);


                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                //dialog.getDatePicker().setMaxDate(new Date().getTime());//입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        //마지막 날짜 선택
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(UpdatePlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        intLastmon = month;
                        intLastday = dayOfMonth;
                        checkDate(intFirstmon, intLastmon, intFirstday, intLastday, year);
                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                //dialog.getDatePicker().setMaxDate(new Date().getTime());//입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });
    }


    private void checkDate(int firstMon, int lastMon, int firstDay, int lastDay, int lastYear) {

        if (firstMon < lastMon) {
            strEndDate = dateFormatByUserCase(2, lastYear, lastMon, lastDay);

        } else if (firstMon > lastMon) {
            Toast.makeText(UpdatePlanActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

        } else {
            if (firstDay > lastDay) {
                Toast.makeText(UpdatePlanActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

            } else {
                strEndDate = dateFormatByUserCase(2, lastYear, lastMon, lastDay);
                Log.i("종료 날짜", strEndDate);
            }
        }
    }

    /*
      flag값에 따라
      selectFirst에 setText할지
      selectLast에 setText할지 구분해주는 메소드.
     */
    private void setSelectFirstOrSelectLast(int flag, String txtmsg) {
        if (flag == 1)
            startDate.setText(txtmsg);
        else if (flag == 2)
            endDate.setText(txtmsg);
    }

    /*
       출력형식을 '2018-08-15'와 같이 지정해주는 메소드.
       return 타입 : String
       이 함수를 이용하여 strFirstDate, strLastDate에 만들어진 형식을 저장할 수 있다.
     */

    private String dateFormatByUserCase(int flag, int year, int month, int dayOfMonth) {

        String format = null;
        String txtmsg;

        SimpleDateFormat dateformat = new SimpleDateFormat("EEE");
        Date date = new Date(year, month, dayOfMonth - 1);
        String dayOfWeek = dateformat.format(date);

        if (month + 1 < 10) {
            int ten_month = (month + 1) / 10;
            int one_month = (month + 1) % 10;

            String month1 = Integer.toString(ten_month) + Integer.toString(one_month);

            if (dayOfMonth < 10) {
                int ten_date = (dayOfMonth) / 10;
                int one_date = (dayOfMonth) % 10;
                String date1 = Integer.toString(ten_date) + Integer.toString(one_date);

                txtmsg = month1 + "월" + date1 + "일" + String.format("(%s)", dayOfWeek);
                format = String.format("%d-", year) + month1 + "-" + date1;

                setSelectFirstOrSelectLast(flag, txtmsg);
            } else if (dayOfMonth >= 10) {
                txtmsg = month1 + "월" + String.format("%d일", dayOfMonth) + String.format("(%s)", dayOfWeek);
                format = String.format("%d-", year) + month1 + "-" + String.format("%d", dayOfMonth);
                setSelectFirstOrSelectLast(flag, txtmsg);
            }
        } else if (month + 1 >= 10) {
            if (dayOfMonth < 10) {
                int ten_date = (dayOfMonth) / 10;
                int one_date = (dayOfMonth) % 10;
                String date1 = Integer.toString(ten_date) + Integer.toString(one_date);

                txtmsg = (month + 1) + "월" + date1 + "일" + String.format("(%s)", dayOfWeek);
                format = String.format("%d-", year) + (month + 1) + "-" + date1; //String.format("%d일", dayOfMonth);
                setSelectFirstOrSelectLast(flag, txtmsg);
            } else if (dayOfMonth >= 10) {
                txtmsg = (month + 1) + "월" + String.format("%d일", dayOfMonth) + String.format("(%s)", dayOfWeek);
                format = String.format("%d-", year) + (month + 1) + "-" + String.format("%d", dayOfMonth);
                setSelectFirstOrSelectLast(flag, txtmsg);
            }
        }
        return format;
    }
}
