package bai.victor.improvevictor.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bai.victor.improvevictor.R;
import bai.victor.improvevictor.ui.BaseFragment;
import bai.victor.improvevictor.ui.activity.SwipeMenuActivity;

/**
 * Created by Victor-Bai on 2017/7/19.
 */

public class Bai_Fragment extends BaseFragment{
    private TextView mJumpInfo;
    private Button mSwipeBtn;
    String url;

    @Override
    public void initDatas() {
        Intent intent = getActivity().getIntent();
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
    }

    @Override
    protected int createView() {
        return R.layout.fragment_bai_layout;
    }


    @Override
    public void initViews(View view) {
        mSwipeBtn = (Button) view.findViewById(R.id.btn_swipe);
        mJumpInfo = (TextView) view.findViewById(R.id.jump);

        mSwipeBtn.setOnClickListener(this);
        if (url != null){
            mJumpInfo.setText(url);
        }
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.btn_swipe){
            Intent intent = new Intent(getContext(), SwipeMenuActivity.class);
            startActivity(intent);
        }
    }

}
