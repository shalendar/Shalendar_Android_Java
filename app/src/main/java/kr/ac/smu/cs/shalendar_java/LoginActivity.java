package kr.ac.smu.cs.shalendar_java;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

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
import java.util.HashMap;
import java.util.Map;

/*
    Login하는 Activity
    Login -> MainActivity로 넘어간다.

 */

public class LoginActivity extends AppCompatActivity {

    //EditText 멤버 변수
    private EditText id_Email;
    private EditText password;

    //button 멤버 변수
    private Button buttonToMain;
    private Button buttonToMember1;

    //EditText값을 String에 저장하기 위한 멤버변수.
    private String userEmail;
    private String userPassword;

    //서버 통신 위한 url 객체 생성  여기서는 /signin
    private NetWorkUrl url = new NetWorkUrl();

    //서버로 부터 로그인 성공 시 오는 응답 Token 변수
    private int userToken;

    //서버로 부터 로그인 실패 시 오는 응답 변수
    private String responseFromServer;

    //Volley를 사용한 통신
    private static RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id_Email = findViewById(R.id.login_id_EditText);
        password = findViewById(R.id.login_password_EditText);


        buttonToMain = findViewById(R.id.login_toMain_button);
        buttonToMember1 = findViewById(R.id.login_signin_button);

        /*
           우선 버튼 클릭시 MainActivity로 넘어간다.
           - 나중에 CreateMemberActivity로 넘어가는 코드 짜야 한다.
        */


        //로그인 버튼
        buttonToMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                userEmail = id_Email.getText().toString().trim();
                userPassword = password.getText().toString().trim();

                Log.d("맞아?", Boolean.toString(userEmail.equals("jacob456@hanmail.net")));


                /*
                  입력하는 e-mail주소 형식 예외처리
                  사용자 이메일 & 비밀번호 dummy data
                  서버 닫혀 있을 때 서버 코드 주석처리 하고 아래  if else if else문 수행.
                */
                if(userEmail.equals("jacob") && userPassword.equals("456")) {

                    Toast.makeText(getApplicationContext(), "사용자정보 일치 메인화면으로이동", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);
                }

                else if(userEmail.equals("") || userPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                else {
                    Log.d("입력한 ID", userEmail);
                    Log.d("입력한 비밀번호", userPassword);
                    Toast.makeText(getApplicationContext(), "아이디/비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show();
                }


                //서버 통신코드 1 AsnychTask사용
                //new LoginTask(LoginActivity.this).execute(url.getServerUrl() + "/signin");

                //서버 통신코드 2 Volley사용
                //makeRequest();
            }
        });

        //Volley 방식 통신을 위한 시작부분
        if(requestQueue == null) {
            //RequestQue객체 생성.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


        //이메일로 회원가입 버튼 클릭 경우
        buttonToMember1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateMember1.class);
                startActivityForResult(intent, CodeNumber.TO_CREATE_MEMBER1);
            }
        });
    }


    //로그인 통신 코드 1. AsynchTask사용.

    public class LoginTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        public LoginTask(Context context){
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute(){
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

                jsonObject.put("id", userEmail);
                jsonObject.put("pw", userPassword);


                Log.d("들어갔는지 확인", "jsonOk??");


                HttpURLConnection con = null;
                BufferedReader reader = null;


                try{
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
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
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
            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);


                responseFromServer = (String)jsonObject.get("message");
                userToken = (int)jsonObject.get("token");


                Log.d("서버응답", responseFromServer);
                Toast.makeText(getApplicationContext(), responseFromServer, Toast.LENGTH_LONG).show();


                //서버로 부터 success 응답 받으면 메인 화면으로 넘어간다.
                if(responseFromServer.equals("login success")) {

                    SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("userToken", userToken);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);
                }
                else if(responseFromServer.equals("wrong password")) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치 하지 않습니다", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //로그인 통신 코드 2 Volley 사용.
    public void makeRequest() {

        String urlToServer = url.getServerUrl() + "/singin";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlToServer, null,
                new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {

                try {
                    responseFromServer = response.getString("message");
                    userToken = response.getInt("token");

                    if(responseFromServer.equals("login success")) {

                        SharedPreferences pref = getSharedPreferences("pref_USERTOKEN", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userToken", userToken);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivityForResult(intent, CodeNumber.TO_MAIN_ACTIVITY);
                    }
                    else if(responseFromServer.equals("wrong password")) {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치 하지 않습니다", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG).show();
                    }

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }

            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("서버 연결 오류", error.toString());
            }
        })

        {

            //RequestBody
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
              Map<String, String> params = new HashMap<>();
              params.put("id", userEmail);
              params.put("pw", userPassword);
              return params;
          }

            //RequestHeader
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);

    }
}
