package xiake.vscreenshot.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xiake.db.entity.FriendAbout;
import xiake.vscreenshot.R;
import xiake.vscreenshot.adapter.ContactAdapter;
import xiake.vscreenshot.adapter.RecyclerViewAdapter;
import xiake.vscreenshot.expand.StickyRecyclerHeadersDecoration;
import xiake.vscreenshot.util.pinyin.CharacterParser;
import xiake.vscreenshot.util.pinyin.PinyinComparator;
import xiake.vscreenshot.widget.DividerDecoration;
import xiake.vscreenshot.widget.SideBar;


/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class ChoiceFriendActivity extends BaseActivity {
    private static final String TAG = "ChoiceFriendActivity";
    public RecyclerView mContactList;
    public SideBar mSideBar;
    public CharacterParser mCharacter;
    public PinyinComparator mPinyinComparator;
    private TextView mUserDialog;
    private ContactAdapter mAdapter;
    private List<FriendAbout> mAllLists = new ArrayList<>();
    public List<FriendAbout> mFriendAbouts = new ArrayList<>();
    public List<FriendAbout> mOtherFriendAbouts = new ArrayList<>();

    @Override
    protected void setTitle() {
        findViewById(R.id.choice_friend_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        //查询数据库获取所有联系人
        mFriendAbouts = mDaoSession.getFriendAboutDao().loadAll();
        mContactList = (RecyclerView) findViewById(R.id.choice_friend_list);
        mSideBar = (SideBar) findViewById(R.id.contact_sidebar);
        mUserDialog = (TextView) findViewById(R.id.contact_dialog);


        initData();

    }


    private void initData() {
        //右侧索引条
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mContactList.getLayoutManager().scrollToPosition(position);

                }

            }
        });
        ((Button) findViewById(R.id.add_we_chat_friend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加微信角色
                Intent intent = new Intent(activity, AddFriendActivity.class);
                startActivity(intent);
            }
        });
        mAdapter = new ContactAdapter(activity);
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerViewAdapter.ClickableViewHolder holder) {
                choiceFriend(position);
            }
        });
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        int orientation = LinearLayoutManager.VERTICAL;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
        mContactList.setLayoutManager(layoutManager);
        mContactList.addItemDecoration(headersDecor);
        mContactList.addItemDecoration(new DividerDecoration(this));
        mContactList.setAdapter(mAdapter);
        mCharacter = CharacterParser.getInstance();
        mPinyinComparator = new PinyinComparator();
        mSideBar.setTextView(mUserDialog);

        setData(sort(mFriendAbouts));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //查询数据库获取所有联系人
        mFriendAbouts = mDaoSession.getFriendAboutDao().loadAll();
        Log.e("onResume: " + "\n", "-------" + mFriendAbouts.size());
        setData(sort(mFriendAbouts));
    }

    private void setData(List<FriendAbout> friendAbouts) {
        mAdapter.setDataSource(friendAbouts);
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 排序
     * @param friendAbouts
     */
    private List<FriendAbout> sort(List<FriendAbout> friendAbouts) {

        if (mOtherFriendAbouts != null) {
            mOtherFriendAbouts.clear();
        }
        if (mAllLists != null) {
            mAllLists.clear();
        }
        for (FriendAbout friend : friendAbouts) {
            String nickName = friend.getNickName();
            if (mCharacter.getSortLetters(nickName).equals("#")) {
                mOtherFriendAbouts.add(friend);
            }
        }
        mFriendAbouts.removeAll(mOtherFriendAbouts);
        Collections.sort(mFriendAbouts, mPinyinComparator);
        mFriendAbouts.addAll(mOtherFriendAbouts);
        mAllLists.addAll(mFriendAbouts);
        return mAllLists;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_choice_friend;
    }


    public void choiceFriend(int position) {
        Log.e(TAG, "choiceFriend: " + "\n" + "选择角色");
        String nickName = mFriendAbouts.get(position).getNickName();
        String imgPath = mFriendAbouts.get(position).getImgPath();
        Intent intent = new Intent();
        intent.putExtra("nickName", nickName);
        intent.putExtra("imgPath", imgPath);
        activity.setResult(RESULT_OK, intent);
        finish();
    }
}
