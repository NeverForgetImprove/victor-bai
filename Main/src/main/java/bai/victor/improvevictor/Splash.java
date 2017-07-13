package bai.victor.improvevictor;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bai.victor.improvevictor.ui.activity.SwipeMenuActivity;
import bai.victor.improvevictor.widget.RoundImageView;

public class Splash extends AppCompatActivity {

    private ImageView mBg;
    private RoundImageView mIcon;
    private TextView mCopryright;
    private TextView mTimer;

    private int time = 5;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTimer.setText("跳过（"+ time-- +"）");
                    SystemClock.sleep(1000);
                }
            });
            if (time < 0){
                enterMainActivity();
            }else {
                handler.post(this);
            }
        }
    };
    private void initEvent() {
        handler.post(timerRunnable);
    }

    private void initView() {
        handler = new Handler();

        mBg = (ImageView) findViewById(R.id.iv_splash_bg);
        mIcon = (RoundImageView) findViewById(R.id.iv_icon);
        mCopryright = (TextView) findViewById(R.id.tv_copyright);
        mTimer = (TextView) findViewById(R.id.tv_timer);
    }

    public void enterMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
