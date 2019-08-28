package kr.ac.smu.cs.shalendar_java;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.AnimateGifMode;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.math.RoundingMode;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

import static kr.ac.smu.cs.shalendar_java.CodeNumber.PICK_IMAGE_REQUEST;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //
    ArrayList<MainPlanItem> mainRecyclerList;

    private TextView textViewTitle;
    private TextView selectedDate;
    private Button buttonToBoard;
    private Button buttonToRegisterPlan;


    //UserToken
    private String userToken;

    private String imageURL;

    private ImageView imageView;

    public static int cid;

    public static String calName;

    //7-17
    boolean isPageOpen = false;

    Animation translateUpAnim;
    Animation translateDownAnim;

    LinearLayout main_animation;
    LinearLayout calendarLinear;
    FrameLayout calendarFrame;
    RelativeLayout calendarRelative;

    //js
    private Context mContext = MainActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역
    private ViewGroup calendarLayout; //달력레이아웃 부분

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    //materialCalendar
    String time, kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;
    Cursor cursor;

    //사용자 지정 날짜 가지는 자료구조.
    HashMap<String, String> map = new HashMap<>();

    //통신
    NetWorkUrl url = new NetWorkUrl();

    //서버로 받은 것.
    ArrayList<ScheduleData> schedList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", MODE_PRIVATE);
        userToken = pref.getString("userToken", "NO_TOKEN");
        Log.i("Main화면::넘겨받은 토큰", userToken);


        //showAllSche() 원래 위치
        //showAllSche();

        checkPermissions();

        textViewTitle = (TextView) findViewById(R.id.calendarNameTextView);
        buttonToBoard = (Button) findViewById(R.id.main_toBoard_button);
        selectedDate = (TextView)findViewById(R.id.TextView1);
        buttonToRegisterPlan = (Button) findViewById(R.id.main_ToRegister_button);

        //07-17
        main_animation = findViewById(R.id.main_anipage);
        calendarFrame = findViewById(R.id.calendarFrame);
        calendarRelative = findViewById(R.id.main_relative);

        //애니메이션
        translateUpAnim = AnimationUtils.loadAnimation(this, R.anim.maintranslate_up);
        translateDownAnim = AnimationUtils.loadAnimation(this, R.anim.maintranslate_down);

        MainActivity.SlidingPageAnimationListner animListener = new MainActivity.SlidingPageAnimationListner();
        translateUpAnim.setAnimationListener(animListener);
        translateDownAnim.setAnimationListener(animListener);

        //리사이클러 부분

        mainRecyclerList=new ArrayList<>();

        //insertData();

        RecyclerView mainRecyclerView = (RecyclerView)findViewById(R.id.mainRecyclerview);
        mainRecyclerView.setHasFixedSize(true);
        MainPlanAdapter m_adapter = new MainPlanAdapter(mainRecyclerList,this);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        mainRecyclerView.setAdapter(m_adapter);

        //JS
        init();

        addSideView();  //사이드바 add

        if(cid == 0) {
            Toast.makeText(getApplicationContext(), "달력을 먼저 선택하세요~", Toast.LENGTH_LONG).show();
        }

        else {
            //materialCalendar뷰 초기화
            initCalendarView();
            //서버로 부터 해당 달력의 일정을 가져온다.
            showAllSche();
            //서버로 부터 받은 모든 일정을 달력에 표시
            setCalendarView();
        }

        /*
          MainActivity에서 BoardActivity로 넘어간다.
        */
        buttonToBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cid == 0) {
                    Toast.makeText(getApplicationContext(), "달력을 먼저 선택하세요~", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                    startActivityForResult(intent, CodeNumber.TO_BOARD_ACTIVITY);
                }
            }
        });

        /*
          MainActivity에서 RegisterPlanActivity로 넘어간다.
        */
        buttonToRegisterPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterPlanActivity.class);
                startActivityForResult(intent, CodeNumber.TO_REGISTERPLAN_ACTIVITY);
            }
        });

    }

    public void initCalendarView() {
        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        materialCalendarView.setArrowColor(Color.parseColor("#ff6067"));
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017,0,1))
                .setMaximumDate(CalendarDay.from(2020,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);
    }

    public void setCalendarView() {
        String[] resultDate = new String[schedList.size()];
        Log.i("resultDate크기", Integer.toString(resultDate.length));


//        for(int i = 0; i< schedList.size(); i++) {
//            //시작 날짜, 끝 날짜 같은 경우
//            resultDate[i] = schedList.get(i).startDate;
//            Log.i("resultDate에 들어있는 시작 날짜", resultDate[i]);
//        }
//
//        new ApiSimulator(resultDate).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");


                //
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String shot_Day = Year + "-" + Month + "-" + Day;
                //ParsePosition pos = new ParsePosition( 0 );
                //Date initialDate = format.parse(shot_Day, pos);

                //String parseDate = format.format(initialDate);

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();


//                insertData();



                ///////애니메이션 구현
//                if (isPageOpen) {
//                    //calendarRelative.setClickable(false);
//                    main_animation.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getApplicationContext(),"열림",Toast.LENGTH_SHORT).show();
//                    main_animation.startAnimation(translateDownAnim);
//                    /*
//                    calendarLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            main_animation.startAnimation(translateDownAnim);
//                        }
//                    });*/
//                }
//
//                else {
//                    main_animation.setVisibility(View.VISIBLE);
//                    main_animation.startAnimation(translateUpAnim);
//                }

                Toast.makeText(getApplicationContext(), shot_Day , Toast.LENGTH_SHORT).show();

                //TextView에 삽입
                selectedDate.setText(shot_Day);
            }
        });
    }



    public void insertData(){

        Log.i("LIST 길이", Integer.toString(schedList.size()));
        for(int i=0; i<schedList.size(); i++) {
            MainPlanItem mitem = new MainPlanItem();
            mitem.setMainPlanname(schedList.get(i).sche_title);
            mitem.setMainPlantime(schedList.get(i).startDate + " ~ " + schedList.get(i).endDate);

            ArrayList<MainPlanTeamIteam> mtItem = new ArrayList<>();
            for(int j=0; j<6; j++){
                mtItem.add(new MainPlanTeamIteam(R.drawable.face));
            }
            mitem.setTeamPicList(mtItem);
            mainRecyclerList.add(mitem);
        }
    }


    //7.17 추가부분
    private class SlidingPageAnimationListner implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            if (isPageOpen) {
                main_animation.setVisibility(View.INVISIBLE);
                isPageOpen = false;
            } else {

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


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //calendar에는 시스템의 현재 날짜가 저장되어있다.
            Calendar calendar = Calendar.getInstance();
            //ArrayList를 사용해야 한다.
            ArrayList<CalendarDay> dates = new ArrayList<>();


            //dummy data Test용 for문
            //특정날짜 달력에 점표시해주는곳
            //월은 0이 1월 년,일은 그대로
            //string 문자열인 Time_Result 을 받아와서 -를 기준으로짜르고 string을 int 로 변환
            for (int i = 0; i < Time_Result.length; i++) {
                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);
                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }

            //map에 있는 data 처리 for문
//            for(String key : map.keySet()) {
//                Log.d("시작날짜 key", key);
//                Log.d("종료날짜 value", map.get(key));
//                //시작 날짜, 종료날짜 같은 경우.
//                if(key.equals(map.get(key))) {
//                    String[] time = key.split("-");
//                    int year = Integer.parseInt(time[0]);
//                    int month = Integer.parseInt(time[1]);
//                    int dayy = Integer.parseInt(time[2]);
//                    Log.d("쪼갠 날짜", Integer.toString(year) + "," + Integer.toString(month) + "," + Integer.toString(dayy));
//                    calendar.set(year, month-1, dayy);
//                    CalendarDay day = CalendarDay.from(calendar);
//                    dates.add(day);
//                }

                //시작 날짜, 종료날짜 다른 경우

//            }

            //dates ArrayList에 사용자 지정 날짜들이 들어있다. 2019-7-8형식.
            Log.d("들어있는 Date", dates.toString());
            return dates;
        }



        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }


            //실질적으로 dot을 찍는 class의 Method를 호출한다.
            EventDecorator event1 = new EventDecorator(1, Color.parseColor("#ff6067"),
                    calendarDays, MainActivity.this);
            EventDecorator event2 = new EventDecorator(2, Color.parseColor("#f8c930"),
                    calendarDays, MainActivity.this);


//            materialCalendarView.addDecorator(event1);
            int[] eventCount = EventDecorator.eventCount;

            Log.d("배열 길이", Integer.toString(eventCount.length));
            for(int i = 0; i<3; i++) {
                if(eventCount[i] == 0) {
                    materialCalendarView.addDecorator(event1);
                }

                else if(eventCount[i] == 1) {
                    materialCalendarView.addDecorator(event2);
                }
            }
        }
    }

    private void init() {

        findViewById(R.id.btn_menu).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);

        mainLayout = findViewById(R.id.id_main);
        viewLayout = findViewById(R.id.fl_silde);
        sideLayout = findViewById(R.id.view_sildebar);
        calendarLayout = findViewById(R.id.calendarFrame);
    }

    private void addSideView() {

        final Sidebar sidebar = new Sidebar(mContext);
        sideLayout.addView(sidebar);
        //sidebar.setUserID(userName);

        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sidebar.setEventListener(new Sidebar.EventListener() {

            @Override
            public void btnCancel() {
                closeMenu();
            }

            @Override
            public void btnLevel1() {
                Intent intent2 = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivityForResult(intent2, CodeNumber.TO_NOTICE_ACTIVITY);
            }

            @Override
            public void btnLevel2() {
                Intent intent2 = new Intent(getApplicationContext(), SettingActivity.class);
                startActivityForResult(intent2, CodeNumber.TO_SETTING_ACTIVITY);
            }

            @Override
            public void btnLevel3() {
                Intent intent2 = new Intent(getApplicationContext(), CreateCalendarActivity.class);
                startActivityForResult(intent2, CodeNumber.TO_CREATE_CALENDAR_ACTIVITY);
            }

            @Override
            public void btnInvited() {
                Intent intent2 = new Intent(getApplicationContext(), WaitInvite.class);
                startActivityForResult(intent2, CodeNumber.TO_WAITINVITEACTIVITY);
            }

            @Override
            public void image_profile(){
                getPictureFromGallery();
            }

        });
    }

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
                    if (resultCode == RESULT_OK) {
                        imageURL = getPathFromURI(data.getData());
                        Log.d("사진 경로", imageURL);
                        imageView = findViewById(R.id.image_profile);
                        //Request to Server.
                        setUserProfileImage_Server(imageURL);

                        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("img_url", imageURL);
                        editor.commit();
                    }
            }

        }catch (Exception e) {
            Toast.makeText(this, "오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    /*
     사진 지정 후 서버와 통신 하는 메소드.
     서버에서 success받으면
     imageView에 set한다.
     */
    public void setUserProfileImage_Server(final String imageUrl) {

        Log.i("프로필 변경", userToken);
        File file = new File(imageUrl);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("프로필 사진 등록 중 입니다~");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        Ion.with(this)
                .load("POST",url.getServerUrl() + "/imageChange")
                //.setHeader("Content-Type", "application/json")
                .setHeader("Authorization", userToken)
                .progressDialog(progressDialog)
                .setMultipartFile("file", file)
                //응답
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        else {
                            String message = result.get("message").getAsString();

                            if(message.equals("image change success")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                Ion.with(imageView)
                                        .centerCrop()
                                        .resize(250, 250)
                                        .load(imageUrl);

                                progressDialog.dismiss();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                } else {
                    // Permission denied - Show a message to inform the user that this app only works
                    // with these permissions granted
                }
                return;
            }
        }
    }


    public void closeMenu() {

        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(mContext, R.anim.siderbar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewLayout.setVisibility(View.GONE);
                viewLayout.setEnabled(false);
                mainLayout.setEnabled(true);
            }
        }, 450);
    }

    public void showMenu() {

        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                showMenu();
                break;
            case R.id.btn_search :
                Intent intent2 = new Intent(getApplicationContext(), SearchPlanActivity.class);
                startActivity(intent2);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isMenuShow) {
            closeMenu();
        } else {

            if (isExitFlag) {
                finish();
            } else {

                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitFlag = false;
                    }
                }, 2000);
            }
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode,resultCode,data);
//
//        if(requestCode == CodeNumber.TO_REGISTERPLAN_ACTIVITY) {
//            if(resultCode == RESULT_OK) {
//                //String strfirstDate = data.getStringExtra("start");
//                //String strlastDate = data.getStringExtra("last");
//                //map.put(strfirstDate,strlastDate);
//               // Log.d("시작날짜 key", strfirstDate);
//                //Log.d("종료날짜 value", map.get(strfirstDate));
//                new ApiSimulator(map).executeOnExecutor(Executors.newSingleThreadExecutor());
//            }
//        }
//    }


    public void showAllSche() {

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this).getConscryptMiddleware().enable(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("잠시만 기다려주세요. 해당 일정을 가져오는 중 입니다~");
        progressDialog.show();

        JsonObject json = new JsonObject();

        //////////////////////////////////////////////////////////////////////
        json.addProperty("cid", cid);
        //////////////////////////////////////////////////////////////////////


        Future ion = Ion.with(getApplicationContext())
                .load("POST", url.getServerUrl() + "/showAllSche")
                .setHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if( e!= null) {
                            Toast.makeText(getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                        }

                        else {
                            progressDialog.dismiss();

                            String message = result.get("message").getAsString();
                            if(message.equals("success")) {
                                JsonArray data = result.get("data").getAsJsonArray();
                                //scheduleData = new ScheduleData[data.size()];
                                //schedList = new ArrayList<>();

                                Log.i("들어있는 일정 개수", Integer.toString(data.size()));

                                String[] dateTime;
                                for(int i = 0; i< data.size(); i++) {
                                    JsonObject sched_data = data.get(i).getAsJsonObject();

                                    ScheduleData scheduleData = new ScheduleData();

                                    scheduleData.sche_title = sched_data.get("title").getAsString();
                                    scheduleData.cid = sched_data.get("cid").getAsInt();
                                    scheduleData.sid = sched_data.get("sid").getAsInt();
                                    scheduleData.sche_content = sched_data.get("sContent").getAsString();

                                    String startDateTime = sched_data.get("startDate").getAsString();
                                    dateTime = parseDateAndTime(startDateTime);
                                    scheduleData.startDate = dateTime[0];
                                    scheduleData.startTime = dateTime[1];

                                    String endDateTime = sched_data.get("endDate").getAsString();
                                    dateTime = parseDateAndTime(endDateTime);
                                    scheduleData.endDate = dateTime[0];
                                    scheduleData.endTime = dateTime[1];

                                    scheduleData.area = sched_data.get("area").getAsString();
                                    scheduleData.numberofComment = sched_data.get("numOfComments").getAsInt();

                                    schedList.add(scheduleData);

//                                    Log.i("title", sche_title);
//                                    Log.i("cid", Integer.toString(cid));
//                                    Log.i("sid", Integer.toString(sid));
//                                    Log.i("sche_content", sche_content);
//                                    Log.i("startDate", startDate);
//                                    Log.i("endDate", endDate);
//                                    Log.i("area", area);
//                                    Log.i("# of comments", Integer.toString(numberofComment));

                                }

                            }

                            else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    public String[] parseDateAndTime(String date) {

                        String[] parseData = date.split(" ");
                        String[] result = new String[parseData.length];

                        for(int i = 0; i<parseData.length; i++) {
                            Log.i("들어있는 값", parseData[i]);
                        }
                        result[0] = parseData[0].trim();
                        result[1] = parseData[1].substring(0, 5).trim();

                        return result;
                    }
                });

        try {
            ion.get();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}



