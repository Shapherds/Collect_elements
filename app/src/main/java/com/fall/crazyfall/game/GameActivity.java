package com.fall.crazyfall.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;


import com.fall.crazyfall.databinding.ActivityGameBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class GameActivity extends AppCompatActivity {
    ActivityGameBinding binding;

    Display mdisp;
    Point mdispSize;
    int maxX;
    int maxY;
    private long duration = 1500;

    int level = 1;
    boolean isWin = false;
    private boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mdisp = getWindowManager().getDefaultDisplay();
        mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x - 300;
        maxY = mdispSize.y - 600;
    }

    public void setStartConfig(ImageView im) {
        im.setVisibility(View.VISIBLE);
        im.setTranslationX((float) Math.random() * maxX - 400);
        im.setTranslationY((float) Math.random() * maxY - 300);
    }

    public void tup(View view) {
        view.setVisibility(View.INVISIBLE);
        if (binding.coin1.getVisibility() == binding.coin2.getVisibility()
                && binding.coin1.getVisibility() == binding.coin3.getVisibility()
                && binding.coin1.getVisibility() == binding.coin4.getVisibility()
                && binding.coin1.getVisibility() == binding.coin5.getVisibility()
                && binding.coin1.getVisibility() == binding.coin6.getVisibility()
                && binding.coin1.getVisibility() == View.INVISIBLE) {
            gameOver();
        }
    }

    private void gameOver() {
        isWin = true;
        result();
        level++;
        if (duration > 500) {
            duration -= 200;
        }
        play(binding.button);
    }

    private void result() {
        MyDialogFragment myDialogFragment = new MyDialogFragment(this, level);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

    private void startAnimation(ImageView coin1) {
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(coin1,
                PropertyValuesHolder.ofFloat("translationY", (float) Math.random() * maxY),
                PropertyValuesHolder.ofFloat("translationX", (float) Math.random() * maxX));
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startAnimation(coin1);
            }

        });
        animator.start();
    }

    public void play(View view) {
        view.setVisibility(View.GONE);
        setStartConfig(binding.coin1);
        setStartConfig(binding.coin2);
        setStartConfig(binding.coin3);
        setStartConfig(binding.coin4);
        setStartConfig(binding.coin5);
        setStartConfig(binding.coin6);
        if (firstStart) {
            firstStart = false;
            startAnimation(binding.coin1);
            startAnimation(binding.coin2);
            startAnimation(binding.coin3);
            startAnimation(binding.coin4);
            startAnimation(binding.coin5);
            startAnimation(binding.coin6);
        }
    }
}