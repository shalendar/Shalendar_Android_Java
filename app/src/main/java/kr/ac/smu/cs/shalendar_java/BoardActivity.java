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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
public class BoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button buttonToPlanDtail;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

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

        //buttonToPlanDtail = (Button)findViewById(R.id.board_toPlanDetail_button);

        /*
          버튼 누르면 'PlanDetailActivity로 넘어간다.

        buttonToPlanDtail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PlanDetailActivity.class);
                startActivityForResult(intent, CodeNumber.TO_PLANDETAIL_ACTIVITY);
            }
        });
        */

        //리스트뷰

        ListView boardListview =(ListView) findViewById(R.id.boardListView);
        //헤더 삽입
        View header = getLayoutInflater().inflate(R.layout.activity_boardheader, null, false);
        boardListview.addHeaderView(header);

        final BoardPlanAdapter planAdapter = new BoardPlanAdapter();
        planAdapter.addItem(new BoardPlanItem("5월 1일 12시 - 5월 2일 14시","치킨먹기","소대공학관","3"));
        planAdapter.addItem(new BoardPlanItem("5월 2일 14시 - 5월 2일 17시","짜장면먹기","소대공학관","33"));
        planAdapter.addItem(new BoardPlanItem("5월 2일 18시 - 5월 2일 21시","봉구스먹기","소대공학관","23"));
        planAdapter.addItem(new BoardPlanItem("5월 3일 3시 - 5월 3일 10시","학식먹기","소대공학관","43"));
        planAdapter.addItem(new BoardPlanItem("5월 4일 6시 - 5월 4일 11시","치킨먹기","소대공학관","23"));

        boardListview.setAdapter(planAdapter);

        boardListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoardPlanItem item = (BoardPlanItem) planAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된것 : "+item.getPlanname(), Toast.LENGTH_LONG).show();
            }
        });


    }

    class BoardPlanAdapter extends BaseAdapter{

        ArrayList<BoardPlanItem> items = new ArrayList<BoardPlanItem>();

        public void addItem(BoardPlanItem item){
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BoardPlanItemView view = null;
            if(convertView == null) {
                view = new BoardPlanItemView(getApplicationContext());
            }
            else{
                view = (BoardPlanItemView) convertView;
            }

            BoardPlanItem item = items.get(position);
            view.setDateandtimetext(item.dateandtime);
            view.setLocationtext(item.location);
            view.setPlannametext(item.planname);
            view.setReplynumtext(item.replynum);

            return view;
        }
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
