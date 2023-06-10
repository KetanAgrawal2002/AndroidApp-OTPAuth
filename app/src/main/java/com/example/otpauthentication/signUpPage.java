package com.example.otpauthentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class signUpPage<options> extends AppCompatActivity {

    FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    EditText signupemail;
    EditText signuppassword;
    EditText signupconfirmpassword;
    EditText signupphone;
    TextView signuptextview;
    Button signupbutton;
    ProgressBar signupprogressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        signupphone = findViewById(R.id.signupphoneno);
        signupemail = findViewById(R.id.signupemail);
        signuppassword = findViewById(R.id.signuppassword);
        signupconfirmpassword = findViewById(R.id.signupconfirmpassword);
        signupbutton = findViewById(R.id.signupbutton);
        signupprogressbar = findViewById(R.id.signupprogress);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!signupphone.getText().toString().trim().isEmpty()){
                    if((signupphone.getText().toString().trim()).length()==10){
                        if((signuppassword.getText().toString()).equals(signupconfirmpassword.getText().toString())){

                            signupprogressbar.setVisibility(View.VISIBLE);
                            signupbutton.setVisibility(View.GONE);

                            sendOtp();



                        }else{
                            Toast.makeText(signUpPage.this, "enter valid phone no.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(signUpPage.this, "enter phone no.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(signUpPage.this, "password should be same as confirm password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    void sendOtp(){
        signupprogressbar.setVisibility(View.VISIBLE);
        signupbutton.setVisibility(View.INVISIBLE);


        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                signupprogressbar.setVisibility(View.GONE);
                signupbutton.setVisibility(View.VISIBLE);
                Toast.makeText(signUpPage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                signupprogressbar.setVisibility(View.GONE);
                signupbutton.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), otpinput.class);
                intent.putExtra("phone", signupphone.getText().toString());
                intent.putExtra("verificationId", verificationId);
                startActivity(intent);
            }
        };


            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber("+91" + signupphone.getText().toString().trim())       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // (optional) Activity for callback binding
                    // If no activity is passed, reCAPTCHA verification can not be used.
                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                    .build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        }
}
