package kr.ac.smu.cs.shalendar_java;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static kr.ac.smu.cs.shalendar_java.CodeNumber.PICK_IMAGE_REQUEST;

public class WaitInvite extends AppCompatActivity implements View.OnClickListener {

    ArrayList<WaitListItem> waitRecyclerList;
    private Context mContext = WaitInvite.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역
    private ViewGroup calendarLayout; //달력레이아웃 부분
    private String imageURL;
    private ImageView imageView;
    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    //
    private String userToken;

    //서버 통신
    NetWorkUrl url = new NetWorkUrl();

    //

    private String senderID;
    private String receiver;
    private String senderName;
    private String sender_img;
    private int cid;
    private String calName;

    //
    private WaitlistAdapter w_adapter;
    private RecyclerView waitInviteRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_invite);
        checkPermissions();
        waitRecyclerList=new ArrayList<>();

        waitInviteRecyclerView = (RecyclerView)findViewById(R.id.waitListRecycler);
        waitInviteRecyclerView.setHasFixedSize(true);
        w_adapter = new WaitlistAdapter(waitRecyclerList, this);
        waitInviteRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));



        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", MODE_PRIVATE);
        //값이 없으면 default로 0
        userToken = pref.getString("userToken", "NO_TOKEN");
        Log.i("초대 대기 화면 :: 사용자 토큰", userToken);


        //통신 준비.
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this).getConscryptMiddleware().enable(false);

        JsonObject json = new JsonObject();
        json.addProperty("", "");

        final ProgressDialog progressDialog = new ProgressDialog(WaitInvite.this);
        progressDialog.setMessage("초대 리스트 불러오는 중 입니다~");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        Future ion = Ion.with(getApplicationContext())
                .load("POST", url.getServerUrl() + "/showInvitation")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", userToken)
                .setTimeout(60000)
                .progressDialog(progressDialog)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            String message = result.get("message").getAsString();

                            if(message.equals("success")) {
                                parseDataFromServer(result);
                            } else if(message.equals("nothing")) {
                                Toast.makeText(getApplicationContext(), "초대 받은 내역이 없습니다.", Toast.LENGTH_LONG).show();
                            } else if(message.equals("fail")) {
                                Toast.makeText(getApplicationContext(), "초대 중 에러가 발생하였습니다.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "잘못된 응답 메세지 입니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                });

        //응답 받아올 때 까지 대기.
        try {
            ion.get();
            progressDialog.dismiss();
        } catch(Exception e){
            e.printStackTrace();
        }


        w_adapter.setOnItemClickListener(new WaitlistAdapter.OnItemClickListener() {

            private int ACCEPT_FLAG;

            @Override
            public void onAcceptClick(View v, int pos) {
                //Toast.makeText(getApplicationContext(),
                // waitRecyclerList.get(pos).calendarName + Integer.toString(waitRecyclerList.get(pos).cid), Toast.LENGTH_LONG).show();
                ACCEPT_FLAG = 1;
                String senderEmail = waitRecyclerList.get(pos).emailID;
                String receiver = waitRecyclerList.get(pos).invitedName;
                int cid = waitRecyclerList.get(pos).cid;

                Log.i("SenderEmail", senderEmail);
                Log.i("receiver", receiver);
                Log.i("FLAG", Integer.toString(ACCEPT_FLAG));
                Log.i("cid", Integer.toString(cid));

                sendToServer(ACCEPT_FLAG, senderEmail, receiver, cid);
            }

            @Override
            public void onRejectClick(View v, int pos) {
                ACCEPT_FLAG = 0;
                String senderEmail = waitRecyclerList.get(pos).emailID;
                String receiver = waitRecyclerList.get(pos).invitedName;
                int cid = waitRecyclerList.get(pos).cid;
                sendToServer(ACCEPT_FLAG, senderEmail, receiver, cid);

                Log.i("SenderEmail", senderEmail);
                Log.i("receiver", receiver);
                Log.i("FLAG", Integer.toString(ACCEPT_FLAG));
                Log.i("cid", Integer.toString(cid));

            }
        });
    }



    public void parseDataFromServer(JsonObject result) {

        JsonArray invitation = result.get("invitation").getAsJsonArray();

        if(invitation.size() == 0)
            Toast.makeText(getApplicationContext(), "초대 받은 내역이 없습니다.", Toast.LENGTH_LONG).show();
        else {
            for(int i = 0; i<invitation.size(); i++) {
                JsonObject invitationData = invitation.get(i).getAsJsonObject();
                senderID = invitationData.get("sender").getAsString();
                receiver = invitationData.get("receiver").getAsString();
                senderName = invitationData.get("senderName").getAsString();
                sender_img = invitationData.get("sender_img").getAsString();
                cid = invitationData.get("cid").getAsInt();
                calName = invitationData.get("cName").getAsString();
                w_adapter.addItem(new WaitListItem(sender_img, senderID, calName, receiver, senderName, cid));
            }
            waitInviteRecyclerView.setAdapter(w_adapter);
        }
        //progressDialog.dismiss();
    }

    public void sendToServer(int ACCEPT_FLAG, String senderEmail, String receiver, int cid) {

        JsonObject json = new JsonObject();
        json.addProperty("flag", ACCEPT_FLAG);
        json.addProperty("sender", senderEmail);
        json.addProperty("receiver", receiver);
        json.addProperty("cid", cid);

        Ion.with(getApplicationContext())
                .load("POST", url.getServerUrl() + "/acceptInvitation")
                .setHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            String message = result.get("message").getAsString();
                            if(message.equals("accept")) {
                                Toast.makeText(getApplicationContext(), "초대 수락 완료", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(WaitInvite.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (message.equals("reject")) {
                                Toast.makeText(getApplicationContext(), "초대 거절", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "초대 수락 실패", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
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
}
