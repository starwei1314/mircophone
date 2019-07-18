package xiake.vscreenshot.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import xiake.db.entity.Data;
import xiake.vscreenshot.R;
import xiake.vscreenshot.adapter.NewFriendAdapter;
import xiake.vscreenshot.adapter.RecyclerViewAdapter;
import xiake.vscreenshot.bean.DefaultData;
import xiake.vscreenshot.db.DaoManager;
import xiake.vscreenshot.util.ToastUtil;
import xiake.vscreenshot.widget.DividerDecoration;

/**
 * Created by Administrator on 2017/9/18 0018.
 * <p>
 * 新的朋友
 */
public class NewFriendActivity extends BaseActivity {


    private static final String TAG = "NewFriendActivity";
    public LinearLayout mLayout;
    public NewFriendAdapter mNewFriendAdapter;
    public List<DefaultData.NewFriendListBean> mNewFriends = new ArrayList<>();
    public ArrayList<String> mNewFriendsJson = new ArrayList<>();
    public TextView mEmpty;
    public ImageView addFriend;

    @Override
    protected void setTitle() {
        mLayout = (LinearLayout) findViewById(R.id.new_friend_title);
        mLayout.findViewById(R.id.new_friend_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) mLayout.findViewById(R.id.new_friend_tx)).setText("新的朋友");
        addFriend = ((ImageView) mLayout.findViewById(R.id.new_friend_add));
        addFriend.setVisibility(View.VISIBLE);
        mLayout.findViewById(R.id.new_ver_line2).setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {

        Data data = datas.get(0);
        mNewFriends = mGson.fromJson(data.getNew_friend_list(), new TypeToken<List<DefaultData.NewFriendListBean>>() {
        }.getType());
        RecyclerView newFriendList = (RecyclerView) findViewById(R.id.new_friend_list);
        newFriendList.setLayoutManager(new LinearLayoutManager(activity));
        mNewFriendAdapter = new NewFriendAdapter(activity);
        mNewFriendAdapter.setDataSource(mNewFriends);
        mNewFriendAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerViewAdapter.ClickableViewHolder holder) {
                Intent intent = new Intent(activity, AddNewFriend.class);
                String newFriendJson = mGson.toJson(mNewFriends.get(position));
                intent.putExtra("newFriendJson", newFriendJson);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        newFriendList.setAdapter(mNewFriendAdapter);
        newFriendList.addItemDecoration(new DividerDecoration(this));
        /**
         * 添加新的朋友
         */
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddNewFriend.class);
                startActivity(intent);
            }
        });
        /**
         * 清空列表
         */
        mEmpty = (TextView) mLayout.findViewById(R.id.new_friend_delete);
        setEmptyClickable();
        mEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        /**
         * 生成预览
         */
        ((Button) findViewById(R.id.new_friend_preview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewFriendsJson.clear();
                if (mNewFriends.size() == 0) {
                    ToastUtil.show(activity, "请先添加新朋友", 1);
                    return;
                }
                for (DefaultData.NewFriendListBean newFriend : mNewFriends) {
                    String newFriendJson = mGson.toJson(newFriend);
                    mNewFriendsJson.add(newFriendJson);
                }
                Intent intent = new Intent(activity, NewFriendPreviewActivity.class);
                intent.putStringArrayListExtra("newFriendJsons", mNewFriendsJson);
                startActivity(intent);
            }
        });

    }

    private void setEmptyClickable() {
        if (mNewFriends.size() == 0) {
            mEmpty.setTextColor(Color.GRAY);
            mEmpty.setClickable(false);
        } else {
            mEmpty.setTextColor(Color.WHITE);
            mEmpty.setClickable(true);
        }
    }

    /**
     * 弹出提示
     */
    private void showDeleteDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("提醒：")
                .setMessage("确定要清空吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        Data data = DaoManager.getInstance().CheckUpDataBase(activity).get(0);
                        mNewFriends.clear();
                        data.setNew_friend_list(mGson.toJson(mNewFriends));
                        mDataDao.deleteAll();
                        mDataDao.insertOrReplace(data);
                        setEmptyClickable();
                        dialog.dismiss();
                        ToastUtil.show(activity, "清除成功", 1);
                        mNewFriendAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Data data = DaoManager.getInstance().CheckUpDataBase(activity).get(0);
        mNewFriends = mGson.fromJson(data.getNew_friend_list(), new TypeToken<List<DefaultData.NewFriendListBean>>() {
        }.getType());
        setEmptyClickable();
        Log.e(TAG, "onResume: " + "\n" + "-----" + mNewFriends.size());
        mNewFriendAdapter.setDataSource(mNewFriends);
        mNewFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_new_friend;
    }

}
