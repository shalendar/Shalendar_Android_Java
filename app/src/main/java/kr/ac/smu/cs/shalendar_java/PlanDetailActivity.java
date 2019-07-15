package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/*
  일정 상세보기 Activity
  app bar의 메뉴에서 '일정 수정', '일정 삭제' 선택시
  UpdatePlan, DeletePlanActivity로 각각 넘어간다.
 */
public class PlanDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonToUpdate;
    private Button buttonToDelete;

    //js
    private Context mContext = PlanDetailActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        //JS
        init();

        addSideView();  //사이드바 add

        //리스트뷰
        ListView plandetail_Listview = (ListView) findViewById(R.id.planDetail_listView);
        //헤더 삽입
        View header = getLayoutInflater().inflate(R.layout.activity_plandetailreply_header, null, false);
        plandetail_Listview.addHeaderView(header);

        final PlandetailAdapter plandetailAdapter = new PlandetailAdapter();

        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준", "5월9일", "댓글입니다"));

        plandetail_Listview.setAdapter(plandetailAdapter);


        //길게 눌렀을 때
        plandetail_Listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("댓글 수정/삭제");

                dialog.setMessage("댓글 수정, 삭제하십니까?")
                        .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                return false;
            }
        });
    }


    private void init() {

        findViewById(R.id.btn_menu).setOnClickListener(this);

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
                closeMenu();
            }

            @Override
            public void btnLevel2() {
                closeMenu();
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
         일정 수정 화면으로 이동

        buttonToUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdatePlanActivity.class);
                startActivityForResult(intent, CodeNumber.TO_UPDATEPLAN_ACTIVITY);
            }
        });
        */

          /*
         일정 삭제 화면으로 이동

        buttonToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeletePlanActivity.class);
                startActivityForResult(intent, CodeNumber.TO_DELETEPLAN_ACTIVITY);
            }
        });
        */


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

        //buttonToUpdate = findViewById(R.id.planDetail_toUpdate_button);
        //buttonToDelete = findViewById(R.id.planDetail_toDelete_button);
        */
