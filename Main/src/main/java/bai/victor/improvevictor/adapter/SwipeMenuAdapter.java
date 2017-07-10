package bai.victor.improvevictor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bai.victor.improvevictor.R;
import bai.victor.improvevictor.bean.SwipeMenuBean;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class SwipeMenuAdapter extends RecyclerView.Adapter<SwipeMenuAdapter.SwipeHolder>{

    private Context mContext;
    private List<SwipeMenuBean> mList;

    public SwipeMenuAdapter(Context context, List<SwipeMenuBean> list){
        mContext = context;
        mList = list;
    }

    @Override
    public SwipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_swipe_menu, parent, false);
        return new SwipeHolder(view);
    }

    @Override
    public void onBindViewHolder(SwipeHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class SwipeHolder extends RecyclerView.ViewHolder{

        public SwipeHolder(View itemView) {
            super(itemView);
        }
    }
}
