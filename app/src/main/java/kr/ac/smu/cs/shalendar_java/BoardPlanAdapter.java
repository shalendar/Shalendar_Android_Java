package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class BoardPlanAdapter extends BaseAdapter {

    ArrayList<BoardPlanItem> items = new ArrayList<BoardPlanItem>();

    public void addItem(BoardPlanItem item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoardPlanItemView view = null;
        if (convertView == null) {
            view = new BoardPlanItemView(parent.getContext());
        } else {
            view = (BoardPlanItemView) convertView;
        }

        BoardPlanItem item = items.get(position);
        view.setDateandtimetext(item.dateandtime);
        view.setLocationtext(item.location);
        view.setPlannametext(item.planname);
        view.setReplynumtext(item.replynum);

        return view;
    }

}
