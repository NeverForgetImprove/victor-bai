package bai.victor.improvevictor.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
        initEvents();
    }

    protected void setContentView() {
    }

    protected void initViews() {
    }

    protected void initEvents() {
    }

    public void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
