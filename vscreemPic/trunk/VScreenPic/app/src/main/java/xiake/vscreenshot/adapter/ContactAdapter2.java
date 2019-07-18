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

import xiake.db.entity.AliFriend;
import xiake.vscreenshot.R;
import xiake.vscreenshot.activity.AddAliFriend;
import xiake.vscreenshot.expand.StickyRecyclerHeadersAdapter;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.pinyin.CharacterParser;

/**
 *
 */
public class ContactAdapter2 extends RecyclerViewAdapter<AliFriend> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {



    private Context mContext;
    public CharacterParser mCharacterParser = new CharacterParser();
    public char mChar;
    public static String TAG = "ContactAdapter2";

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public ContactAdapter2(Activity activity) {
        this.mContext = activity;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onBindViewHolder: " + "\n" +"--------0000------");
        bindContext(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: " + "\n" +"-------1111-------");
        AliFriend friend = mDataSource.get(position);
        if (holder instanceof ContactViewHolder){
            Log.e(TAG, "onBindViewHolder: " + "\n" +"-------2222-------");
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.mName.setText(friend.getNick());
            contactViewHolder.mMark.setText(friend.getMark());
            String imgPath = friend.getImg();
            BitmapUtil.setImg(((Activity) mContext),contactViewHolder.mImg,imgPath);

            contactViewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AddAliFriend.class);
                    intent.putExtra("editId",mDataSource.get(position).getId());
                    mContext.startActivity(intent);
                }
            });

        }

        super.onBindViewHolder(holder, position);
    }

    @Override
    public long getHeaderId(int position) {
        String nickName = mDataSource.get(position).getNick();
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
            String nickName = mDataSource.get(i).getNick();
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


