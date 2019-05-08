package kr.ac.smu.cs.shalendar_java;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TeammemberAdapter extends RecyclerView.Adapter<TeammemberAdapter.ViewHolder> {

    private ArrayList<String> mData=null;

    //생성자
    public TeammemberAdapter(ArrayList<String> mData) {
        this.mData = mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView profilename;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilename = itemView.findViewById(R.id.teammember_profile_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //context를 부모로 부터 받기
        Context context = viewGroup.getContext();
        //받은걸로 layoutinflator생성
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //어떤 레이아웃 가져와 뷰를 그릴지 결정
        View view = inflater.inflate(R.layout.teammember_recyclerview_item,viewGroup,false);
        //생성후 view홀더 만들기
        TeammemberAdapter.ViewHolder vh = new TeammemberAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name = mData.get(i);
        viewHolder.profilename.setText(name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
