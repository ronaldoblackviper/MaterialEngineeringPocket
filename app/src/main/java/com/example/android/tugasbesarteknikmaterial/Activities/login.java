package com.example.android.tugasbesarteknikmaterial.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tugasbesarteknikmaterial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText userMail, userPass;
    private Button btnLogin;
    private ProgressBar loginprogress;
    private FirebaseAuth mAuth;
    private Intent dashboard;

    int setPtypelogin;
    TextView txttypelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.logusername);
        userPass = findViewById(R.id.logpassword);
        btnLogin = findViewById(R.id.btn_login);
        loginprogress = findViewById(R.id.login_ProgressBar);
        mAuth = FirebaseAuth.getInstance();
        dashboard = new Intent(this, com.example.android.tugasbesarteknikmaterial.Activities.dashboard.class);

        loginprogress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginprogress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String email = userMail.getText().toString();
                final String password = userPass.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    showMassage("Please verify all fields");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginprogress.setVisibility(View.INVISIBLE);
                }else{
                    if(!isConnected()){
                        showMassage("No internet connection");
                        btnLogin.setVisibility(View.VISIBLE);
                        loginprogress.setVisibility(View.INVISIBLE);
                    }else{
                        signIn(email,password);
                    }
                }
            }
        });

        TextView tv = (TextView) findViewById(R.id.tv_register);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, register.class);
                startActivity(i);
            }
        });

        setPtypelogin = 1;
        txttypelogin = (TextView) findViewById(R.id.titypelogin);
        txttypelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setPtypelogin == 1 ){
                    setPtypelogin = 0;
                    userPass.setTransformationMethod(null);
                    if(userPass.getText().length()>0)
                        userPass.setSelection(userPass.getText().length());
                    txttypelogin.setBackgroundResource(R.drawable.eye);
                }else{
                    setPtypelogin = 1;
                    userPass.setTransformationMethod(new PasswordTransformationMethod());
                    if(userPass.getText().length()>0)
                        userPass.setSelection(userPass.getText().length());
                    txttypelogin.setBackgroundResource(R.drawable.hide);
                }
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginprogress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();
                }else{
                    showMassage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginprogress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        startActivity(dashboard);
        finish();
    }

    private void showMassage(String Message) {
        Toast.makeText(getApplicationContext(),Message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            updateUI();
        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
