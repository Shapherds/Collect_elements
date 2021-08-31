package com.fall.crazyfall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import com.fall.crazyfall.databinding.ActivityMainBinding;
import com.fall.crazyfall.game.GameInfo;
import com.fall.crazyfall.web.WebCommunication;

public class MainActivity extends AppCompatActivity {
    private int pStatus = 0;
    ActivityMainBinding binding;

    private Handler handler = new Handler();
    public static boolean isGame = true;
    private boolean isfirst = true;
    private SharedPreferences.Editor myEditor;
    private SharedPreferences myPreferences;
    public static MainActivity activity;

    void startProgress() {
        new Thread(() -> {
            while (true) {
                handler.post(() -> binding.progressBar.setProgress(pStatus));
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pStatus++;
                if (pStatus == 100) {
                    pStatus = 0;
                }
            }
        }).start();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startProgress();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = this;
        myPreferences = this.getSharedPreferences(StaticData.SHARED_PREF_NAME.getData(), MODE_PRIVATE);
        myEditor = myPreferences.edit();
        new Handler().postDelayed(() -> {
            if (isGame) {
                Intent myIntent = new Intent(this, GameInfo.class);
                startActivity(myIntent);
            }
        }, 7 * 1000);
    }

    @SuppressLint("CommitPrefEdits")
    public void initWebData(String deep) {
        if (isfirst) {
            isfirst = false;
            WebCommunication webCommunication = new WebCommunication(this, myPreferences, this, myEditor, deep);
            webCommunication.getGeo();
            webCommunication.regEvent();
        }
    }
}