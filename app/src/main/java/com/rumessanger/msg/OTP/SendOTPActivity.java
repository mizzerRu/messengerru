package com.rumessanger.msg.OTP;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rumessanger.msg.R;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_send_otpactivity);
       final EditText inputMobile = findViewById(R.id.inputMobile);
       final ProgressBar progressBar = findViewById(R.id.progressBar);
       Button buttonGetOTP = findViewById(R.id.buttonGetOTP);

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (inputMobile.getText().toString().trim().isEmpty()) {
                   Toast.makeText(SendOTPActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                   return;
               }

               progressBar.setVisibility(View.VISIBLE);
               buttonGetOTP.setVisibility(View.INVISIBLE);
               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
               firebaseAuth.setLanguageCode("ru");
               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       "+7" + inputMobile.getText().toString(),
                       60,
                       TimeUnit.SECONDS,
                       SendOTPActivity.this,
                       new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                           @Override
                           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                               progressBar.setVisibility(View.GONE);
                               buttonGetOTP.setVisibility(View.VISIBLE);
                               Toast.makeText(SendOTPActivity.this, "Code send your number", Toast.LENGTH_SHORT).show();
                           }
                           @Override
                           public void onVerificationFailed(@NonNull FirebaseException e) {
                               Toast.makeText(SendOTPActivity.this, "Failed send code", Toast.LENGTH_SHORT).show();
                           }
                           @Override
                           public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                intent.putExtra("mobile", inputMobile.getText().toString());
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                           }
                       }
               );

               Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
               intent.putExtra("mobile", inputMobile.getText().toString());
               startActivity(intent);
           }
       });


    }
}