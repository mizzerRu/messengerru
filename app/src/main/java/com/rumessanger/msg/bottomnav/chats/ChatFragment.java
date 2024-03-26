package com.rumessanger.msg.bottomnav.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rumessanger.msg.chats.Chat;
import com.rumessanger.msg.chats.ChatsAdapter;
import com.rumessanger.msg.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ChatFragment extends Fragment {

    private FragmentChatsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        LoadChats();
        return binding.getRoot();
    }


    private void LoadChats() {
        ArrayList<Chat> chats = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser != null ? currentUser.getUid() : "Default Value if Null";

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object chatValue = snapshot.child("Users").child(uid).child("Chats").getValue();
                String chatsStr = (chatValue != null) ? chatValue.toString() : "";
                String[] chatsIds = chatsStr.split(",");

                for(String chatId : chatsIds) {
                    DataSnapshot chatSnapshot = snapshot.child("Chats").child(chatId);
                    Object user1Value = chatSnapshot.child("User1").getValue();
                    String userId1 = (user1Value != null) ? user1Value.toString() : "";
                    Object user2Value = chatSnapshot.child("User2").getValue();
                    String userId2 = (user2Value != null) ? user2Value.toString() : "" ;

                    String chatUserId = (uid.equals(userId1))? userId2 : userId1;

                    Object usernameValue = snapshot.child("Users").child(chatUserId).child("username").getValue();
                    String chatName = (usernameValue != null) ? usernameValue.toString() : "";

                    Chat chat = new Chat(chatId,chatName, userId1, userId2 );
                    chats.add(chat);
                }

                Objects.requireNonNull(binding.chatsRv).setLayoutManager(new LinearLayoutManager(getContext()));
                Objects.requireNonNull(binding.chatsRv).setAdapter(new ChatsAdapter(chats));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to get user chats", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
