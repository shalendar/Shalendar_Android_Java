package kr.ac.smu.cs.shalendar_java;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UserEmailAdapter extends RecyclerView.Adapter<UserEmailAdapter.ViewHolder>{

    ArrayList<UserEmail> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.custom_recycleview_invite_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        UserEmail item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(UserEmail item) {
        items.add(item);
    }

    public void setItems(ArrayList<UserEmail> items) {
        this.items = items;
    }

    public UserEmail getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, UserEmail item) {
        items.set(position, item);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView userEmail;

        public ViewHolder(View itemView) {
            super(itemView);

            userEmail = itemView.findViewById(R.id.registeredUserEmail_TextView);
        }

        public void setItem(UserEmail item) {
            userEmail.setText(item.getUserEmail());
        }
    }
}
