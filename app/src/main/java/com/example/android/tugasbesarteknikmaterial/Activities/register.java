package com.example.android.tugasbesarteknikmaterial.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tugasbesarteknikmaterial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class register extends AppCompatActivity {

    ImageView userPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedimageuri;

    private EditText userEmail,userName, userPassword, userPassowrd2;
    private ImageView imageregis;
    private ProgressBar loadingProgress;
    private Button regbtn;

    private FirebaseAuth mAuth;

    int setPtype;
    int setPtype2;
    TextView txttype;
    TextView txttype2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail = findViewById(R.id.regemail);
        userName = findViewById(R.id.regusername);
        userPassword = findViewById(R.id.regpassword);
        userPassowrd2 = findViewById(R.id.regpasswordconfirm);
        imageregis = findViewById(R.id.regaddimage);
        loadingProgress = findViewById(R.id.regProgressBar);
        regbtn = findViewById(R.id.btn_regis);
        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regbtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();
                final String pass = userPassword.getText().toString();
                final String pass2 = userPassowrd2.getText().toString();

                if (email.isEmpty() || pickedimageuri == null || name.isEmpty() || pass.isEmpty() || !pass.equals(pass2)){
                    showMessage("Please verify all fields and add picture profile");
                    regbtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }else{
                    if(!isConnected()){
                        showMessage("No internet connection");
                        regbtn.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                    }else{
                        CreateUserAccount(email,name,pass);
                    }
                }
            }
        });

        userPhoto = findViewById(R.id.regaddimage);

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }else{
                    openGalerry();
                }
            }
        });

        setPtype = 1;
        txttype = (TextView) findViewById(R.id.titype);
        txttype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setPtype==1){
                    setPtype=0;
                    userPassword.setTransformationMethod(null);
                    if(userPassword.getText().length()>0)
                        userPassword.setSelection(userPassword.getText().length());
                    txttype.setBackgroundResource(R.drawable.eye);
                }else{
                    setPtype=1;
                    userPassword.setTransformationMethod(new PasswordTransformationMethod());
                    if(userPassword.getText().length()>0)
                        userPassword.setSelection(userPassword.getText().length());
                    txttype.setBackgroundResource(R.drawable.hide);
                }
            }
        });

        setPtype2 = 1;
        txttype2 = (TextView) findViewById(R.id.hide2);
        txttype2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setPtype2==1){
                    setPtype2=0;
                    userPassowrd2.setTransformationMethod(null);
                    if (userPassowrd2.getText().length()>0)
                        userPassowrd2.setSelection(userPassowrd2.getText().length());
                    txttype2.setBackgroundResource(R.drawable.eye);
                }else {
                    setPtype2 = 1;
                    userPassowrd2.setTransformationMethod(new PasswordTransformationMethod());
                    if(userPassowrd2.getText().length()>0)
                        userPassowrd2.setSelection(userPassowrd2.getText().length());
                    txttype2.setBackgroundResource(R.drawable.hide);
                }
            }
        });
    }

    private void CreateUserAccount(String email, final String name, String pass) {
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showMessage("Account created");
                    updateUserInfo(name,pickedimageuri,mAuth.getCurrentUser());
                }else{
                    showMessage("Account created failed "+ task.getException().getMessage());
                    regbtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUserInfo(final String name, Uri pickedimageuri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilePath = mStorage.child(pickedimageuri.getLastPathSegment());
        imageFilePath.putFile(pickedimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder().setDisplayName(name).setPhotoUri(uri).build();

                        currentUser.updateProfile(profileupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    showMessage("Register Complete");
                                    updateUI();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void updateUI() {
        Intent i = new Intent(getApplicationContext(), dashboard.class);
        startActivity(i);
        finish();
    }

    private void showMessage(String Message) {
        Toast.makeText(getApplicationContext(),Message, Toast.LENGTH_LONG).show();
    }

    private void openGalerry() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(register.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(register.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(register.this,"Please accept for required permission", Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(register.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }else{
            openGalerry();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedimageuri = data.getData();
            userPhoto.setImageURI(pickedimageuri);
        }
    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
