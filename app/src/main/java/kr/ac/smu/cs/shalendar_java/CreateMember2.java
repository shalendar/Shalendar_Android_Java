package kr.ac.smu.cs.shalendar_java;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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
import org.json.simple.JSONObject;

public class CreateMember2 extends AppCompatActivity {

    private Button buttonToMember3;
    private EditText editTextUserName;
    private EditText editTextUserPassword;
    private EditText editTextReEntPassword;

    //CreateMember3로 보낼 data
    private String userEmail;
    private String userName;
    private String userPassword;
    private String reEntPassword;


    //서버 URL을 위한 객체 생성
    private NetWorkUrl url = new NetWorkUrl();


    //Firebase를 위한 device Token값
    private String deviceToken;


    public String getUserName() {
        return this.userName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member2);

        buttonToMember3 = findViewById(R.id.create_member3_button);
        editTextUserName = findViewById(R.id.create_mem2_userName_editText1);
        editTextUserPassword = findViewById(R.id.create_mem2_password_editText2);
        editTextReEntPassword = findViewById(R.id.create_mem2_checkPassword_editText3);


        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        Toast.makeText(getApplicationContext(), userEmail, Toast.LENGTH_LONG).show();


        try {
             deviceToken = FirebaseInstanceId.getInstance().getToken();
            Log.i("Device Token", deviceToken);
        } catch (Exception e) {
            e.printStackTrace();
        }


        buttonToMember3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editTextUserName.getText().toString().trim();
                userPassword = editTextUserPassword.getText().toString().trim();
                reEntPassword = editTextReEntPassword.getText().toString().trim();


                if(userName.equals("") || userName.length() < 2) {
                    Toast.makeText(getApplicationContext(), "이름은 3글자 이상으로 해주세요",Toast.LENGTH_LONG).show();
                }
                else if(userPassword.equals("") || userPassword.length() < 5) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 5자 이상이어야 합니다.",Toast.LENGTH_LONG).show();
                }
                else if(!(userPassword.equals(reEntPassword))) {
                    Toast.makeText(getApplicationContext(), "입력한 비밀번호가 맞지 않습니다",Toast.LENGTH_LONG).show();
                }
                else {

                    serverNetwork();
                }
            }
        });
    }

    public void serverNetwork() {

        JsonObject json = new JsonObject();
        json.addProperty("id", userEmail);
        json.addProperty("pw", userPassword);
        json.addProperty("userName", userName);
        /////////////////////////////////////////////////
        //divce토큰 은 로그인 할 때 준다! 변경 필요.
        /////////////////////////////////////////////////
        //json.addProperty("deviceToken", deviceToken);

        final ProgressDialog progressDialog = new ProgressDialog(CreateMember2.this);
        progressDialog.setMessage("회원 정보를 등록중입니다~");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();


        Ion.with(getApplicationContext())
                .load("POST", url.getServerUrl() + "/signup")
                .setHeader("Content-Type", "application/json")
                .progressDialog(progressDialog)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.i("회원등록 NetworkErrorCode", e.getMessage());
                        }

                        else {
                            progressDialog.dismiss();
                            parseFromServer(result);
                        }
                    }
                });
    }


    public void parseFromServer(JsonObject result) {

        String message = result.get("message").getAsString();
        if(message.equals("success")) {
            Toast.makeText(getApplicationContext(), "회원 등록 " + message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), CreateMember3.class);
            startActivity(intent);
        }

        else {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
