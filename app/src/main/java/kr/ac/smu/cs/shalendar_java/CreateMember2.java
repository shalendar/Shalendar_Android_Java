package kr.ac.smu.cs.shalendar_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateMember2 extends AppCompatActivity {

    private Button buttonToMember3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member2);

        buttonToMember3 = findViewById(R.id.create_member3_button);

        buttonToMember3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), CreateMember3.class);
                startActivityForResult(intent2, CodeNumber.TO_CREATE_MEMBER3);

            }
        });




    }
}
