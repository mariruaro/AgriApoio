package br.com.marianarv.agriapoio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {
    private int sleepTime=3;
    private ImageView logoImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        logoImg = (ImageView) findViewById(R.id.img_logo);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        logoImg.startAnimation(myanim);
        LogoLaucher logoLaucher = new LogoLaucher();
        logoLaucher.start();
    }

    private class LogoLaucher extends Thread{
        public void run(){
            try{
                sleep(1000*sleepTime);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            Intent intent = new Intent(SplashScreenActivity.this, PostListActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }
    }
}
