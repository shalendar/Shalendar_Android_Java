package kr.ac.smu.cs.shalendar_java;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
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
        //서버 통신코드 1 AsnychTask사용
        //new initBoardTask(BoardActivity.this).execute(url.getServerUrl() + "/initBoard");

        //통신 준비
        Ion.getDefault(getApplicationContext()).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(getApplicationContext()).getConscryptMiddleware().enable(false);


        addSideView();  //사이드바 add

        RecyclerView boardRecyclerView = findViewById(R.id.BoarderRecyclerView);
        //레이아웃 매니져가 null값을 받는다 이유는?
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boardRecyclerView.setLayoutManager(linearLayoutManager);

        //통신코드 시작(initBoard)

        final JsonObject json = new JsonObject();

        json.addProperty("cid", 1);

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
                .setCallback(new FutureCallback<JsonObject>(){
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //받을 변수
                        String datetime, planname, location, replynum;
                        String id, pw, userName, img_url;

                        if(e != null) {
                            Toast.makeText(getApplicationContext(), "Server Connection Error!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            //응답 형식이 { "data":{"id":"jacob456@hanmail.net", "cid":1, "sid":10, "title":"korea"}, "message":"success"}
                            //data: 다음에 나오는 것들도 JsonObject형식.
                            //따라서 data를 JsonObject로 받고, 다시 이 data를 이용하여(어찌보면 JsonObject안에 또다른 JsonObject가 있는 것이다.
                            //JSONArray가 아님. 얘는 [,]로 묶여 있어야 함.

                            String message = result.get("message").getAsString();
                            int sharePeopleNum =result.get("sharePeopleNum").getAsInt();
                            //서버로 부터 응답 메세지가 success이면...

                            if(message.equals("success")) {
                                //서버 응답 오면 로딩 창 해제
                                //progressDialog.dismiss();

                                //data: {} 에서 {}안에 있는 것들도 JsonObject
                                JsonArray sharedUserData = result.get("shareUserData").getAsJsonArray();

                                for(int i=0; i<sharePeopleNum; i++) {
                                    JsonObject jsonArr = sharedUserData.get(i).getAsJsonObject();
                                    id=jsonArr.get("id").getAsString();
                                    Log.i("공유 넘버", Integer.toString(sharePeopleNum));
                                    Log.i("id는 이거다잉",id);
                                }



                            } else {

                                Toast.makeText(getApplicationContext(), "해당 일정이 없습니다.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });


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

    //보더초기화 통신
    //로그인 통신 코드 1. AsynchTask사용.
    /*
    public class initBoardTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        public initBoardTask(Context context) {
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려주세요. 로그 인 중 입니다~");
            progressDialog.show();

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("doInBackground 확인", "doIn");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.


//                for (int i = 0; i < 5; i++) {
//                    progressDialog.setProgress(i * 30);
//                    Thread.sleep(500);
//
//                }


                JSONObject jsonObject = new JSONObject();

                //임시 cid
                int cid = 1;
                jsonObject.put("cid", cid);
                //캘린더 id


                Log.d("들어갔는지 확인", "jsonOk??");


                HttpURLConnection con = null;
                BufferedReader reader = null;


                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    //요청 방식 선택(POST or GET)
                    con.setRequestMethod("POST");
                    //Request Header값 Setting(data를 key, value형식으로 보낼 수 있음)
//                    con.setRequestProperty("Autorization", Integer.toString(userToken));
                    con.setRequestProperty("Cache-Control", "no-cache");
                    //RequestBody전달 시 application/json형식으로 서버에 전달.
                    con.setRequestProperty("Content-Type", "application/json");

                    //OutputStream으로 POST data를 넘겨주겠다는 옵현
                    con.setDoOutput(true);
                    //InputStrean으로 서버로 부터 응답을 받겠다는 옵션
                    con.setDoInput(true);

                    con.connect();

                    Log.d("con 연결1", "");

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    Log.d("con 연결2", "");

                    //서버로 부터 데이터를 InputStream으로 받겠다!
                    InputStream stream = con.getInputStream();

                    Log.d("con연결3 데이터받음", "");

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.d("들어오는 pid", result);//서버로 부터 받은 값을 출력해주는 부분
            //dialog창 닫기
            progressDialog.dismiss();

            if (result == null) return;

            try {

                //파서로 먼저받기
                JSONArray jsonArray = new JSONArray(result);

                //int i=0;
                //JSONObject jsonObject = jsonArray.getJSONObject(i);


                for(int i=0; i<jsonArray.length(); i++){
                    org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                }

                //responseFromServer = (String) jsonObject.get("message");
                //userToken = (String) jsonObject.get("token");


                Log.d("서버응답", responseFromServer);
                Toast.makeText(getApplicationContext(), responseFromServer, Toast.LENGTH_LONG).show();


                //서버로 부터 success 응답 받으면 메인 화면으로 넘어간다.
                if (responseFromServer.equals("login success")) {

                    SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userToken", userToken);
                    editor.apply();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    */

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
