package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/*

  일정 상세보기 Activity
  app bar의 메뉴에서 '일정 수정', '일정 삭제' 선택시
  UpdatePlan, DeletePlanActivity로 각각 넘어간다.
 */
public class PlanDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button buttonToUpdate;
    private Button buttonToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

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

        //리스트뷰
        ListView plandetail_Listview =(ListView) findViewById(R.id.planDetail_listView);
        //헤더 삽입
        View header = getLayoutInflater().inflate(R.layout.activity_plandetailreply_header, null, false);
        plandetail_Listview.addHeaderView(header);

        final PlandetailAdapter plandetailAdapter = new PlandetailAdapter();

        plandetailAdapter.addItem(new PlandetailItem("박성준","5월9일","댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준","5월9일","댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준","5월9일","댓글입니다"));
        plandetailAdapter.addItem(new PlandetailItem("박성준","5월9일","댓글입니다"));

        plandetail_Listview.setAdapter(plandetailAdapter);

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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
    }
}
