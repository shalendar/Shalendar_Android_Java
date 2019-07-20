package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainPlanAdapter extends RecyclerView.Adapter<MainPlanAdapter.ItemRowHolder> {

    private Context mContext;
    private ArrayList<MainPlanItem> mainPlanList;

    public MainPlanAdapter(ArrayList<MainPlanItem> mainPlanList, Context mContext) {
        this.mainPlanList = mainPlanList;
        this.mContext = mContext;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycleritem, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(MainPlanAdapter.ItemRowHolder holder, int position) {
        String planName = mainPlanList.get(position).getMainPlanname();
        String planTime = mainPlanList.get(position).getMainPlantime();
        ArrayList picList = mainPlanList.get(position).getTeamPicList();

        holder.main_planname.setText(planName);
        holder.main_plantime.setText(planTime);

        MainPlanTeamAdapter mt_adapter = new MainPlanTeamAdapter(picList, mContext);

        holder.mainteam_recyclerView.setHasFixedSize(true);
        holder.mainteam_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.mainteam_recyclerView.setAdapter(mt_adapter);

    }

    @Override
    public int getItemCount() {
        return (null != mainPlanList ? mainPlanList.size() : 0);
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView main_planname;
        protected TextView main_plantime;
        protected RecyclerView mainteam_recyclerView;

        public ItemRowHolder(View itemView) {
            super(itemView);

            this.mainteam_recyclerView = (RecyclerView) itemView.findViewById(R.id.mainteam_recyclerView);
            this.main_planname = (TextView) itemView.findViewById(R.id.main_planname);
            this.main_plantime = (TextView) itemView.findViewById(R.id.main_plantime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), main_planname.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
