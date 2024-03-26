package com.rumessanger.msg.MembersList;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rumessanger.msg.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImage_mem;
    TextView username_mem;
    TextView phone_mem;

    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        phone_mem = itemView.findViewById(R.id.rv_phone);
        username_mem = itemView.findViewById(R.id.member_rv);
        profileImage_mem = itemView.findViewById(R.id.avatar_member_rv);
    }
}
