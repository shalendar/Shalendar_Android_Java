package kr.ac.smu.cs.shalendar_java;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
  일정 작성 Activity
  사용자로 부터 일정에 대한 상세 정보들을 입력 받는다.
  이때 '추천시간'을 받기 위해 '추처시간 받기 버튼을 누르면
  RecommandTimeActivity로 이동한다.
  개인 일정을 등록하는 경우에는 해당 기능 비활성화 시킨다.
 */
public class RegisterPlanActivity extends AppCompatActivity {

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

    //추가본 7.10
    boolean isPageOpen = false;
    ArrayList<RecommandTimeItem> itemList;

    RecommandTimeAdapter t_adapter;

    Animation translateUpAnim;
    Animation translateDownAnim;

    LinearLayout page;
    Button reccomandtime_button;

    Button sndbutton;
    //


    private String strfirstDate, strlastDate; //시작 날짜, 마지막 날짜
    int intFirstday, intLastday, intFirstmon, intLastmon;
    int startHour, startMinute, endHour, endMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_plan);

        //7.10추가 부분

        page = findViewById(R.id.page);

        sndbutton = findViewById(R.id.sendButton);

        RecyclerView recyclerView = findViewById(R.id.recotime_Recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        t_adapter = new RecommandTimeAdapter(this, itemList);

        t_adapter.addItem(new RecommandTimeItem("1", "14:00-15:00"));
        t_adapter.addItem(new RecommandTimeItem("2", "15:00-16:00"));
        t_adapter.addItem(new RecommandTimeItem("3", "16:00-17:00"));
        t_adapter.addItem(new RecommandTimeItem("4", "17:00-18:00"));

        recyclerView.setAdapter(t_adapter);

        t_adapter.notifyDataSetChanged();

        translateUpAnim = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        translateDownAnim = AnimationUtils.loadAnimation(this, R.anim.translate_down);

        SlidingPageAnimationListner animListener = new SlidingPageAnimationListner();
        translateUpAnim.setAnimationListener(animListener);
        translateDownAnim.setAnimationListener(animListener);

        //전송버튼 눌릴때
        sndbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_adapter.sendServer();
                recommandedTime.setText(t_adapter.sendRecommandTime());

                if (isPageOpen) {
                    page.startAnimation(translateDownAnim);
                } else {
                    page.setVisibility(View.VISIBLE);
                    page.startAnimation(translateUpAnim);
                }
            }
        });

        //추천시간 눌릴때
        reccomandtime_button = findViewById(R.id.register_getTime_Button);
        reccomandtime_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPageOpen) {
                    page.startAnimation(translateDownAnim);
                } else {
                    page.setVisibility(View.VISIBLE);
                    page.startAnimation(translateUpAnim);
                }
            }
        });



        //

        planTitle = findViewById(R.id.register_title_EditText);
        aboutPlan = findViewById(R.id.register_aboutPlan_EditText);
        location = findViewById(R.id.register_location_EditText);
        startDate = findViewById(R.id.register_startDate_TextView);
        endDate = findViewById(R.id.register_endDate_TextView);
        startTime = findViewById(R.id.register_startTime_TextView);
        endTime = findViewById(R.id.register_endTime_TextView);
        buttonToRecommandTime = findViewById(R.id.register_getTime_Button);
        buttonCompleteRegister = findViewById(R.id.register_registerPlan_Button);
        recommandedTime = findViewById(R.id.register_getTime_TextView);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("FFF");


        //날짜 선택
        initDate();

        //시간 선택
        initTime();

        /*
         추천시간 버튼 누를시 RecommandTimeActivity로 넘어간다.

        buttonToRecommandTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecommandTimeActivity.class);
                startActivityForResult(intent, CodeNumber.TO_RECOMMANDTIME_ACTIVITY);
            }
        });
        */
        buttonCompleteRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //7.10 추가부분
    private class SlidingPageAnimationListner implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            if (isPageOpen) {
                page.setVisibility(View.INVISIBLE);

                reccomandtime_button.setText("Open");
                isPageOpen = false;
            } else {
                reccomandtime_button.setText("Close");
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    //시간 설정
    private void initTime() {
        final Calendar cal = Calendar.getInstance();
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(RegisterPlanActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        startHour = hourOfDay;
                        startMinute = minute;

                        Log.d("시작시간", Integer.toString(hourOfDay));
                        Log.d("시작 분", Integer.toString(minute));
                        /*
                          시간 형식 08:12 오후
                         */
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        try {
                            Date date1 = format.parse("" + hourOfDay + ":" + minute);
                            startTime.setText(new SimpleDateFormat("hh:mm a").format(date1.getTime()));
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
                TimePickerDialog dialog = new TimePickerDialog(RegisterPlanActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                DatePickerDialog dialog = new DatePickerDialog(RegisterPlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //month = 0부터 시작한다.
                        intFirstmon = month;
                        intFirstday = dayOfMonth;
                        strfirstDate = dateFormatByUserCase(1, year, month, dayOfMonth);
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
                DatePickerDialog dialog = new DatePickerDialog(RegisterPlanActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            strlastDate = dateFormatByUserCase(2, lastYear, lastMon, lastDay);

        } else if (firstMon > lastMon) {
            Toast.makeText(RegisterPlanActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

        } else {
            if (firstDay > lastDay) {
                Toast.makeText(RegisterPlanActivity.this, "다시 입력해주세요.", Toast.LENGTH_LONG).show();

            } else {
                strlastDate = dateFormatByUserCase(2, lastYear, lastMon, lastDay);
            }
        }
    }

    /*
      flag값에 따라
      selectFirst에 setText할지
      selectLast에 setText할지 구분해주는 메소드.
     */
    private void setSelectFirstOrSelectLast(int flag, String txtmsg) {
        if (flag == 1) {
            startDate.setText(txtmsg);
        } else if (flag == 2) {
            endDate.setText(txtmsg);
        }
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

