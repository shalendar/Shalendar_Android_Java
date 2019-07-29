package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

public class SidebarTeamAdapter extends RecyclerView.Adapter<SidebarTeamAdapter.TeamItemHolder>{

    private ArrayList<SidebarTeamItem> teamPicList;
    private Context mContext;


    public SidebarTeamAdapter(ArrayList<SidebarTeamItem> teamPicList, Context mContext) {
        this.teamPicList = teamPicList;
        this.mContext = mContext;
    }

    @Override
        public TeamItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sidebarteamitem, null);
        SidebarTeamAdapter.TeamItemHolder th = new SidebarTeamAdapter.TeamItemHolder(v);
        return th;
    }

    @Override
    public void onBindViewHolder(TeamItemHolder holder, int position) {
        SidebarTeamItem teamIteam = teamPicList.get(position);
        holder.sideteammatePic.setImageResource(teamIteam.getTeammatePic());


    }

    @Override
    public int getItemCount() {
        return (null != teamPicList ? teamPicList.size() : 0);
    }

    public class TeamItemHolder extends RecyclerView.ViewHolder {

        protected ImageView sideteammatePic;

        public TeamItemHolder(View itemView) {
            super(itemView);

            this.sideteammatePic = (ImageView) itemView.findViewById(R.id.sidebarTeamImage);

        }
    }
}
