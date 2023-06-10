package com.example.otpauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpinput extends AppCompatActivity {
    EditText otpinput;
    Button verifybutton;
    ProgressBar inputprogressBar;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpinput);
        otpinput = findViewById(R.id.otpinputtext);
        verifybutton = findViewById(R.id.otpsubmit);
        inputprogressBar = findViewById(R.id.otpprogress);
        String phoneno = "OTP sent to"+ getIntent().getStringExtra("phone");
        Toast.makeText(this, phoneno, Toast.LENGTH_SHORT).show();
        verificationId = getIntent().getStringExtra("verificationId");
        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputprogressBar.setVisibility(View.VISIBLE);
                verifybutton.setVisibility(View.INVISIBLE);
                if(otpinput.getText().toString().trim().isEmpty()){
                    Toast.makeText(otpinput.this, "please enter otp before procceding", Toast.LENGTH_SHORT).show();
                }else{
                    if(verificationId!=null){
                        String code = otpinput.getText().toString().trim();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth.getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    inputprogressBar.setVisibility(View.VISIBLE);
                                    verifybutton.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(otpinput.this,MainActivity.class);
                                    //below line is used s that user cannot come to previous page on clicking back button
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else{
                                    inputprogressBar.setVisibility(View.VISIBLE);
                                    verifybutton.setVisibility(View.INVISIBLE);
                                    Toast.makeText(otpinput.this, "OTP is not valid", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            }
        });

    }
}