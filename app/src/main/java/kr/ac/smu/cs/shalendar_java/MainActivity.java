package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //
    ArrayList<MainPlanItem> mainRecyclerList;

    private TextView textViewTitle;
    private Button buttonToBoard;
    private Button buttonToRegisterPlan;

    //SharedPreferences 변수
    //private SharedPreferences pref;

    //UserToken
    private String userToken;

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

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    //materialCalendar
    String time, kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;
    Cursor cursor;

    //사용자 지정 날짜 가지는 자료구조.
    HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreference에 저장된 userToken가져오기.
        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", MODE_PRIVATE);
        //값이 없으면 default로 0
        userToken = pref.getString("userToken", "NO_TOKEN");
        Log.i("넘겨받은 토큰", userToken);


       // textViewTitle = (TextView) findViewById(R.id.main_title_textView);
        buttonToBoard = (Button) findViewById(R.id.main_toBoard_button);
        final TextView selectedDate = (TextView)findViewById(R.id.TextView1);
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

        insertData();

        RecyclerView mainRecyclerView = (RecyclerView)findViewById(R.id.mainRecyclerview);
        mainRecyclerView.setHasFixedSize(true);
        MainPlanAdapter m_adapter = new MainPlanAdapter(mainRecyclerList,this);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        mainRecyclerView.setAdapter(m_adapter);

        //JS
        init();

        addSideView();  //사이드바 add


        //materialCalendar
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


        //dummy data test용
//        String[] result = {"2019-07-01", "2019-07-31", "2019-07-22"};
//        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "-" + Month + "-" + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();


                ///////애니메이션 구현
                if (isPageOpen) {
                    calendarRelative.setClickable(false);
                    main_animation.startAnimation(translateDownAnim);

                } else {
                    main_animation.setVisibility(View.VISIBLE);
                    main_animation.startAnimation(translateUpAnim);
                }

                Toast.makeText(getApplicationContext(), shot_Day , Toast.LENGTH_SHORT).show();

                //TextView에 삽입
                selectedDate.setText(shot_Day);
            }
        });


        /*
        //툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        //플로팅액션버튼
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/


        /*//드로워
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //내비게이션뷰
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */


        /*
          MainActivity에서 BoardActivity로 넘어간다.
        */
        buttonToBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivityForResult(intent, CodeNumber.TO_BOARD_ACTIVITY);
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


    public void insertData(){
        for(int i=0; i<=10; i++) {
            MainPlanItem mitem = new MainPlanItem();
            mitem.setMainPlanname("프로젝트"+(i+1));
            mitem.setMainPlantime("2014-02-01");

            ArrayList<MainPlanTeamIteam> mtItem = new ArrayList<>();
            for(int j=0; j<6; j++){
                mtItem.add(new MainPlanTeamIteam(R.drawable.face));
            }
            mitem.setTeamPicList(mtItem);
            mainRecyclerList.add(mitem);
        }
    };


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
        //String[] Time_Result;
        HashMap<String, String> Time_Result;
        /*
        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }
        */

        ApiSimulator(HashMap<String, String> Time_Result) {
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

            /*
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
            */
            //map에 있는 data 처리 for문
            for(String key : map.keySet()) {
                Log.d("시작날짜 key", key);
                Log.d("종료날짜 value", map.get(key));
                //시작 날짜, 종료날짜 같은 경우.
                if(key.equals(map.get(key))) {
                    String[] time = key.split("-");
                    int year = Integer.parseInt(time[0]);
                    int month = Integer.parseInt(time[1]);
                    int dayy = Integer.parseInt(time[2]);
                    Log.d("쪼갠 날짜", Integer.toString(year) + "," + Integer.toString(month) + "," + Integer.toString(dayy));
                    calendar.set(year, month-1, dayy);
                    CalendarDay day = CalendarDay.from(calendar);
                    dates.add(day);
                }

                //시작 날짜, 종료날짜 다른 경우

            }

            //dates ArrayList에 사용자 지정 날짜들이 들어있다. 2019-7-8형식.
            Log.d("들어있는 Date", dates.toString());
            return dates;
        }

        protected void setEventList() {

        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            HashMap<HashSet<CalendarDay>, Integer> eventMap = new HashMap<>();

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

            HashSet<CalendarDay> dateSet = event1.getDates();


//            materialCalendarView.addDecorator(new EventDecorator(1, Color.parseColor("#ff6067"), calendarDays, MainActivity.this));
//            materialCalendarView.addDecorator(new EventDecorator(2, Color.parseColor("#f8c930"), calendarDays, MainActivity.this));
//            materialCalendarView.addDecorator(new EventDecorator(3, Color.parseColor("#cdcdcd"), calendarDays, MainActivity.this));
        }
    }

    //Test Code이다.
    /*
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(1,Color.parseColor("#ff6067"), calendarDays, MainActivity.this));
            materialCalendarView.addDecorator(new EventDecorator(2,Color.parseColor("#f8c930"), calendarDays, MainActivity.this));
            materialCalendarView.addDecorator(new EventDecorator(3,Color.parseColor("#cdcdcd"), calendarDays, MainActivity.this));
        }
    }
    */

    private void init() {

        findViewById(R.id.btn_menu).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);

        mainLayout = findViewById(R.id.id_main);
        viewLayout = findViewById(R.id.fl_silde);
        sideLayout = findViewById(R.id.view_sildebar);

    }

    private void addSideView() {

        Sidebar sidebar = new Sidebar(mContext);
        sideLayout.addView(sidebar);

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


        });
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
                startActivityForResult(intent2, CodeNumber.TO_SEARCH_PLAN_ACTIVITY);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CodeNumber.TO_REGISTERPLAN_ACTIVITY) {
            if(resultCode == RESULT_OK) {
                //String strfirstDate = data.getStringExtra("start");
                //String strlastDate = data.getStringExtra("last");
                //map.put(strfirstDate,strlastDate);
               // Log.d("시작날짜 key", strfirstDate);
                //Log.d("종료날짜 value", map.get(strfirstDate));
                new ApiSimulator(map).executeOnExecutor(Executors.newSingleThreadExecutor());
            }
        }
    }

}
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.toolbar_search){
            Intent intent = new Intent(getApplicationContext(), SearchPlanActivity.class );
            startActivityForResult(intent, CodeNumber.TO_SEARCH_PLAN_ACTIVITY);
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.toNotice_item) {
            Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
            startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);

        } else if (id == R.id.toInviteMember_item) {
            Intent intent = new Intent(getApplicationContext(), InviteActivity.class);
            startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);

        } else if (id == R.id.toMakeCalendar_item) {
            Intent intent = new Intent(getApplicationContext(), CreateCalendarActivity.class);
            startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);

        } else if (id == R.id.toSetting_item) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/



