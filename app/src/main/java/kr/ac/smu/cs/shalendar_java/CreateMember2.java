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
                    //안드 Test용, 서버 통신이 없어도 되는 코드임.
                    Intent intent = new Intent(getApplicationContext(), CreateMember3.class);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPassword", userPassword);

                    //여기서 서버와 통신.
                    //new CreateMemberTask(CreateMember2.this).execute(url.getServerUrl() + "/signup");

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


//    public class CreateMemberTask extends AsyncTask<String, String, String> {
//
//        ProgressDialog progressDialog;
//
//        public CreateMemberTask(Context context){
//            progressDialog = new ProgressDialog(context);
//        }
//
//        @Override
//        protected void onPreExecute(){
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시만 기다려주세요. 회원정보 등록 중 입니다~");
//            progressDialog.show();
//
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//                Log.d("doInBackground 확인", "doIn");
//                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
//
//
////                for (int i = 0; i < 5; i++) {
////                    progressDialog.setProgress(i * 30);
////                    Thread.sleep(500);
////
////                }
//
//                JSONObject jsonObject = new JSONObject();
//
//                jsonObject.put("id", userEmail);
//                jsonObject.put("pw", userPassword);
//                jsonObject.put("userName", userName);
//                jsonObject.put("deviceToken", deviceToken);
//
//                Log.d("들어갔는지 확인", "jsonOk??");
//
//
//                HttpURLConnection con = null;
//                BufferedReader reader = null;
//
//
//                try{
//                    URL url = new URL(urls[0]);
//                    //연결을 함
//                    con = (HttpURLConnection) url.openConnection();
//
//                    con.setRequestMethod("POST");//POST방식으로 보냄
//                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
//                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
//
//                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
//                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
//                    con.connect();
//
//                    Log.d("con 연결1", "");
//
//                    //서버로 보내기위해서 스트림 만듬
//                    OutputStream outStream = con.getOutputStream();
//                    //버퍼를 생성하고 넣음
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
//                    writer.write(jsonObject.toString());
//                    writer.flush();
//                    writer.close();//버퍼를 받아줌
//
//                    Log.d("con 연결2", "");
//
//
//                    InputStream stream = con.getInputStream();
//
//                    Log.d("con연결3 데이터받음", "");
//
//                    reader = new BufferedReader(new InputStreamReader(stream));
//
//                    StringBuffer buffer = new StringBuffer();
//                    String line = "";
//                    while((line = reader.readLine()) != null){
//                        buffer.append(line);
//                    }
//
//                    return buffer.toString();
//
//                } catch (MalformedURLException e){
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if(con != null){
//                        con.disconnect();
//                    }
//                    try {
//                        if(reader != null){
//                            reader.close();//버퍼를 닫아줌
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return "";
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            //dialog창 닫기
//            progressDialog.dismiss();
//            try {
//
//                JSONParser jsonParser = new JSONParser();
//                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
//
//                responseFromServer = (String)jsonObject.get("message");
//                Log.d("서버응답", responseFromServer);
//                Toast.makeText(getApplicationContext(), responseFromServer, Toast.LENGTH_LONG).show();
//
//
//                //서버로 부터 success 응답 받으면 '회원 가입 완료 화면(CreateMember3.java)'으로 넘어간다.
//                if(responseFromServer.equals("success")) {
//                    Intent intent = new Intent(getApplicationContext(), CreateMember3.class);
//                    startActivity(intent);
//                }
//                else if(responseFromServer.equals("please check email")) {
//                    Toast.makeText(getApplicationContext(), "이미 사용중인 Emial입니다.", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG).show();
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
}
