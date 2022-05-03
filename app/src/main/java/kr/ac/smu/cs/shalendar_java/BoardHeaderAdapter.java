package kr.ac.smu.cs.shalendar_java;


import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class BoardHeaderAdapter extends RecyclerView.Adapter<BoardHeaderAdapter.TeammateHolder> {

    private ArrayList<BoardTeamItem> teammateList= new ArrayList<>();

    @NonNull
    @Override
    public TeammateHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teammember_recyclerview_item, viewGroup, false);
        return new TeammateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeammateHolder holder, int position) {
        holder.onBind(teammateList.get(position));
    }

    @Override
    public int getItemCount() {
        return teammateList.size();
    }

    void addItem(BoardTeamItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        teammateList.add(data);
    }

    class TeammateHolder extends RecyclerView.ViewHolder {

        private ImageView teammate_pic;
        private TextView teammate_name;

        TeammateHolder(View itemView) {
            super(itemView);

            teammate_pic=itemView.findViewById(R.id.teammember_profile_image);
            teammate_name=itemView.findViewById(R.id.teammember_profile_name);
        }

        void onBind(BoardTeamItem data) {
            Global global = new Global();
            global.setProfileImage(teammate_pic, data.getTeammate_pic());

            teammate_name.setText(data.getTeammate_name());
        }
    }
}
