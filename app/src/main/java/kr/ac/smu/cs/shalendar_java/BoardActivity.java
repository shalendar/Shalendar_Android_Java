package kr.ac.smu.cs.shalendar_java;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Future;

import static kr.ac.smu.cs.shalendar_java.CodeNumber.PICK_IMAGE_REQUEST;

/*
  등록된 일정에 대하여
  게시판 형식으로 보여주는 Activity.
  일정 item을 누르면 PlanDetailActivity로 넘어간다.
 */

public class BoardActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Button buttonToPlanDtail;
    private ScrollView scrollView;
    private BoarderAdapter b_adapter;
    private Button buttonToHome;
    private Button buttonToRegisterPlan;

    //서버 통신 위한 url 객체 생성  여기서는 /signin
    private NetWorkUrl url = new NetWorkUrl();

    //서버로 부터 로그인 성공 시 오는 응답 Token 변수
    private String userToken;

    //서버로 부터 로그인 실패 시 오는 응답 변수
    private String responseFromServer;

    private String imageURL;

    private ImageView imageView;

    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침

    int sharePeopleNum;

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainswipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        buttonToHome = (Button) findViewById(R.id.button_home);
        buttonToRegisterPlan = (Button) findViewById(R.id.main_ToRegister_button);

        buttonToRegisterPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Intent intent = new Intent(getApplicationContext(), RegisterPlanActivity.class);
                startActivityForResult(intent, CodeNumber.TO_REGISTERPLAN_ACTIVITY);
            }
        });


        buttonToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);
            }
        });


        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", MODE_PRIVATE);
        userToken = pref.getString("userToken", "NO_TOKEN");
        //Log.i("Board화면::넘겨받은 토큰", userToken);

        //JS
        init();

        //통신코드

        addSideView();  //사이드바 add

        RecyclerView boardRecyclerView = findViewById(R.id.BoarderRecyclerView);
        //레이아웃 매니져가 null값을 받는다 이유는?
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boardRecyclerView.setLayoutManager(linearLayoutManager);

        //통신코드 시작(initBoard)
        checkPermissions();
        //통신 준비
        Ion.getDefault(getApplicationContext()).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(getApplicationContext()).getConscryptMiddleware().enable(false);

        onRefresh();


        //Log.i("누가 먼저 실행되는 거임??333", "BoardActivity Ion 통신 끝");
    }

    //새로고침


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //b_adapter.notifyDataSetChanged();
                final RecyclerView boardRecyclerView = findViewById(R.id.BoarderRecyclerView);
                final JsonObject json = new JsonObject();

                json.addProperty("cid", MainActivity.cid);
                Log.i("게시판 넘어온 cid", Integer.toString(MainActivity.cid));


                Future ion = Ion.with(getApplicationContext())
                        .load("POST", url.getServerUrl() + "/initBoard")
                        .setHeader("Content-Type", "application/json")
                        //.progressDialog(progressDialog)
                        .setJsonObjectBody(json)
                        .asJsonObject() //응답
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                //받을 변수
                                String datetime, planname, location, replynum;
                                String id, pw, userName, img_url, calName, calContent;
                                int cid, sid, numOfComments, userCount;
                                String title, sContent, startDate, endDate, area, planDate;


                                if (e != null) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                } else {
                                    //응답 형식이 { "data":{"id":"jacob456@hanmail.net", "cid":1, "sid":10, "title":"korea"}, "message":"success"}
                                    //data: 다음에 나오는 것들도 JsonObject형식.
                                    //따라서 data를 JsonObject로 받고, 다시 이 data를 이용하여(어찌보면 JsonObject안에 또다른 JsonObject가 있는 것이다.
                                    //JSONArray가 아님. 얘는 [,]로 묶여 있어야 함.

                                    String message = result.get("message").getAsString();
                                    sharePeopleNum = result.get("sharePeopleNum").getAsInt();


                                    Log.i("요기요1", Integer.toString(sharePeopleNum));

                                    //서버로 부터 응답 메세지가 success이면...

                                    if (message.equals("success")) {
                                        //서버 응답 오면 로딩 창 해제
                                        //progressDialog.dismiss();

                                        //shareuserdata: {} 에서 {}안에 있는 것들도 JsonObject
                                        JsonArray sharedUserData = result.get("shareUserData").getAsJsonArray();



                                        //calendardata: {} 에서 {}안에 있는 것들도 JsonObject
                                        JsonObject calendarData = result.get("calendarData").getAsJsonObject();
                                        b_adapter = new BoarderAdapter(sharePeopleNum, sharedUserData, calendarData);
                                        boardRecyclerView.setAdapter(b_adapter);


                                        //scheduleData: {} 에서 {}안에 있는 것들도 JsonObject
                                        JsonArray scheduleData = result.get("scheduleData").getAsJsonArray();
                                        //문제없음

                                        for (int i = 0; i < scheduleData.size(); i++) {
                                            JsonObject jsonArr1 = scheduleData.get(i).getAsJsonObject();
                                            cid = jsonArr1.get("cid").getAsInt();
                                            sid = jsonArr1.get("sid").getAsInt();
                                            title = jsonArr1.get("title").getAsString();
                                            sContent = jsonArr1.get("sContent").getAsString();
                                            startDate = jsonArr1.get("startDate").getAsString();
                                            endDate = jsonArr1.get("endDate").getAsString();
                                            area = jsonArr1.get("area").getAsString();
                                            numOfComments = jsonArr1.get("numOfComments").getAsInt();
                                            String numOfCommentsstring = Integer.toString(numOfComments);
                                            //date 형식에 맞춰 잘라내기
                                            startDate = startDate.substring(0,16);
                                            endDate = endDate.substring(0,16);
                                            //plan의 일자
                                            planDate = startDate+" ~ "+endDate;

                                            b_adapter.addItem(new BoardPlanItem(planDate, title, area, numOfCommentsstring, sid));


                                            boardRecyclerView.setAdapter(b_adapter);
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                try {
                    ion.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }
        },1000);
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
                Intent intent3 = new Intent(getApplicationContext(), WaitInvite.class);
                startActivityForResult(intent3, CodeNumber.TO_CREATE_CALENDAR_ACTIVITY);
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

    public void setUserProfileImage_Server(final String imageUrl) {

        Log.i("프로필 변경", userToken);
        File file = new File(imageUrl);

        final ProgressDialog progressDialog = new ProgressDialog(BoardActivity.this);
        progressDialog.setMessage("프로필 사진 등록 중 입니다~");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        Future ion = Ion.with(this)
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
                            }
                            else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
        try {
            ion.get();
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

