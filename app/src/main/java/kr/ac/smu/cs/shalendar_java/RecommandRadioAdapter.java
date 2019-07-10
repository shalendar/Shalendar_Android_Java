package kr.ac.smu.cs.shalendar_java;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class RecommandRadioAdapter extends RecyclerView.Adapter<RecommandRadioAdapter.ViewHolder> {

    public int mSelectedItem = -1;
    public ArrayList<RecommandTimeItem> mItems;
    private Context mContext;
    public int minPosition;

    public RecommandRadioAdapter() {
    }

    public RecommandRadioAdapter( Context mContext, ArrayList<RecommandTimeItem> mItems) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.recommand_time_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommandRadioAdapter.ViewHolder holder, int position) {
        holder.radio_button.setChecked(position == mSelectedItem);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton radio_button;
        public TextView time_number;
        public TextView time_text;

        public ViewHolder(final View inflate) {
            super(inflate);
            time_number = (TextView) inflate.findViewById(R.id.recommandtime_number);
            time_text = (TextView) inflate.findViewById(R.id.recommandtime_text);
            radio_button = (RadioButton) inflate.findViewById(R.id.recommand_radio_button);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    minPosition = mSelectedItem;

                    notifyDataSetChanged();
                }
            };
            itemView.setOnClickListener(clickListener);
            radio_button.setOnClickListener(clickListener);
        }

    }
}
