package kr.ac.smu.cs.shalendar_java;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class UserEmailAdapter extends RecyclerView.Adapter<UserEmailAdapter.ViewHolder> {

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

        final int pos = position;

        UserEmail item = items.get(position);
        holder.setItem(item);

        holder.checkBox.setChecked(items.get(pos).getIs_checked());
        holder.checkBox.setTag(items.get(pos));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                UserEmail contact = (UserEmail) checkBox.getTag();

                contact.setIs_checked(checkBox.isChecked());
                items.get(pos).setIs_checked(checkBox.isChecked());

                Toast.makeText(v.getContext(), "Clicked on Checkbox: " + checkBox.getText() + " is " + checkBox.isChecked(), Toast.LENGTH_LONG).show();

            }
        });
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

        ImageView userImage;
        TextView userEmail;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.inviteList_userImage);
            userEmail = itemView.findViewById(R.id.registeredUserEmail_TextView);
            checkBox = itemView.findViewById(R.id.checkBox);


        }

        public void setItem(UserEmail item) {
            userImage.setBackground(new ShapeDrawable(new OvalShape()));
            if (Build.VERSION.SDK_INT >= 21) {
                userImage.setClipToOutline(true);
            }
            if (item.getUserImageURL().equals("DEFAULT :: profile_IMAGE")) {
                userImage.setImageResource(R.drawable.profile_default);
            } else {
                Ion.with(userImage)
                        .centerCrop()
                        .resize(80, 80)
                        .load(item.getUserImageURL());
            }
            userImage.setBackground(new ShapeDrawable(new OvalShape()));
            userImage.setClipToOutline(true);
            userEmail.setText(item.getUserEmail());
//            checkBox.setChecked(item.getIs_checked());
        }

    }
}
