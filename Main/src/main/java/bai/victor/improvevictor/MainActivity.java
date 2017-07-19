package bai.victor.improvevictor;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import bai.victor.improvevictor.ui.BaseActivity;
import bai.victor.improvevictor.ui.fragment.Bai_Fragment;
import bai.victor.improvevictor.ui.fragment.Hao_Fragment;
import bai.victor.improvevictor.ui.fragment.Other_Fragment;
import bai.victor.improvevictor.ui.fragment.Victor_Fragment;
import bai.victor.improvevictor.ui.fragment.Ya_Fragment;

/**
 * Created by Victor-Bai on 2017/7/13.
 */

public class MainActivity extends BaseActivity{
    public static final int VICTOR = 0;
    public static final int BAI = 1;
    public static final int YA = 2;
    public static final int HAO = 3;
    public static final int OTHER = 4;
    private Fragment currentFragment;// 当前显示的 fragment
    private int currentPos = -1;// 当前显示的 fragmentId
    private int[] titles = new int[]{
            R.string.app_name,
            R.string.bai,
            R.string.ya,
            R.string.hao,
            R.string.others
    };
    private long lastBackPressedTime;

    private NavigationView navigationView;
    private DrawerLayout drawer_layout;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolBar(toolbar, getString(R.string.app_name),false,false);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, 0, 0);
        drawerToggle.syncState();
        selectItem(BAI);
    }

    @Override
    protected void initEvents() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.my_navigation_0:
                    selectItem(VICTOR);
                    break;
                case R.id.my_navigation_1:
                    selectItem(BAI);
                    break;
                case R.id.my_navigation_2:
                    selectItem(YA);
                    break;
                case R.id.my_navigation_3:
                    selectItem(HAO);
                    break;
                case R.id.my_navigation_4:
                    selectItem(OTHER);
                    break;
            }
            drawer_layout.closeDrawer(Gravity.START);
            return true;
        });
    }

    private void selectItem(int pos){
        updateTitle(getString(titles[pos]));
        // 点击的正是当前显示的，直接返回
        if (currentPos == pos) return;

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment != null){
            // 隐藏当前显示的 fragment
            transaction.hide(currentFragment);
        }
        currentPos = pos;
        Fragment fragment = manager.findFragmentByTag(getTag(pos));
        // 通过 manager 判断是否已存在目标 fragment ,若存在直接 show ， 否则去 add
        if (fragment != null){
            transaction.show(fragment);
            currentFragment = fragment;
        }else {
            transaction.add(R.id.content_container, getFragment(pos), getTag(pos));
        }
        transaction.commitAllowingStateLoss();
    }

    private Fragment getFragment(int pos){
        switch (pos){
            case VICTOR:
                currentFragment = new Victor_Fragment();
                break;
            case BAI:
                currentFragment = new Bai_Fragment();
                break;
            case YA:
                currentFragment = new Ya_Fragment();
                break;
            case HAO:
                currentFragment = new Hao_Fragment();
                break;
            case OTHER:
                currentFragment = new Other_Fragment();
                break;
            default:
                currentFragment = new Bai_Fragment();
                break;
        }
        return currentFragment;
    }

    private String getTag(int pos){
        return "fg_tag_" + pos;
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
