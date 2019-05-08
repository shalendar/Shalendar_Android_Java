package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateMember1 extends AppCompatActivity {

    private Button buttonToMember2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member1);

        buttonToMember2 = findViewById(R.id.create_member2_button);

        buttonToMember2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), CreateMember2.class);
                startActivityForResult(intent2, CodeNumber.TO_CREATE_MEMBER2);

            }
        });


    }
}
