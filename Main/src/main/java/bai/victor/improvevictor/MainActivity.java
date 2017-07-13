package bai.victor.improvevictor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import bai.victor.improvevictor.ui.activity.BaseActivity;
import bai.victor.improvevictor.ui.activity.SwipeMenuActivity;

/**
 * Created by Victor-Bai on 2017/7/13.
 */

public class MainActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, SwipeMenuActivity.class);
        startActivity(intent);
    }
}
