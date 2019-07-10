package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Sidebar extends LinearLayout implements View.OnClickListener {

    public EventListener listener;

    public void setEventListener(EventListener l) {
        listener = l;
    }



    public interface EventListener {
        void btnCancel();
        void btnLevel1();
        void btnLevel2();
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
        findViewById(R.id.btn_side_level_1).setOnClickListener(this);
        findViewById(R.id.btn_side_level_2).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                listener.btnCancel();
                break;
            case R.id.btn_side_level_1:
                listener.btnLevel1();
                break;
            case R.id.btn_side_level_2:
                listener.btnLevel2();
                break;
            default:
                break;
        }
    }
}
