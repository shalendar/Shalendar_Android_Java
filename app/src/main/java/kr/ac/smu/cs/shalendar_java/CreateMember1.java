package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class CreateMember1 extends AppCompatActivity {

    private EditText editTextUserEmail;
    private Button buttonToMember2;
    private String userEmail;

    private NetWorkUrl url = new NetWorkUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member1);

        editTextUserEmail = (EditText)findViewById(R.id.create_member1_editText1);
        buttonToMember2 = findViewById(R.id.create_member2_button);


        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        Ion.getDefault(this).getConscryptMiddleware().enable(false);

        buttonToMember2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원 가입 2번째로 보낼 userEmail
                userEmail = editTextUserEmail.getText().toString().trim();
                Log.d("Email", userEmail);

                if(userEmail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Email을 입력하세요~", Toast.LENGTH_LONG).show();
                }

                else if(!userEmail.contains("@")) {
                    Toast.makeText(getApplicationContext(), "잘못된 Email형식입니다", Toast.LENGTH_LONG).show();
                }

                else{

                    JsonObject json = new JsonObject();
                    json.addProperty("id", userEmail);

                    Ion.with(getApplicationContext())
                            .load("POST", url.getServerUrl() + "/emailCheck")
                            .setHeader("Content-Type","application/json")
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if(e != null) { //서버 연결 오류
                                        Toast.makeText(getApplicationContext(), "SEver Error", Toast.LENGTH_LONG).show();
                                    }

                                    else {// 서버 연결 성공 시
                                        if(result.get("message").getAsString().equals("available")) {
                                            Toast.makeText(getApplicationContext(), result.get("message").getAsString(), Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(getApplicationContext(), CreateMember2.class);
                                            intent.putExtra("userEmail", userEmail);
                                            startActivityForResult(intent, CodeNumber.TO_CREATE_MEMBER2);
                                        }
                                        else
                                            Toast.makeText(getApplicationContext(), "해당 이메일은 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
//                    Intent intent = new Intent(getApplicationContext(), CreateMember2.class);
//                    intent.putExtra("userEmail", userEmail);
//                    startActivityForResult(intent, CodeNumber.TO_CREATE_MEMBER2);
                }
            }
        });
    }
}
