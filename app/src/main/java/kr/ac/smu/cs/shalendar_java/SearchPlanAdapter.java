package kr.ac.smu.cs.shalendar_java;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class SearchPlanAdapter extends RecyclerView.Adapter<SearchPlanAdapter.ViewHolder> {

    private NetWorkUrl url = new NetWorkUrl();

    private ArrayList<SearchPlanItem> searchList;
    private Context context;

    public SearchPlanAdapter(ArrayList<SearchPlanItem> searchList, Context mContext) {
        this.searchList = searchList;
        this.context = mContext;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.setSid(searchList.get(getAdapterPosition()).getSid());
                    Toast.makeText(v.getContext(), searchList.get(getAdapterPosition()).getPlanname() + "여기로 간다잉", Toast.LENGTH_SHORT).show();

                    JsonObject json = new JsonObject();

                    json.addProperty("sid", Global.getSid());

                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("해당 댓글로 이동중~" + searchList.get(getAdapterPosition()).getSid());
                    progressDialog.show();

                    Global global = new Global();
                    global.netWork_ShowSche(context, progressDialog, json, url);

                }
            });

        }

    }
}
