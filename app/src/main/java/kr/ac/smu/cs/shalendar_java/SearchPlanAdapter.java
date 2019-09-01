package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchPlanAdapter extends RecyclerView.Adapter<SearchPlanAdapter.ViewHolder> {

    private ArrayList<SearchPlanItem> searchList;
    private Context context;

    public SearchPlanAdapter() {
        super();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchplan_item, null);
        SearchPlanAdapter.ViewHolder sh = new SearchPlanAdapter.ViewHolder(v);

        return sh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String planname = searchList.get(position).getPlanname();
        holder.searchedPlan.setText(planname);
    }

    @Override
    public int getItemCount() {
        return (null !=  searchList? searchList.size() : 0);
    }


    public void addItem(SearchPlanItem item){
        searchList.add(item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView searchedPlan;


        public ViewHolder(View itemView) {

            super(itemView);
            this.searchedPlan = (TextView) itemView.findViewById(R.id.searchPlanitem);

        }

    }
}
