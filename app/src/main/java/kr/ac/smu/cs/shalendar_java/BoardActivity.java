package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
  등록된 일정에 대하여
  게시판 형식으로 보여주는 Activity.
  일정 item을 누르면 PlanDetailActivity로 넘어간다.
 */
public class BoardActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonToPlanDtail;
    private ScrollView scrollView;
    private BoarderAdapter b_adapter;

    //js
    private Context mContext = BoardActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //JS
        init();

        addSideView();  //사이드바 add

        RecyclerView boardRecyclerView = findViewById(R.id.BoarderRecyclerView);
        //레이아웃 매니져가 null값을 받는다 이유는?
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boardRecyclerView.setLayoutManager(linearLayoutManager);

        b_adapter = new BoarderAdapter();

        boardRecyclerView.setAdapter(b_adapter);

        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트1", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트2", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트3", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트4", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트5", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트6", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트7", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트8", "G207", "2"));
        b_adapter.addItem(new BoardPlanItem("2019년", "졸업프로젝트9", "G207", "2"));

        b_adapter.notifyDataSetChanged();
    }




    private void init(){

        findViewById(R.id.btn_menu).setOnClickListener(this);

        mainLayout = findViewById(R.id.id_main);
        viewLayout = findViewById(R.id.fl_silde);
        sideLayout = findViewById(R.id.view_sildebar);

    }

    private void addSideView(){

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

    public void closeMenu(){

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

    public void showMenu(){

        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_menu :
                showMenu();
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
        if(isMenuShow){
            closeMenu();
        }else{

            if(isExitFlag){
                finish();
            } else {

                isExitFlag = true;
                //Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.",  Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitFlag = false;
                    }
                }, 2000);
            }
        }
    }
}




        /*
        //앱바(툴바)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //드로워
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //내비게이션뷰
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        buttonToPlanDtail = (Button)findViewById(R.id.board_toPlanDetail_button);


          버튼 누르면 'PlanDetailActivity로 넘어간다.

        buttonToPlanDtail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PlanDetailActivity.class);
                startActivityForResult(intent, CodeNumber.TO_PLANDETAIL_ACTIVITY);
            }
        });
        */

        /*
        //리사이클링뷰가 보더레이아웃이 아니라 컨텐츠보더에 있으니까 인플레이터 이용해서 부른다
        View inflatedView = getLayoutInflater().inflate(R.layout.activity_boardheader, null);
        RecyclerView memberrecyclerview = inflatedView.findViewById(R.id.teammemberRecyclerview);
        */

          /*
        //리스트뷰
        ListView boardListview =(ListView) findViewById(R.id.boardListView);
        //헤더 삽입
        View header = getLayoutInflater().inflate(R.layout.activity_boardheader, null, false);
        boardListview.addHeaderView(header);

        final BoardPlanAdapter planAdapter = new BoardPlanAdapter();
        planAdapter.addItem(new BoardPlanItem("5월 1일 12시 - 5월 1일 14시","치킨먹기","소대공학관","3"));
        planAdapter.addItem(new BoardPlanItem("5월 2일 14시 - 5월 2일 17시","짜장면먹기","소대공학관","33"));
        planAdapter.addItem(new BoardPlanItem("5월 2일 18시 - 5월 2일 21시","봉구스먹기","소대공학관","23"));

        boardListview.setAdapter(planAdapter);

        boardListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoardPlanItem item = (BoardPlanItem) planAdapter.getItem(position-1);
                Toast.makeText(getApplicationContext(), "선택된것 : "+item.getPlanname(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), PlanDetailActivity.class);
                startActivityForResult(intent, CodeNumber.TO_PLANDETAIL_ACTIVITY);
            }
        });
        */
