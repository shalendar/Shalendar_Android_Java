package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class MainPlanTeamAdapter extends RecyclerView.Adapter<MainPlanTeamAdapter.TeamItemHolder> {

    private ArrayList<MainPlanTeamIteam> teamList;
    private Context mContext;

    public MainPlanTeamAdapter(ArrayList<MainPlanTeamIteam> teamList, Context mContext) {
        this.teamList = teamList;
        this.mContext = mContext;
    }

    @Override
    public TeamItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_teamrecycleritem, null);
        TeamItemHolder th = new TeamItemHolder(v);
        return th;
    }

    @Override
    public void onBindViewHolder(TeamItemHolder holder, int position) {
        MainPlanTeamIteam teamIteam = teamList.get(position);
//        holder.teammatePic.setImageResource(teamIteam.getTeammatePic());

        if(teamIteam.getTeammatePic().equals("DEFAULT :: profile_IMAGE"))
            holder.teammatePic.setImageResource(R.drawable.profile_default);
        else {
            Ion.with(holder.teammatePic)
                    .centerCrop()
                    .resize(50,50)
                    .load(teamIteam.getTeammatePic());
            holder.teammatePic.setBackground(new ShapeDrawable(new OvalShape()));
            holder.teammatePic.setClipToOutline(true);

        }

    }

    @Override
    public int getItemCount() {
        return (null != teamList ? teamList.size() : 0);
    }

    public class TeamItemHolder extends RecyclerView.ViewHolder {

        protected ImageView teammatePic;

        public TeamItemHolder(View itemView) {
            super(itemView);

            this.teammatePic = (ImageView) itemView.findViewById(R.id.main_teammateImage);

        }
    }

}
