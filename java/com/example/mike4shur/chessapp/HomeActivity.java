package com.example.mike4shur.chessapp;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.animation.AnimationUtils;


public class HomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void startSinglePlayerChessActivity(View v)
    {
        Intent intent = new Intent(this, PlayingChessActivity.class);
        intent.putExtra("ai",true);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public void startMultiPlayerChessActivity(View v)
    {
        Intent intent = new Intent(this, PlayingChessActivity.class);
        intent.putExtra("ai",false);
        startActivity(intent);
    }
}