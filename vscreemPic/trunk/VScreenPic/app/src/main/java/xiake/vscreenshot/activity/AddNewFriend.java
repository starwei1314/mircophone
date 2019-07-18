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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xiake.db.entity.Data;
import xiake.vscreenshot.R;
import xiake.vscreenshot.bean.DefaultData;
import xiake.vscreenshot.db.DaoManager;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.Constant;
import xiake.vscreenshot.util.ToastUtil;

/**
 * 添加新朋友 、修改信息
 */

public class AddNewFriend extends BaseActivity implements View.OnClickListener {
    private static final int CHOOSE_PICTURE = 0;
    private static final int CROP_SMALL_PICTURE = 1;
    public EditText mNickName;
    public EditText mVerify;
    private boolean isAdded;
    private Button mButton;
    private Button delete;
    private Intent mIntent;
    private String TAG = "AddNewFriend";
    public ImageView mImg;
    public Switch mSwitch;
    private Dialog mDialog;
    private String mName = "";
    private String mPath = "";
    public DefaultData.NewFriendListBean mNewFriendListBean;
    public List<DefaultData.NewFriendListBean> mNewFriends = new ArrayList<>();
    public int mPosition;

    @Override
    protected void setTitle() {
        mIntent = getIntent();
        mPosition = mIntent.getIntExtra("position", -1);
        Data data = datas.get(0);
        mNewFriends = mGson.fromJson(data.getNew_friend_list(),new TypeToken<List<DefaultData.NewFriendListBean>>(){}.getType());
        if (mPosition !=-1){
            mNewFriendListBean = mNewFriends.get(mPosition);
        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.add_new_friend_title);
        ((ImageView) layout.findViewById(R.id.weChat_transfer_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mButton = ((Button) findViewById(R.id.add_new_friend));
        delete = ((Button) findViewById(R.id.delete_new_friend));
        TextView title_name = (TextView) layout.findViewById(R.id.weChat_transfer_title);
        if (mPosition == -1){
            title_name.setText("添加新朋友");
            mButton.setText("添加");
            delete.setVisibility(View.GONE);
        }else {
            title_name.setText("修改信息");
            mButton.setText("修改完成");
            delete.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void initView() {

        ((RelativeLayout) findViewById(R.id.add_new_r2)).setOnClickListener(this);
        mNickName = ((EditText) findViewById(R.id.add_new_nickName));


        mImg = ((ImageView) findViewById(R.id.add_new_friend_img));


        ((ImageView) findViewById(R.id.add_new_refresh)).setOnClickListener(this);
        mVerify = ((EditText) findViewById(R.id.add_new_verify));

        ((ImageView) findViewById(R.id.add_verify_refresh)).setOnClickListener(this);
        mSwitch = ((Switch) findViewById(R.id.new_friend_is_add));
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isAdded = isChecked;
            }
        });
        mButton.setOnClickListener(this);
        delete.setOnClickListener(this);
        initData();
    }

    private void initData() {

        if (mPosition==-1){
            getNickName();
            setRandomImg();
            mSwitch.setChecked(isAdded);
        }else {
            mNickName.setText(mNewFriendListBean.getName());
            mVerify.setText(mNewFriendListBean.getRequest_msg());
            mPath = mNewFriendListBean.getIcon();
            BitmapUtil.setImg(activity,mImg,mPath);
            isAdded = mNewFriendListBean.getRequest_state();
            mSwitch.setChecked(isAdded);
        }

    }

    private String getRid() {
        int s = (int) (Math.random() * CommonUtil.randomNum);
        String headportrait = "picture" + String.valueOf(s)+".jpg";
        Log.e("=====", headportrait);

        mPath = CommonUtil.defaultPath+headportrait;
//        String type = "drawable";
//        String packge = "xiake.vscreenshot";//包名
//        int Rid = getResources().getIdentifier(headportrait, type, packge);

        return headportrait;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_new_friend;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.delete_new_friend:
                deleteNewFriend();
                break;
            case R.id.add_new_r2:
                showChoiceImgDialog();
                break;
            case R.id.add_new_nickName:
                getNickName();
                break;
            case R.id.add_new_refresh:
                getNickName();

                break;
            case R.id.add_new_friend:
                addNewFriend();
                break;

            case R.id.random:
                //随机一个
                mDialog.dismiss();
                setRandomImg();

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

    private void deleteNewFriend() {
        new AlertDialog.Builder(activity)
                .setMessage("亲，确定要删除吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Data data = DaoManager.getInstance().CheckUpDataBase(activity).get(0);
                        mNewFriends.remove(mPosition);
                        data.setNew_friend_list(mGson.toJson(mNewFriends));
                        mDataDao.deleteAll();
                        mDataDao.insertOrReplace(data);
                        ToastUtil.show(activity, "删除成功", 1);
                        dialog.dismiss();
                        finish();
                    }
                }).create().show();
    }

    private void addNewFriend() {
        //添加：获取头像地址，昵称，备注（可为空）
        //添加后保存到角色库
        Data data = DaoManager.getInstance().CheckUpDataBase(activity).get(0);
        mName = mNickName.getText().toString().trim();
        int preId = getSharedPreferences("config", MODE_PRIVATE).getInt("id",0);
        String verify = mVerify.getText().toString();
        if (mName .equals("")){
            ToastUtil.show(activity,"请设置昵称",1);
            return;
        }else if (verify.equals("")){
            ToastUtil.show(activity,"请设置验证消息",1);
            return;
        } else if (mPosition==-1){

            DefaultData.NewFriendListBean newFriend = new DefaultData.NewFriendListBean("我是" + mName, isAdded,mName,-1,preId+1, mPath,0, "","2017-10-11 12:20",false,new ArrayList<DefaultData.ChatRecordBean>());
            mNewFriends.add(newFriend);
            String new_friend_list = mGson.toJson(mNewFriends);
            Log.e(TAG, "addNewFriend: " + "\n" +new_friend_list);
            data.setNew_friend_list(new_friend_list);
            getSharedPreferences("config",MODE_PRIVATE).edit().putInt("id",preId+1).commit();

        }else {
            mNewFriendListBean.setIcon(mPath);
            mNewFriendListBean.setName(mName);
            mNewFriendListBean.setRequest_msg(verify);
            mNewFriendListBean.setRequest_state(isAdded);
            mNewFriends.set(mPosition,mNewFriendListBean);
            String new_friend_list = mGson.toJson(mNewFriends);
            String new_friend = mGson.toJson(mNewFriends.get(mPosition));
            Log.e(TAG, "addNewFriend: " + "\n"+new_friend);
            data.setNew_friend_list(new_friend_list);
            Log.e(TAG, "onClick: " + "\n" +"修改成功");
        }
        mDataDao.deleteAll();
        mDataDao.insertOrReplace(data);
        finish();
    }

    private void setRandomImg() {
        String rid = getRid();
        AssetManager assets = getAssets();
        InputStream open = null;

        try {
            open = assets.open("image/" + rid);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeStream(open, null, options);
            mImg.setImageBitmap(bitmap);
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


  }


    private void getNickName() {
        Random random = new Random();
        int length = Constant.nameStr.length;
        int i = random.nextInt(length - 1);
        mName = Constant.nameStr[i];
        mNickName.setText(mName);
        mNickName.setSelection(mNickName.getText().toString().length());
        mVerify.setText("我是"+mName);
    }

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
                    Intent intent = BitmapUtil.cutImage(uri); // 对图片进行裁剪处理
                    startActivityForResult(intent, CROP_SMALL_PICTURE);
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }else {
            Log.i(TAG, "onActivityResult: "+"获取相册图片失败");
        }
    }
    private void setImageToView() {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(BitmapUtil.cutImgUri));
            mPath = BitmapUtil.saveFile(activity,"crop",bitmap);
            mImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "setImageToView: \n"+e.toString() );
            Log.e(TAG, "setImageToView: \n"+BitmapUtil.cutImgUri );
        }

    }

}
