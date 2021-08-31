package com.fall.crazyfall.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fall.crazyfall.R;

public class GameInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
    }

    public void playGame(View view) {
        Intent myIntent = new Intent(this, GameActivity.class);
        startActivity(myIntent);
    }
}