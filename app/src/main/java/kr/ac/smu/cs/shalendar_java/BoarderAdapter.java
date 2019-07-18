package kr.ac.smu.cs.shalendar_java;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BoarderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BoardPlanItem> boardList = new ArrayList<>();
    private Context context;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;
    private BoardHeaderAdapter h_adapter;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_boardheader, parent, false);
            //추가

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
            RecyclerView h_recyclerView = view.findViewById(R.id.header_recycler);
            h_recyclerView.setLayoutManager(linearLayoutManager);

            h_adapter = new BoardHeaderAdapter();
            h_recyclerView.setAdapter(h_adapter);

            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.addItem(new BoardTeamItem("박성준",R.drawable.ic_launcher_foreground));
            h_adapter.notifyDataSetChanged();

            holder = new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boardplan_item, parent, false);
            holder = new ItemViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        }
        else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.onBind(boardList.get(position - 1), position);
        }
    }

    @Override
    public int getItemCount() {
        return boardList.size() + 1;
    }

    void addItem(BoardPlanItem data) {
        boardList.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView planname_text;
        private TextView plandate_text;
        private TextView planlocation_text;
        private TextView planreply_text;
        private BoardPlanItem data;
        private int position;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);

            plandate_text = itemView.findViewById(R.id.dateandtime);
            planname_text = itemView.findViewById(R.id.planname);
            planlocation_text = itemView.findViewById(R.id.location);
            planreply_text = itemView.findViewById(R.id.replynum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), boardList.get(getAdapterPosition()-1).getPlanname(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PlanDetailActivity.class);
                    //어댑터에서는 이렇게 해줘야함
                    //해당 plandetail로 이동
                    context.startActivity(intent);
                }
            });

        }

        void onBind(BoardPlanItem data, int position) {
            this.data = data;
            this.position = position;

            plandate_text.setText(data.getDateandtime());
            planname_text.setText(data.getPlanname());
            planlocation_text.setText(data.getLocation());
            planreply_text.setText(data.getReplynum());
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        HeaderViewHolder(View headerView) {
            super(headerView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        FooterViewHolder(View footerView) {
            super(footerView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else if (position == boardList.size() + 1)
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }
}