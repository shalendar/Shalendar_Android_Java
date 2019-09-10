package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

public class PlandetailItemView extends LinearLayout {

    ImageView reply_profile_img;
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

        reply_profile_img = findViewById(R.id.reply_profile_pic);
        reply_profile_name=(TextView)findViewById(R.id.reply_profile_name);
        reply_date=(TextView)findViewById(R.id.reply_date);
        reply_content=(TextView)findViewById(R.id.reply_content);
    }

    public void setReply_profile_img(String profile_img) {

        Global global = new Global();
        global.setProfileImage(reply_profile_img, profile_img);
//        if (!(profile_img.equals("DEFAULT :: profile_IMAGE"))) {
//
//            reply_profile_img.setBackground(new ShapeDrawable(new OvalShape()));
//            if(Build.VERSION.SDK_INT >= 21) {
//                reply_profile_img.setClipToOutline(true);
//            }
//
//            Ion.with(reply_profile_img)
//                    .centerCrop()
//                    .resize(250, 250)
//                    .load(profile_img);
//        } else {
//
//            Ion.with(reply_profile_img)
//                    .centerCrop()
//                    .placeholder(R.drawable.profile_default)
//                    .resize(250, 250);
//        }
    }
    public void setReply_profile_name(String profile_name){ reply_profile_name.setText(profile_name);}
    public void setReply_date(String date){ reply_date.setText(date);}
    public void setReply_content(String content){ reply_content.setText(content);}

}
