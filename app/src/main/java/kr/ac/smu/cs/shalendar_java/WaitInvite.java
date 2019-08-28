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
import android.widget.ImageButton;
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

public class WaitInvite extends AppCompatActivity {


    ArrayList<WaitListItem> waitRecyclerList;
    private Context mContext = WaitInvite.this;
    private String imageURL;
    private ImageView imageView;
    private Boolean isExitFlag = false;
    ImageButton backButton;

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
        waitRecyclerList=new ArrayList<>();

        waitInviteRecyclerView = (RecyclerView)findViewById(R.id.waitListRecycler);
        waitInviteRecyclerView.setHasFixedSize(true);
        w_adapter = new WaitlistAdapter(waitRecyclerList, this);
        waitInviteRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));


        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



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
                                parseDataFromServer(result, progressDialog);
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
        } catch(Exception e){
            e.printStackTrace();
        }

//        w_adapter.addItem(new WaitListItem("aaa","ididid","졸프","박지상","박성준"));
//        w_adapter.addItem(new WaitListItem("aaa","ididid","졸프","박지상","박성준"));
//        w_adapter.addItem(new WaitListItem("aaa","ididid","졸프","박지상","박성준"));
//        w_adapter.addItem(new WaitListItem("aaa","ididid","졸프","박지상","박성준"));
//
//        waitInviteRecyclerView.setAdapter(w_adapter);
    }



    public void parseDataFromServer(JsonObject result, ProgressDialog progressDialog) {

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
                w_adapter.addItem(new WaitListItem(sender_img, senderID, calName, receiver, senderName));
            }
            waitInviteRecyclerView.setAdapter(w_adapter);
        }
        progressDialog.dismiss();
    }

}

