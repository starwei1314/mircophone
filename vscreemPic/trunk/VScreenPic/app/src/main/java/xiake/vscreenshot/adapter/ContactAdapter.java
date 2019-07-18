/**
 #
 */

package xiake.vscreenshot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xiake.db.entity.FriendAbout;
import xiake.vscreenshot.R;
import xiake.vscreenshot.activity.AddFriendActivity;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.pinyin.CharacterParser;

/**
 *
 * 根据当前权限进行判断相关的滑动逻辑
 */
public class ContactAdapter extends RecyclerViewAdapter<FriendAbout> implements xiake.vscreenshot.expand.StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    /**
     * 当前处于打开状态的item
     */

    private Context mContext;
    public CharacterParser mCharacterParser = new CharacterParser();
    public char mChar;


    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public ContactAdapter(Activity activity) {
        this.mContext = activity;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, final int position) {
        FriendAbout friendAbout = mDataSource.get(position);
        if (holder instanceof ContactViewHolder){
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.mName.setText(friendAbout.getNickName());
            contactViewHolder.mMark.setText(friendAbout.getRemark());
            String imgPath = friendAbout.getImgPath();
            BitmapUtil.setImg(((Activity) mContext),contactViewHolder.mImg,imgPath);

            contactViewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AddFriendActivity.class);
                    intent.putExtra("editId",mDataSource.get(position).getId());
                    mContext.startActivity(intent);
                }
            });

        }

        super.onBindViewHolder(holder, position);
    }

    @Override
    public long getHeaderId(int position) {
        String nickName = mDataSource.get(position).getNickName();
        String pinyin = mCharacterParser.getSelling(nickName);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")){
            mChar = sortString.charAt(0);
        }else {
            mChar = "#".charAt(0);
        }

        return mChar;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_header, parent, false);
                return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(String.valueOf(mChar));

    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String nickName = mDataSource.get(i).getNickName();
            String pinyin = mCharacterParser.getSelling(nickName);
            char ch = pinyin.substring(0, 1).toUpperCase().charAt(0);
            if (ch==section){
                Log.e( "getPositionForSection: ",  "\n" +i);
                return i;
            }
        }

        return -1;
    }


    public class ContactViewHolder extends ClickableViewHolder{

        public ImageView mImg;
        public TextView mName;
        public TextView mMark;
        public TextView mEdit;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mImg = ((ImageView) itemView.findViewById(R.id.item_contact_img));
            mName = (TextView) itemView.findViewById(R.id.item_contact_nick);
            mMark = (TextView) itemView.findViewById(R.id.item_contact_mark);
            mEdit = (TextView) itemView.findViewById(R.id.item_contact_edit);

        }

    }
}


