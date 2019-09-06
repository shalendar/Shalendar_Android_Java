package kr.ac.smu.cs.shalendar_java;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startLoading();
    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
//
//        try {
//            Thread.sleep(3000);
//        }
//        catch (InterruptedException e){
//            e.printStackTrace();
//        }
//
//
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();

