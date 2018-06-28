package cakart.cakart.in.quiz_app.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import cakart.cakart.in.quiz_app.register.LoginActivity;
import cakart.cakart.in.quiz_app.R;
import cakart.cakart.in.quiz_app.db.MyDatabaseHelper;

public class DeckListActivity extends AppCompatActivity {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
      @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);



        NavigationView nvDrawer =findViewById(R.id.nv);
        mDrawerlayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);


        CaFoundationQuiz c = new CaFoundationQuiz();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, c).commit();

    }

       public void selectedItemDrawer(MenuItem menuItem) {
        android.support.v4.app.Fragment myFragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId()) {

            case R.id.quiz:
                fragmentClass = CaFoundationQuiz.class;
                break;

            case R.id.logout:
                SharedPreferences s = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = s.edit();
                ed.clear();
                ed.commit();
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                        "app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = sharedPref.edit();
                edt.clear();
                edt.commit();
                getApplicationContext().deleteDatabase(MyDatabaseHelper.DATABASE_NAME);
                Intent i = new Intent(DeckListActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return;
            default:
                fragmentClass = CaFoundationQuiz.class;
        }
        try {
            myFragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
    }


    @Override
    protected void onResume() {
        super.onResume();
//        showList();
    }




    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerlayout.closeDrawers();
                selectedItemDrawer(item);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
