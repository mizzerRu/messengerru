package com.rumessanger.msg.MembersList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rumessanger.msg.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchContactActivity extends AppCompatActivity {

    MutableLiveData<ArrayList<MemberUser>> liveData = new MutableLiveData<ArrayList<MemberUser>>();
    androidx.appcompat.widget.SearchView searchView;
    RecyclerView recyclerView;
    TextView member;
    TextView phonemem;
    CircleImageView avatarmem;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchcontact_layout);

        member = findViewById(R.id.member_rv);
        phonemem = findViewById(R.id.rv_phone);
        avatarmem = findViewById(R.id.avatar_member_rv);
        recyclerView = findViewById(R.id.rv_searchContact);
        searchView = findViewById(R.id.findMember);

        final Observer<ArrayList<MemberUser>> nameObserver = new Observer<ArrayList <MemberUser>>() {
            @Override
            public void onChanged(@Nullable final ArrayList <MemberUser> members) {
                recyclerView.setAdapter(new MemberAdapter (members));
            }
        };

        liveData.observe(this, nameObserver);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPhoneNumber(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchPhoneNumber(String phoneNumber) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        
        Query query = usersRef.child("phone").equalTo(phoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<MemberUser> searchResults = new ArrayList<>(); // Создаем список для хранения результатов поиска
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String avatarUrl = snapshot.child("profileImage").getValue(String.class);

                    MemberUser memberUser = new MemberUser(username, phone, avatarUrl); // Создаем объект пользователя
                    searchResults.add(memberUser); // Добавляем пользователя в список результатов
                    Log.d("MyLog", String.valueOf(memberUser));
                }

                liveData.setValue(searchResults);
                // Здесь можно использовать searchResults для дальнейшей обработки или вывода
                for (MemberUser memberUser : searchResults) {
                    Log.d("UserInfo", "Username: " + memberUser.getUsername() +
                            ", Phone: " + memberUser.getPhone() +
                            ", Avatar URL: " + memberUser.getAvatarUrl());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // обработка ошибок

            }
        });
    }
}
