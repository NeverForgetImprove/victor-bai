package bai.victor.improvevictor.ui.activity;

import android.support.v7.widget.LinearLayoutManager;

import bai.victor.improvevictor.R;
import bai.victor.improvevictor.adapter.SwipeMenuAdapter;
import bai.victor.improvevictor.bean.DataFactory;
import bai.victor.improvevictor.bean.SwipeMenuBean;
import bai.victor.module.SwipeRecyclerView;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class SwipeMenuActivity extends BaseActivity{

    SwipeRecyclerView mSwipeRecyclerView;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_swipe_menu);
    }

    @Override
    protected void initViews() {
        mSwipeRecyclerView = (SwipeRecyclerView) findViewById(R.id.swipe_recyclerview);
        mSwipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeMenuAdapter adapter = new SwipeMenuAdapter(this, DataFactory.createListData(SwipeMenuBean.class));
        mSwipeRecyclerView.setAdapter(adapter);
    }
}
