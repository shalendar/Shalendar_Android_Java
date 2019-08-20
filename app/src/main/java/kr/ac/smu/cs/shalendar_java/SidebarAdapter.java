package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class SidebarAdapter extends RecyclerView.Adapter<SidebarAdapter.ItemRowHolder> {

    private Context context;
    private Context mContext;
    private ArrayList<SidebarItem> calendarList;


    public SidebarAdapter(Context mContext, ArrayList<SidebarItem> calendarList) {
        this.mContext = mContext;
        this.calendarList = calendarList;
    }


    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sidebaritem, null);
//        SidebarAdapter.ItemRowHolder mh = new SidebarAdapter.ItemRowHolder(v);
//        return mh;

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.sidebaritem, parent, false);

        return new ItemRowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
//        String calendarName = calendarList.get(position).getCalendarName();
//        String calendarImage = calendarList.get(position).getCalendarImage();
        ArrayList teampicList = calendarList.get(position).getTeamImageList();
//
//        holder.calendarSidebarName.setText(calendarName);
//        holder.calendarSidebarImage.setImageURI(calendarImage);

//        Ion.with(holder.calendarSidebarImage)
//                .centerCrop()
//                .placeholder(R.drawable.face)
//                .load(calendarImage);

        SidebarItem item = calendarList.get(position);
        holder.setItem(item);

        SidebarTeamAdapter st_adapter = new SidebarTeamAdapter(teampicList, mContext);

        holder.sideteam_recyclerView.setHasFixedSize(true);
        holder.sideteam_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.sideteam_recyclerView.setAdapter(st_adapter);

    }


    @Override
    public int getItemCount() {
        return (null != calendarList ? calendarList.size() : 0);
    }

    public void addItem(SidebarItem item) {
        calendarList.add(item);
    }

    public void setItems(ArrayList<SidebarItem> items) {
        this.calendarList = items;
    }

    public SidebarItem getItem(int position) {
        return calendarList.get(position);
    }

    public void setItem(int position, SidebarItem item) {
        calendarList.set(position, item);
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder {


        TextView calendarSidebarName;
        ImageView calendarSidebarImage;
        RecyclerView sideteam_recyclerView;
        ImageView toInviteMember;

        int calendar_ID;
        String calendarName;


        public ItemRowHolder(final View itemView) {
            super(itemView);

            this.sideteam_recyclerView = (RecyclerView) itemView.findViewById(R.id.sideBarTeamRecycler);
            this.calendarSidebarName = (TextView) itemView.findViewById(R.id.calendarSidebarName);
            this.calendarSidebarImage = (ImageView) itemView.findViewById(R.id.calendarSidebarImage);
            this.toInviteMember = itemView.findViewById(R.id.sidebarList_invite_member);


//            멤버 초대화면으로 이동.
            toInviteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), InviteActivity.class);
                    intent.putExtra("cid", calendar_ID);
                    intent.putExtra("calName", calendarName);
                    itemView.getContext().startActivity(intent);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), calendarSidebarName.getText() + Integer.toString(calendar_ID), Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("달력 수정/삭제");
                    dialog.setMessage("달력 수정, 삭제하십니까?")
                            .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //여기 캘린더 수정화면으로 바꿔야함
                                    Intent intent = new Intent(context, CreateCalendarActivity.class);
                                    context.startActivity(intent);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    return false;
                }
            });

        }

        public void setCid(int calendar_ID) {
            this.calendar_ID = calendar_ID;
        }

        public void setCalName(String calName) {
            this.calendarName = calName;
        }

        public void setItem(SidebarItem item) {
            calendarSidebarName.setText(item.getCalendarName());
            setCid(item.getCalendar_ID());
            setCalName(item.getCalendarName());
            Ion.with(calendarSidebarImage)
                .centerCrop()
                .placeholder(R.drawable.face)
                .load(item.getCalendarImage());
        }
    }

}
