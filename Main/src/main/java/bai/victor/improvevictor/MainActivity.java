package bai.victor.improvevictor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import bai.victor.improvevictor.ui.activity.BaseActivity;
import bai.victor.improvevictor.ui.activity.SwipeMenuActivity;
import bai.victor.utils.LogUtils;

import static android.R.attr.data;

/**
 * Created by Victor-Bai on 2017/7/13.
 */

public class MainActivity extends BaseActivity{
    private long lastBackPressedTime;

    private NavigationView navigationView;

    private TextView mJumpInfo;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 接收从 H5 链接跳转的Intent
        Intent intent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action)){
            Uri uri = intent.getData();
            if (uri != null){
                String host = uri.getHost();
                String id = uri.getQueryParameter("id");
                String name = uri.getQueryParameter("name");
                String path = uri.getPath();
                url = "跳转的链接 \n"+"url:"+data + "\n host:"+host+"\n id:"+id+"\n name:"+name+"\n path:"+path;
            }
        }
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolBar(toolbar, getString(R.string.app_name),false);

        mJumpInfo = (TextView) findViewById(R.id.jump);
        if (url != null){
            mJumpInfo.setText(url);
        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, SwipeMenuActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (lastBackPressedTime + 2000 >= System.currentTimeMillis()){
            super.onBackPressed();
        }else {
            toast("再点一次退出应用！");
        }
        lastBackPressedTime = System.currentTimeMillis();
    }
}
