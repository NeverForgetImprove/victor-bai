package bai.victor.improvevictor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import bai.victor.improvevictor.ui.activity.BaseActivity;
import bai.victor.improvevictor.ui.activity.SwipeMenuActivity;

/**
 * Created by Victor-Bai on 2017/7/13.
 */

public class MainActivity extends BaseActivity{
    private TextView mJumpInfo;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 接收从 H5 链接跳转的Intent
        Intent intent = getIntent();
        String data = intent.getDataString();
        if (data != null){
            String[] split = data.split("data/");
            url = split.length > 0 ? split[1] : "no data";
        }
        initView();
    }

    private void initView() {
        mJumpInfo = (TextView) findViewById(R.id.jump);
        if (url != null){
            mJumpInfo.setText(url);
        }
    }

    public void onClick(View view){
        Intent intent = new Intent(this, SwipeMenuActivity.class);
        startActivity(intent);
    }
}
