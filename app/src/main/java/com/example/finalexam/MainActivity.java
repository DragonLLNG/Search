package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, RegisterFragment.SignUpListener,
        SearchFragment.SearchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        if(mAuth.getCurrentUser() == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new SearchFragment())
                    .commit();
        }


    }

    @Override
    public void gotoSignUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new RegisterFragment())
                .commit();

    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();

    }

    @Override
    public void gotoFavorite() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new MyFavoritesFragment())
                .commit();
    }

    @Override
    public void gotoDetail(Image data) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new PhotoDetailsFragment().newInstance(data))
                .commit();
    }

    @Override
    public void gotoSearch() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new SearchFragment())
                .commit();
    }
}
