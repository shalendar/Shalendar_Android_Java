package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class RecommandTimeAdapter extends RecommandRadioAdapter {

    ArrayList<RecommandTimeItem> items = new ArrayList<>();
    Context context;

    public RecommandTimeAdapter() {
    }

    public RecommandTimeAdapter(Context context, ArrayList<RecommandTimeItem> items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(RecommandRadioAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.time_number.setText(items.get(position).getReco_timeNumber());
        holder.time_text.setText(items.get(position).getReco_time());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void sendServer() {
        Log.d("불러", items.get(minPosition).getReco_time());
    }

    public String sendRecommandTime() {
        return items.get(minPosition).getReco_time();
    }

    public void addItem(RecommandTimeItem item) {
        items.add(item);
    }

}
