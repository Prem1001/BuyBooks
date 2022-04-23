package com.george.booksapp;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.booksapp.databinding.ActivityMainBinding;

/**
 * this is the first UI screen when user enters the app
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflating the views on UI
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setting up bottom navigation click event listeners
        binding.navBottom.setOnNavigationItemSelectedListener(this::onItemSelected);
        binding.navBottom.setOnNavigationItemReselectedListener(this::onItemSelected);

        //when user enters, keeping the search item selected by default.
        binding.navBottom.setSelectedItemId(R.id.search);
    }


    /**
     * this method listens the click on bottom navigation tabs
     * it returns true only if it has consumed the click
     */
    public boolean onItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                return true;
            case R.id.profile:
                replaceFragment(new ProfileFragment());
                return true;
            case R.id.search:
                replaceFragment(new SearchFragment());
                return true;
            case R.id.messages:
                return true;
        }
        return false;
    }

    /**
     * this method replaces the fragments (pieces of ui, here all those screens that gets repalces when bottom navigation is clicked) inside activity container
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameSearch, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

}
