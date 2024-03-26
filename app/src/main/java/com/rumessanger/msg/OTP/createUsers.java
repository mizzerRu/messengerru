package com.rumessanger.msg.OTP;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.rumessanger.msg.MainActivity;
import com.rumessanger.msg.R;
import com.rumessanger.msg.databinding.CreateUsersActivityBinding;
import java.io.IOException;
import java.util.Objects;

public class createUsers extends AppCompatActivity {

    public final android.content.Context requireContext() {
        return null;
    }

    private CreateUsersActivityBinding binding;
    private Uri filepath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateUsersActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.avatarCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.createuser.getText().toString().isEmpty()) {
                    Toast.makeText(createUsers.this, "Please indicate username!", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance()
                                            .getCurrentUser())
                                    .getUid())
                            .child("username").setValue(binding.createuser.getText().toString());


                    String mobile_phone = String.valueOf((R.id.textMobile));
                    FirebaseDatabase.getInstance().getReference().child("account").child(mobile_phone).setValue(mobile_phone);

                    Intent intent = new Intent(createUsers.this, MainActivity.class);
                    startActivity(intent);
                }
             }
        });
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
                                    Objects.requireNonNull(requireContext()).getContentResolver(),
                                    filepath
                            );
                            binding.avatarCreate.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        upLoadImage();
                    }
                }
            }

    );

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
                            Toast.makeText(createUsers.this, "Photo upload success", Toast.LENGTH_SHORT).show();
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
