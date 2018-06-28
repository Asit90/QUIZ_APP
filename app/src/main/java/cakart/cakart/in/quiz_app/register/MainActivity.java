package cakart.cakart.in.quiz_app.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cakart.cakart.in.quiz_app.R;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loding.....");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref=getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                if(pref.getString("email",null)==null){
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(MainActivity.this,HomePage.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2500);
    }
}

