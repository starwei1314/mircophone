package xiake.vscreenshot.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2017/1/24.
 * RecyclerView基类
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ClickableViewHolder> {

    public OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;
    public List<T> mDataSource = new ArrayList<>();
    public Context context;

    /**
     * 对外提供条目点击监听的方法
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    /**
     * 对外提供条目长按点击监听的方法
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.itemLongClickListener = listener;
    }

    /**
     * 设置Recycler的数据源
     * @param dataSource
     */
    public void setDataSource(List<T> dataSource){
        this.mDataSource = dataSource;
    }

    /**
     * 添加新数据
     * @param newData
     */
    public void addData(List<T> newData){
        if (mDataSource==null){
            mDataSource= new ArrayList<>();
        }
        mDataSource.addAll(newData);
    }
    /**
     * 设置Recycler的count
     * @return
     */
    public int getItemCount(){
        return mDataSource==null?0:mDataSource.size();
    }

    /**
     * 设置context
     * @param context
     */
    public void bindContext(Context context){
        this.context = context;
    }

    /**
     * 获取context
     * @return
     */
    public Context getContext(){
        return context;
    }

    /**
     * 重写onBindViewHolder设置item的点击和长按事件的监听
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ClickableViewHolder holder, final int position) {
        holder.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener!=null){
                    itemClickListener.onItemClick(position,holder);
                }
            }
        });
        holder.getParentView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemLongClickListener != null){
                    itemLongClickListener.onItemLongClick(position, holder);
                }
                return true;
            }
        });

    }

    /**
     * item点击接口
     */
    public interface OnItemClickListener{
        void onItemClick(int position, ClickableViewHolder holder);
    }

    /**
     * item长按点击接口
     */
    public interface OnItemLongClickListener{
        boolean onItemLongClick(int position, ClickableViewHolder holder);
    }


    public static class ClickableViewHolder extends RecyclerView.ViewHolder{
        private final View parentView;

        public ClickableViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
        }

        public View getParentView(){
            return parentView;
        }

        public <T extends View> T getView(@IdRes int id){
            return (T) parentView.findViewById(id);
        }
    }
}
