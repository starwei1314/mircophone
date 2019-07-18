package xiake.vscreenshot.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xiake.db.dao.AliFriendDao;
import xiake.db.entity.AliFriend;
import xiake.vscreenshot.R;
import xiake.vscreenshot.adapter.ContactAdapter2;
import xiake.vscreenshot.adapter.RecyclerViewAdapter;
import xiake.vscreenshot.expand.StickyRecyclerHeadersDecoration;
import xiake.vscreenshot.util.ToastUtil;
import xiake.vscreenshot.util.pinyin.CharacterParser;
import xiake.vscreenshot.util.pinyin.PinyinComparator2;
import xiake.vscreenshot.widget.DividerDecoration;
import xiake.vscreenshot.widget.SideBar;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class AliFriends extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AliFriends";
    public Button mAddFriend;
    public TextView mDeleteAll;
    public RecyclerView mContactList;
    private SideBar mSideBar;
    public ContactAdapter2 mAdapter2;
    public List<AliFriend> mAllLists = new ArrayList<>();
    public List<AliFriend> mAliFriends = new ArrayList<>();
    public List<AliFriend> mOtherFriendAbouts = new ArrayList<>();
    private CharacterParser mCharacter;
    private PinyinComparator2 mPinyinComparator;
    private TextView mUserDialog;
    public AliFriendDao mAliFriendDao;

    @Override
    protected void setTitle() {
        LinearLayout title = (LinearLayout) findViewById(R.id.ali_friend_title);
        title.findViewById(R.id.new_friend_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) title.findViewById(R.id.new_friend_tx)).setText("支付宝角色库");
        //清空角色库
        mDeleteAll = ((TextView) title.findViewById(R.id.new_friend_delete));
        initData();
    }

    private void initData() {

        mSideBar = (SideBar) findViewById(R.id.contact2_sidebar);
        mUserDialog = (TextView) findViewById(R.id.contact2_dialog);
        mCharacter = CharacterParser.getInstance();
        mPinyinComparator = new PinyinComparator2();
        mAliFriendDao = mDaoSession.getAliFriendDao();
        mAliFriends = mAliFriendDao.loadAll();
        int size = mAliFriends.size();
        sortList(mAliFriends);
        Log.e(TAG, "initData: " + "\n" +size);
        changeStatus();
    }

    private void changeStatus() {
        mAliFriends = mAliFriendDao.loadAll();
        int size = mAliFriends.size();
        if (size ==0){
            mDeleteAll.setTextColor(Color.GRAY);
            mDeleteAll.setClickable(false);
        }else {
            mDeleteAll.setTextColor(Color.WHITE);
            mDeleteAll.setClickable(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //查询数据库获取所有联系人
        mAliFriends = mAliFriendDao.loadAll();
        String json = mGson.toJson(mAliFriends);
        sortList(mAliFriends);
        setData(mAllLists);
        changeStatus();
    }

    @Override
    protected void initView() {
        //添加朋友
        mAddFriend = (Button) findViewById(R.id.ali_friends_add);
        mContactList = ((RecyclerView) findViewById(R.id.ali_friend_rl));
        mAdapter2 = new ContactAdapter2(activity);
        //右侧索引条
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                int position = mAdapter2.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mContactList.getLayoutManager().scrollToPosition(position);

                }

            }
        });
        mAdapter2.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerViewAdapter.ClickableViewHolder holder) {
                choiceFriend(position);
            }
        });

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter2);
        mAdapter2.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
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
        mContactList.setAdapter(mAdapter2);

        mSideBar.setTextView(mUserDialog);

        setData(mAllLists);
        initListener();
    }

    private void setData(List<AliFriend> friends) {
        mAdapter2.setDataSource(friends);
        mAdapter2.notifyDataSetChanged();
    }

    private List<AliFriend> sortList(List<AliFriend> aliFriends) {
        if (mOtherFriendAbouts != null) {
            mOtherFriendAbouts.clear();
        }
        if (mAllLists != null) {
            mAllLists.clear();
        }
        for (AliFriend friend : aliFriends) {
            String nickName = friend.getNick();
            if (mCharacter.getSortLetters(nickName).equals("#")) {
                mOtherFriendAbouts.add(friend);
            }
        }
        mAliFriends.removeAll(mOtherFriendAbouts);
        Collections.sort(mAliFriends, mPinyinComparator);
        mAliFriends.addAll(mOtherFriendAbouts);
        mAllLists.addAll(mAliFriends);
        return mAllLists;
    }

    private void choiceFriend(int position) {
        Log.e(TAG, "choiceFriend: " + "\n" + "选择角色");
        String nickName = mAllLists.get(position).getNick();
        String imgPath = mAllLists.get(position).getImg();
        String vipLv = mAllLists.get(position).getVipLv();
        String num = mAllLists.get(position).getNum();
        Intent intent = new Intent();
        intent.putExtra("nickName", nickName);
        intent.putExtra("imgPath", imgPath);
        intent.putExtra("vipLv", vipLv);
        intent.putExtra("num", num);
        activity.setResult(RESULT_OK, intent);
        finish();
    }

    private void initListener() {

        mDeleteAll.setOnClickListener(this);
        mAddFriend.setOnClickListener(this);

    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_ali_friends;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ali_friends_add:
                startActivity(new Intent(activity,AddAliFriend.class));
                break;
            case R.id.new_friend_delete:
                new AlertDialog.Builder(activity)
                        .setMessage("确定要清空吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAliFriendDao.deleteAll();
                                ToastUtil.show(activity,"清除成功",1);
                                changeStatus();
                            }
                        })
                        .create()
                        .show();

                break;
        }
    }
}
