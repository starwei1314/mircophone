package xiake.vscreenshot.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import xiake.db.dao.FriendAboutDao;
import xiake.db.entity.FriendAbout;
import xiake.vscreenshot.R;

/**
 * Created by Administrator on 2017/9/23 0023.
 */
public class ChangeFriendActivity extends BaseActivity{

    public String mImgPath;
    public String mNickName;
    public String mRemark;
    public FriendAboutDao mFriendDao;
    public FriendAbout mFriend;

    @Override
    protected void setTitle() {
        LinearLayout  layout   = ((LinearLayout) findViewById(R.id.change_friend_title));
        ((TextView) layout.findViewById(R.id.weChat_transfer_title)).setText("修改信息");
        layout.findViewById(R.id.weChat_transfer_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void initView() {

        long editId = getIntent().getLongExtra("editId",-1);
        if (editId!=-1){
            mFriendDao = mDaoSession.getFriendAboutDao();
            mFriend = mFriendDao.queryBuilder().where(FriendAboutDao.Properties.Id.eq(editId)).unique();
            mImgPath = mFriend.getImgPath();
            mNickName = mFriend.getNickName();
            mRemark = mFriend.getRemark();

        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.change_friend;
    }
}
