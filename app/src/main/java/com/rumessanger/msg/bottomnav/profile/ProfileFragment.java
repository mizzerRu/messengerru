package com.rumessanger.msg.bottomnav.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.rumessanger.msg.OTP.SendOTPActivity;
import com.rumessanger.msg.databinding.FragmentProfleBinding;

import java.io.IOException;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private Uri filepath;
    private FragmentProfleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfleBinding.inflate(inflater, container, false);
        loadUserInfo();

        binding.profileAvatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SendOTPActivity.class));
            }
        });


        return binding.getRoot();
    }

    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null)
                        ;
                    {
                        assert result.getData() != null;
                        filepath = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    requireContext().getContentResolver(),
                                    filepath
                            );
                            binding.profileAvatarImg.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        upLoadImage();

                    }
                }
            }
    );

    private void loadUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                        Object profileImageValue = snapshot.child("profileImage").getValue();
                        String profileImage = (profileImageValue != null) ? profileImageValue.toString() : "DefaultProfileImageURL";
                        binding.usernameTv.setText(username);
                        binding.usernameTxAcc.setText(username);
                        if (!profileImage.isEmpty()) {
                            Glide.with(getContext()).load(profileImage).into(binding.profileAvatarImg);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }




    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageActivityResultLauncher.launch(intent);
    }


    private void upLoadImage(){
        if(filepath!=null) {
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            FirebaseStorage.getInstance().getReference().child("images/"+uid)
                    .putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Photo upload success", Toast.LENGTH_SHORT).show();
                            FirebaseStorage.getInstance().getReference().child("images/"+uid).getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child("profileImage").setValue(uri.toString());

                                        }
                                    });
                        }
                    });
        }
    }
}
