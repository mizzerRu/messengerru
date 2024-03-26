package com.rumessanger.msg.bottomnav.new_chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rumessanger.msg.MembersList.SearchContactActivity;
import com.rumessanger.msg.databinding.FragmentNewchatBinding;
import com.rumessanger.msg.users.User;
import com.rumessanger.msg.users.UsersAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class NewChatFragment extends Fragment {

    private FragmentNewchatBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewchatBinding.inflate(inflater, container, false);
        loadUsers();

        binding.searchContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchContactActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }




    private void loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersShapshot : snapshot.getChildren()) {

                    if (Objects.equals(usersShapshot.getKey(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        continue;
                    }
                    String username = Objects.requireNonNull(usersShapshot.child("username").getValue()).toString();
                    String profileImage = Objects.requireNonNull(usersShapshot.child("profileImage").getValue()).toString();
                    users.add(new User(username, profileImage));
                }
                    binding.usersRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.usersRv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL ));
                    binding.usersRv.setAdapter(new UsersAdapter(users));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
