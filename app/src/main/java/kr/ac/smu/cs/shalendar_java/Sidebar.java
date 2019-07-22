package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Sidebar extends LinearLayout implements View.OnClickListener {


    ArrayList<SidebarItem> calendarRecyclerList;

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

        //리사이클러
        calendarRecyclerList = new ArrayList<>();

        insertData();

        RecyclerView sidebarRecyclerView = (RecyclerView)findViewById(R.id.sideBarRecyclerView);
        sidebarRecyclerView.setHasFixedSize(true);
        SidebarAdapter s_adapter = new SidebarAdapter(sidebarRecyclerView.getContext(), calendarRecyclerList);
        sidebarRecyclerView.setLayoutManager(new LinearLayoutManager(sidebarRecyclerView.getContext(), LinearLayout.VERTICAL, false));
        sidebarRecyclerView.setAdapter(s_adapter);



    }

    public void insertData(){
        for(int i=0; i<=10; i++) {
            SidebarItem sitem = new SidebarItem();
            sitem.setCalendarName("달력"+(i+1));
            sitem.setCalendarImage(R.drawable.ic_launcher_foreground);

            ArrayList<SidebarTeamItem> stItem = new ArrayList<>();
            for(int j=0; j<6; j++){
                stItem.add(new SidebarTeamItem(R.drawable.face));
            }
            sitem.setTeamImageList(stItem);
            calendarRecyclerList.add(sitem);
        }
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
