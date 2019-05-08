package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlandetailItemView extends LinearLayout {

    TextView reply_profile_name;
    TextView reply_date;
    TextView reply_content;

    public PlandetailItemView(Context context) {
        super(context);
        init(context);
    }

    public PlandetailItemView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.reply_item, this, true);

        reply_profile_name=(TextView)findViewById(R.id.reply_profile_name);
        reply_date=(TextView)findViewById(R.id.reply_date);
        reply_content=(TextView)findViewById(R.id.reply_content);
    }

    public void setReply_profile_name(String profile_name){ reply_profile_name.setText(profile_name);}
    public void setReply_date(String date){ reply_date.setText(date);}
    public void setReply_content(String content){ reply_content.setText(content);}

}
