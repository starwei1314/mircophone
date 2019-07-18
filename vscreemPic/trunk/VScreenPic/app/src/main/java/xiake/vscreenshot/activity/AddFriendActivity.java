package xiake.vscreenshot.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import xiake.db.dao.FriendAboutDao;
import xiake.db.entity.FriendAbout;
import xiake.vscreenshot.R;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.Constant;
import xiake.vscreenshot.util.ToastUtil;

/**
 * Created by Administrator on 2017/9/20 0020.
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener {
    private static final int CHOOSE_PICTURE = 2;
    private static final int CROP_SMALL_PICTURE = 3;
    private static final String TAG = "AddFriendActivity";
    private String mPath;
    private Uri tempUri;
    public ImageView friendImg;
    public Dialog mDialog;
    public FriendAboutDao mFriendAboutDao;
    public long mEditId;
    public Button mButton;
    public Button delete;
    public Intent mIntent;
    public String mName = "";

    @Override
    protected void setTitle() {
        mFriendAboutDao = mDaoSession.getFriendAboutDao();
        mIntent = getIntent();
        mEditId = mIntent.getLongExtra("editId", -1);

        LinearLayout layout = (LinearLayout) findViewById(R.id.add_friend_title);
        layout.findViewById(R.id.weChat_transfer_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mButton = ((Button) findViewById(R.id.add_friend));
        delete = ((Button) findViewById(R.id.delete_friend));
        if (mEditId==-1){
            ((TextView) layout.findViewById(R.id.weChat_transfer_title)).setText("添加角色");
            mButton.setText("添加");
            delete.setVisibility(View.GONE);

        }else if (mEditId!=-1){
            ((TextView) layout.findViewById(R.id.weChat_transfer_title)).setText("修改信息");
            mButton.setText("修改完成");
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FriendAbout friend = mFriendAboutDao.queryBuilder().where(FriendAboutDao.Properties.Id.eq(mEditId)).unique();
                    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                            .setTitle("提示")
                            .setMessage("亲，删除联系人后将清空相关聊天记录，确定要删除吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    mFriendAboutDao.delete(friend);
                                    ToastUtil.show(activity, "删除成功", 1);
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create();
                    alertDialog.setCancelable(true);
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }
            });
        }

    }

    @Override
    protected void initView()  {


        ((RelativeLayout) findViewById(R.id.r2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoiceImgDialog();
            }
        });
        friendImg = ((ImageView) findViewById(R.id.friend_img));
        final EditText nickName = (EditText) findViewById(R.id.nickName);//昵称
        getNickName(nickName);
        final EditText remark = (EditText) findViewById(R.id.remark);//备注
        //随机昵称
        ((ImageView) findViewById(R.id.add_friend_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNickName(nickName);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加：获取头像地址，昵称，备注（可为空）
                //添加后保存到角色库
                mName = nickName.getText().toString().trim();
                String mark = remark.getText().toString().trim();
                if (mName .equals("")){
                    ToastUtil.show(activity,"请设置昵称",1);
                }else if (mEditId==-1){
                    FriendAbout friendAbout = new FriendAbout();
                    friendAbout.setImgPath(mPath);
                    friendAbout.setNickName(mName);
                    friendAbout.setRemark(mark);
                    mFriendAboutDao.insertOrReplace(friendAbout);
//                    ToastUtil.show(activity,"添加成功",1);
                    finish();
                }else {
                    FriendAbout friend = mFriendAboutDao.queryBuilder().where(FriendAboutDao.Properties.Id.eq(mEditId)).unique();
                    friend.setImgPath(mPath);
                    friend.setNickName(mName);
                    friend.setRemark(mark);
                    mFriendAboutDao.insertOrReplace(friend);
                    Log.e(TAG, "onClick: " + "\n" +"修改成功");
                    finish();
                }
            }
        });

        if (mEditId==-1){
            //进来就随机设置一张图片
            String rid1 = null;
            try {

                AssetManager assets = getAssets();
                rid1 = getRid();

                InputStream open = assets.open("image/" + rid1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeStream(open, null, options);
                friendImg.setImageBitmap(bitmap);
               open.close();
        } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (mEditId!=-1){
            FriendAbout friend = mFriendAboutDao.queryBuilder().where(FriendAboutDao.Properties.Id.eq(mEditId)).unique();
            mPath = friend.getImgPath();
            BitmapUtil.setImg(activity,friendImg,mPath);
            nickName.setText(friend.getNickName());
            remark.setText(friend.getRemark());
        }
    }

    private void getNickName(EditText nickName) {
        Random random = new Random();
        int length = Constant.nameStr.length;
        int i = random.nextInt(length - 1);
        mName = Constant.nameStr[i];
        nickName.setText(mName);
        nickName.setSelection(nickName.getText().toString().length());
    }

    /**
     * 选择图片
     */
    private void showChoiceImgDialog() {
        mDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        Button choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        Button random = (Button) inflate.findViewById(R.id.random);
        Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);
        choosePhoto.setOnClickListener(this);
        random.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mDialog.setContentView(inflate);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        lp.width = dialogWindow.getWindowManager().getDefaultDisplay().getWidth()-20;
        dialogWindow.setAttributes(lp);
        mDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    Log.e("path",path);
                    Intent intent = BitmapUtil.cutImage(data.getData()); // 对图片进行裁剪处理
                    startActivityForResult(intent, CROP_SMALL_PICTURE);
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {

                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }else {
            Log.i(TAG, "onActivityResult: "+"失败");
        }
    }

    private void setImageToView(Intent data) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(BitmapUtil.cutImgUri));
            mPath = BitmapUtil.saveFile(activity, "crop", bitmap);
            friendImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.random:
                //随机一个
                mDialog.dismiss();
                try {
                    AssetManager assets = getAssets();
                    String rid = getRid();
                    InputStream open = assets.open("image/" + rid);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeStream(open, null, options);
                    friendImg.setImageBitmap(bitmap);
                   open.close();
             } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
            case R.id.choosePhoto:
                mDialog.dismiss();
                startActivityForResult(CommonUtil.getImg(), CHOOSE_PICTURE);
                break;
            case R.id.btn_cancel:
                mDialog.dismiss();
                break;

        }
    }

    /**
     * 获取随机图片资源
     * @return
     */
    private String getRid() throws IOException {
        int s = (int) (Math.random() * CommonUtil.randomNum);
        String headportrait = "picture" + String.valueOf(s)+".jpg";
        Log.e("=====", headportrait);
        mPath = CommonUtil.defaultPath+headportrait;
//        String type = "drawable";
//        String packge = "xiake.vscreenshot";//包名
//        int Rid = getResources().getIdentifier(headportrait, type, packge);

        return headportrait;
    }
}
