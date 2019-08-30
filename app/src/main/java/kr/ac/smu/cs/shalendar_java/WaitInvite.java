package kr.ac.smu.cs.shalendar_java;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
}
