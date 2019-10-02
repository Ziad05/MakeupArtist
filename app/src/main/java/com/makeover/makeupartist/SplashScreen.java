package com.makeover.makeupartist;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.makeover.makeupartist.Home.AutoWritingText;
import com.makeover.makeupartist.Home.ContentActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* Auto Writing Text */
        AutoWritingText appName = findViewById(R.id.appName);
        appName.setCharacterDelay(160);  /*Add a character every 150ms*/
        appName.animateText(getResources().getString(R.string.app_name));
        appName.setTypeface(Typeface.createFromAsset(getAssets(), "alex_brush.ttf"));

        /* Splash Screen */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, ContentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                overridePendingTransition(R.anim.activity_go_up,R.anim.activity_go_down);
            }
        },1500);
    }
}
