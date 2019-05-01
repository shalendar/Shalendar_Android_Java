package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoardPlanItemView extends LinearLayout {

    TextView dateandtimetext;
    TextView plannametext;
    TextView locationtext;
    TextView replynumtext;


    public BoardPlanItemView(Context context) {
        super(context);
        init(context);
    }

    public BoardPlanItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boardplan_item, this, true);

        dateandtimetext=(TextView)findViewById(R.id.dateandtime);
        plannametext=(TextView)findViewById(R.id.planname);
        locationtext=(TextView)findViewById(R.id.location);
        replynumtext=(TextView)findViewById(R.id.replynum);

    }

    public void setDateandtimetext(String dateandtime){
        dateandtimetext.setText(dateandtime);
    }

    public void setPlannametext(String planname){
        plannametext.setText(planname);
    }

    public void setLocationtext(String location){
        locationtext.setText(location);
    }

    public void setReplynumtext(String replynum){
        replynumtext.setText(replynum);
    }
}
