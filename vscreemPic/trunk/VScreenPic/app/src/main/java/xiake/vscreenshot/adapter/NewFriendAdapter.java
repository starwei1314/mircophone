package xiake.vscreenshot.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import xiake.vscreenshot.R;
import xiake.vscreenshot.activity.BaseActivity;
import xiake.vscreenshot.bean.DefaultData;
import xiake.vscreenshot.util.BitmapUtil;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class NewFriendAdapter extends RecyclerViewAdapter<DefaultData.NewFriendListBean> {

    public NewFriendAdapter(BaseActivity activity) {
        this.context = activity;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_new_friend, null, false);
        NewFriendViewHolder newFriendViewHolder = new NewFriendViewHolder(inflate);
        return newFriendViewHolder;
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof NewFriendViewHolder){
            DefaultData.NewFriendListBean newFriend = mDataSource.get(position);
            NewFriendViewHolder holder1 = (NewFriendViewHolder) holder;
            BitmapUtil.setImg(((Activity) context),holder1.mImg,newFriend.getIcon());
            holder1.mName.setText(newFriend.getName());
            holder1.mVerify.setText(newFriend.getRequest_msg());
            boolean isAdded = newFriend.getRequest_state();
            if (isAdded){
                holder1.mIsAdd.setVisibility(View.VISIBLE);
                holder1.mNotAdd.setVisibility(View.GONE);
            }else {
                holder1.mIsAdd.setVisibility(View.GONE);
                holder1.mNotAdd.setVisibility(View.VISIBLE);
            }
        }


        super.onBindViewHolder(holder, position);
    }

    public class NewFriendViewHolder extends ClickableViewHolder{
        public final ImageView mImg;
        public final TextView mName;
        public final TextView mVerify;
        public final TextView mIsAdd;
        public final Button mNotAdd;

        public NewFriendViewHolder(View itemView) {
            super(itemView);
            mImg = ((ImageView) getView(R.id.item_new_friend_img));
            mName = ((TextView) getView(R.id.item_new_friend_nick));
            mVerify = ((TextView) getView(R.id.item_new_friend_verify));
            mIsAdd = ((TextView) getView(R.id.item_new_friend_isAdd));
            mNotAdd = ((Button) getView(R.id.item_new_friend_notAdd));

        }
    }
}
