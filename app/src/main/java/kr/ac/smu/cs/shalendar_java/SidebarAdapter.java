package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SidebarAdapter extends RecyclerView.Adapter<SidebarAdapter.ItemRowHolder> {

    private Context mContext;
    private ArrayList<SidebarItem> calendarList;

    public SidebarAdapter(Context mContext, ArrayList<SidebarItem> calendarList) {
        this.mContext = mContext;
        this.calendarList = calendarList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sidebaritem, null);
        SidebarAdapter.ItemRowHolder mh = new SidebarAdapter.ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        String calendarName = calendarList.get(position).getCalendarName();
        //int calendar_ID = calendarList.get(position).getCalendar_ID();
        int calendarImage = calendarList.get(position).getCalendarImage();
        ArrayList teampicList = calendarList.get(position).getTeamImageList();

        holder.calendarSidebarName.setText(calendarName);
        holder.calendarSidebarImage.setImageResource(calendarImage);

        SidebarTeamAdapter st_adapter = new SidebarTeamAdapter(teampicList,mContext);

        holder.sideteam_recyclerView.setHasFixedSize(true);
        holder.sideteam_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.sideteam_recyclerView.setAdapter(st_adapter);

    }

    @Override
    public int getItemCount() {
        return (null != calendarList ? calendarList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView calendarSidebarName;
        protected ImageView calendarSidebarImage;
        protected RecyclerView sideteam_recyclerView;
        //
        protected ImageView toInviteMember;
        //
        protected int cid;

        public ItemRowHolder(final View itemView) {
            super(itemView);

            this.sideteam_recyclerView = (RecyclerView) itemView.findViewById(R.id.sideBarTeamRecycler);
            this.calendarSidebarName = (TextView) itemView.findViewById(R.id.calendarSidebarName);
            this.calendarSidebarImage = (ImageView) itemView.findViewById(R.id.calendarSidebarImage);
            //
            this.toInviteMember = itemView.findViewById(R.id.sidebarList_invite_member);

            //멤버 초대화면으로 이동.
            toInviteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), InviteActivity.class);
                    itemView.getContext().startActivity(intent);

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), calendarSidebarName.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
