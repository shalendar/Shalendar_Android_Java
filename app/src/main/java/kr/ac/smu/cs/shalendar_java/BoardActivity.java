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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static kr.ac.smu.cs.shalendar_java.CodeNumber.PICK_IMAGE_REQUEST;

/*
  등록된 일정에 대하여
  게시판 형식으로 보여주는 Activity.
  일정 item을 누르면 PlanDetailActivity로 넘어간다.
 */

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonToPlanDtail;
    private ScrollView scrollView;
    private BoarderAdapter b_adapter;

    //서버 통신 위한 url 객체 생성  여기서는 /signin
    private NetWorkUrl url = new NetWorkUrl();

    //서버로 부터 로그인 성공 시 오는 응답 Token 변수
    private String userToken;

    //서버로 부터 로그인 실패 시 오는 응답 변수
    private String responseFromServer;

    private String imageURL;

    private ImageView imageView;

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

        //JS
        init();

        //통신코드

        addSideView();  //사이드바 add

        final RecyclerView boardRecyclerView = findViewById(R.id.BoarderRecyclerView);
        //레이아웃 매니져가 null값을 받는다 이유는?
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boardRecyclerView.setLayoutManager(linearLayoutManager);

        //통신코드 시작(initBoard)
        checkPermissions();
        //통신 준비
        Ion.getDefault(getApplicationContext()).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(getApplicationContext()).getConscryptMiddleware().enable(false);

        final JsonObject json = new JsonObject();

        json.addProperty("cid", 20);

//        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("잠시만 기다려주세요. 게시판화면 불러오는중~");
//        progressDialog.show();

        Ion.with(getApplicationContext())
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

                            /*
                            //여기서부터 SharedPreference써본다잉
                            SharedPreferences sharedPnum = getSharedPreferences("Peoplenum", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPnum.edit();
                            editor.putInt("Peoplenum",sharePeopleNum);
                            editor.apply();
                            */

                            Log.i("요기요1", Integer.toString(sharePeopleNum));

                            //b_adapter.setSharedNumber(sharePeopleNum);

                            //서버로 부터 응답 메세지가 success이면...

                            if (message.equals("success")) {
                                //서버 응답 오면 로딩 창 해제
                                //progressDialog.dismiss();

                                //shareuserdata: {} 에서 {}안에 있는 것들도 JsonObject
                                JsonArray sharedUserData = result.get("shareUserData").getAsJsonArray();

                                /*
                                for (int i = 0; i < sharePeopleNum; i++) {
                                    JsonObject jsonArr = sharedUserData.get(i).getAsJsonObject();
                                    id = jsonArr.get("id").getAsString();
                                    //pw=jsonArr.get("pw").getAsString();

                                }
                                */

                                //calendardata: {} 에서 {}안에 있는 것들도 JsonObject
                                JsonObject calendarData = result.get("calendarData").getAsJsonObject();
                                //calName = calendarData.get("calName").getAsString();
                                //calContent = calendarData.get("calContent").getAsString();
                                //userCount=calendarData.get("userCount").getAsInt();

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

                                    b_adapter.addItem(new BoardPlanItem(planDate, title, area, numOfCommentsstring));

                                }

                                b_adapter.notifyDataSetChanged();
                                // boardRecyclerView.setAdapter(b_adapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


        Log.i("누가 먼저 실행되는 거임??333", "BoardActivity Ion 통신 끝");
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

