package com.rumessanger.msg.MembersList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rumessanger.msg.R;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> {

    private final ArrayList<MemberUser> memberUser;

    public MemberAdapter(ArrayList<MemberUser> memberUser) {
        this.memberUser = memberUser;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_rv_item, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.username_mem.setText(memberUser.get(position).username);
        holder.phone_mem.setText(memberUser.get(position).phone);
        if(!memberUser.get(position).profileImage.isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(memberUser.get(position).profileImage).into(holder.profileImage_mem) ;
        }
    }

    @Override
    public int getItemCount() {
       return memberUser.size();
    }
}
