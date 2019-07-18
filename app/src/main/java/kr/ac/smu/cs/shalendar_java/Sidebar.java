package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Sidebar extends LinearLayout implements View.OnClickListener {

    public EventListener listener;

    public void setEventListener(EventListener l) {
        listener = l;
    }



    public interface EventListener {
        void btnCancel();
        void btnLevel1();
        void btnLevel2();
        void btnLevel3();
    }



    public Sidebar(Context context)
    {
        this(context, null);
        init();
    }


    public Sidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.activity_sidebar, this, true);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_info).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_add_calender).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                listener.btnCancel();
                break;
            case R.id.btn_info :
                listener.btnLevel1();

                break;
            case R.id.btn_setting :
                listener.btnLevel2();
                break;
            case R.id.btn_add_calender :
                listener.btnLevel3();
                break;
            default:
                break;
        }
    }
}
