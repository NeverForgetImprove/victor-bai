package bai.victor.improvevictor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bai.victor.improvevictor.R;
import bai.victor.improvevictor.bean.SwipeMenuBean;
import bai.victor.module.BezierRedPoint;
import bai.victor.module.SwipeMenuLayout;

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
    public void onBindViewHolder(final SwipeHolder holder, int position) {
        final SwipeMenuBean bean = mList.get(position);
        if (bean != null){
            holder.unread_point.setVisibility(bean.unreadNum != 0 ? View.VISIBLE : View.INVISIBLE);
            holder.unread_point.setText(String.valueOf(bean.unreadNum));
            holder.unread_point.setOnDragListener(new BezierRedPoint.OnDragStatusListener() {
                @Override
                public void onDrag() {

                }

                @Override
                public void onMove() {

                }

                @Override
                public void onRestore() {

                }

                @Override
                public void onDismiss() {
                    bean.unreadNum = 0;
                }
            });
            holder.tv_desc.setText(bean.desc);
            holder.tv_to_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.swipe_menu.isMenuOpen()){
                        holder.swipe_menu.smoothToCloseMenu();
                    }
                    Toast.makeText(mContext, "置顶", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_to_read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.swipe_menu.isMenuOpen()){
                        holder.swipe_menu.smoothToCloseMenu();
                    }
                    Toast.makeText(mContext, "设为已读", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_to_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.swipe_menu.isMenuOpen()){
                        holder.swipe_menu.smoothToCloseMenu();
                    }
                    Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
                }
            });
            holder.swipe_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, bean.desc, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class SwipeHolder extends RecyclerView.ViewHolder{
        private TextView tv_to_top, tv_to_read, tv_to_delete;
        private TextView tv_desc;
        private SwipeMenuLayout swipe_menu;
        private BezierRedPoint unread_point;

        public SwipeHolder(View itemView) {
            super(itemView);
            swipe_menu = (SwipeMenuLayout) itemView.findViewById(R.id.swipe_menu);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            unread_point = (BezierRedPoint) itemView.findViewById(R.id.unread_red_point);
            tv_to_top = (TextView) itemView.findViewById(R.id.tv_to_top);
            tv_to_read = (TextView) itemView.findViewById(R.id.tv_to_read);
            tv_to_delete = (TextView) itemView.findViewById(R.id.tv_to_delete);
        }
    }
}
