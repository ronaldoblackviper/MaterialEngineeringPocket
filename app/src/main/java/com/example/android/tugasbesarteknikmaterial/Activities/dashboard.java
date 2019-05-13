package com.example.android.tugasbesarteknikmaterial.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.tugasbesarteknikmaterial.Fragments.HomeFragment;
import com.example.android.tugasbesarteknikmaterial.Fragments.mtFragment;
import com.example.android.tugasbesarteknikmaterial.Models.Post;
import com.example.android.tugasbesarteknikmaterial.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog popAddpost;
    ImageView popup_userimage,popup_postimage,popup_addbutton;
    TextView popup_title,popup_desc;
    ProgressBar popup_progress;
    private Uri pickedimageuri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        
        popup_post();

        setUppopupimage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddpost.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    private void setUppopupimage() {
        popup_postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });
    }

    private void openGalerry() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(dashboard.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(dashboard.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(dashboard.this,"Please accept for required permission", Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(dashboard.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
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
            popup_postimage.setImageURI(pickedimageuri);
        }
    }

    private void popup_post() {
        popAddpost= new Dialog(this);
        popAddpost.setContentView(R.layout.popup_add_post);
        popAddpost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddpost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddpost.getWindow().getAttributes().gravity = Gravity.TOP;

        popup_userimage = popAddpost.findViewById(R.id.popup_userphoto);
        popup_title = popAddpost.findViewById(R.id.popup_title);
        popup_desc = popAddpost.findViewById(R.id.popup_description);
        popup_postimage = popAddpost.findViewById(R.id.popup_image);
        popup_addbutton = popAddpost.findViewById(R.id.popup_create);
        popup_progress = popAddpost.findViewById(R.id.popup_progressBar);

        Glide.with(dashboard.this).load(currentUser.getPhotoUrl()).into(popup_userimage);

        popup_addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_addbutton.setVisibility(View.INVISIBLE);
                popup_progress.setVisibility(View.VISIBLE);
                if(!popup_title.getText().toString().isEmpty()&&!popup_desc.getText().toString().isEmpty()&&pickedimageuri!=null){
                    if(!isConnected()){
                        showMessage("No internet connection");
                        popup_addbutton.setVisibility(View.VISIBLE);
                        popup_progress.setVisibility(View.INVISIBLE);
                    }else{
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("news_images");
                        final  StorageReference imagefilepath = storageReference.child(pickedimageuri.getLastPathSegment());
                        imagefilepath.putFile(pickedimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imagefilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imagedownloadlink = uri.toString();
                                        Post post = new Post(popup_title.getText().toString(),
                                                popup_desc.getText().toString(),
                                                imagedownloadlink,
                                                currentUser.getUid(),
                                                currentUser.getPhotoUrl().toString());

                                        addPost(post);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showMessage(e.getMessage());
                                        popup_progress.setVisibility(View.INVISIBLE);
                                        popup_addbutton.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                    }
                }else{
                    showMessage("Please verify all input fields and choose post image");
                    popup_addbutton.setVisibility(View.VISIBLE);
                    popup_progress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        String key = myRef.getKey();
        post.setPostKey(key);

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post added succesfully");
                popup_progress.setVisibility(View.INVISIBLE);
                popup_addbutton.setVisibility(View.VISIBLE);
                popAddpost.dismiss();
            }
        });
    }

    private void showMessage(String Message) {
        Toast.makeText(dashboard.this,Message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        } else if (id == R.id.nav_information) {
            getSupportActionBar().setTitle("Information");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new mtFragment()).commit();
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent login = new Intent(getApplicationContext(), com.example.android.tugasbesarteknikmaterial.Activities.login.class);
            startActivity(login);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUseremail = headerView.findViewById(R.id.nav_useremail);
        ImageView navUserphoto = headerView.findViewById(R.id.nav_userphoto);

        navUseremail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserphoto);
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
