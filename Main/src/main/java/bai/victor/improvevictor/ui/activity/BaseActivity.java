package bai.victor.improvevictor.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import bai.victor.improvevictor.R;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class BaseActivity extends AppCompatActivity{
    private boolean isShowRight;


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

    public void initToolBar(Toolbar toolbar, String title, boolean showHomeAsUp){
        initToolBar(toolbar, title, showHomeAsUp, false);
    }

    public void initToolBar(Toolbar toolbar, String title, boolean showHomeAsUp, boolean isShowRight){
        this.isShowRight = isShowRight;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isShowRight){
            getMenuInflater().inflate(R.menu.toolbar_right, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
