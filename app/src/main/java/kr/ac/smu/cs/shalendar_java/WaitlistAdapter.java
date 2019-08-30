package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class WaitlistAdapter extends RecyclerView.Adapter<WaitlistAdapter.ViewHolder> {

    private ArrayList<WaitListItem> waitList;
    private Context context;

    private OnItemClickListener mListener = null;


    public WaitlistAdapter(ArrayList<WaitListItem> waitList, Context mContext) {
        this.waitList = waitList;
        this.context = mContext;
    }

    public interface OnItemClickListener {
        void onAcceptClick(View v, int pos);
        void onRejectClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.waitlist_item, null);
        WaitlistAdapter.ViewHolder mh = new WaitlistAdapter.ViewHolder(v);

        return mh;
    }



    public void onBindViewHolder(ViewHolder holder, int position) {

        String waitPeopleImg = waitList.get(position).getWaitPeoplePic();
        String senderEmailID = waitList.get(position).getEmailID();
        String invitedCalName = waitList.get(position).getCalendarName();
        String senderName = waitList.get(position).getInvitedName();
        String receiverName = waitList.get(position).getInviteName();
        int calendarID = waitList.get(position).getCid();

        holder.inviteName.setText(senderName);
        holder.invitedName.setText(receiverName);
        holder.calendarName.setText(invitedCalName);
        holder.emailID.setText(senderEmailID);
        holder.setCalendarID(calendarID);
        //holder.WaitPeoplePic.setImageResource();

        Ion.with(holder.waitPeoplePic)
                .centerCrop()
                .resize(50,50)
                .load(waitPeopleImg);

    }

    @Override
    public int getItemCount() {
        return (null !=  waitList? waitList.size() : 0);
    }

    public void addItem(WaitListItem item){
        waitList.add(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView waitPeoplePic;
        protected TextView emailID;
        protected TextView calendarName;
        protected TextView invitedName;
        protected TextView inviteName;
        protected Button Okbutton;
        protected Button Cancelbutton;
        protected Button cancelButton;
        protected Button acceptButton;

        protected int calendarID;

        public ViewHolder(View itemView) {

            super(itemView);
            this.waitPeoplePic=(ImageView)itemView.findViewById(R.id.waitListPicture);
            this.emailID=(TextView)itemView.findViewById(R.id.waitListID);
            this.calendarName=(TextView)itemView.findViewById(R.id.calendarName);
            this.invitedName=(TextView)itemView.findViewById(R.id.invitedName);
            this.inviteName=(TextView)itemView.findViewById(R.id.inviteName);
            this.cancelButton = itemView.findViewById(R.id.inviteCancelButton);
            this.acceptButton = itemView.findViewById(R.id.inviteAcceptButton);


            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mListener != null)
                            mListener.onAcceptClick(v, pos);
                    }
                }
            });


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mListener != null)
                            mListener.onRejectClick(v, pos);
                    }
                }
            });
        }

        public void setCalendarID(int cid) {
            this.calendarID = cid;
        }
    }
}
