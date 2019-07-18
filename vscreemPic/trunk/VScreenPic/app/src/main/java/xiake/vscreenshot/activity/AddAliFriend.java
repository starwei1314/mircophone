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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import xiake.db.dao.AliFriendDao;
import xiake.db.entity.AliFriend;
import xiake.vscreenshot.R;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.Constant;
import xiake.vscreenshot.util.ToastUtil;

/**
 * Created by Administrator on 2017/10/17. 0017.
 */
public class AddAliFriend extends BaseActivity implements View.OnClickListener {
    private static final int CHOOSE_PICTURE = 1;
    private static final String TAG = "AddAliFriend";
    private static final int CROP_SMALL_PICTURE = 2;
    public ImageView friendImg;
    public ImageView sjNum;
    public ImageView sjNick;
    public Switch mIsFriend;
    public TextView friendLv;
    public EditText friendNick;
    public EditText friendNum;
    public EditText freindRemark;
    public Button addFriend;
    public boolean isFriend;
    public Random mRandom;
    private Dialog mDialog;
    private String mPath = "";
    private Intent mIntent;
    private long mEditId;
    public TextView mTitle_name;
    public LinearLayout cOrdFriend;
    public Button cFriend;
    public Button dFriend;
    public String[] lv = {"大众会员","黄金会员","铂金会员","钻石会员",};
    public AliFriendDao mAliFriendDao;
    public AliFriend mAliFriend;
    public RelativeLayout friendMsg;

    @Override
    protected void setTitle() {

        LinearLayout title_layout = (LinearLayout) findViewById(R.id.add_ali_friend_title);
        mTitle_name = (TextView) title_layout.findViewById(R.id.weChat_transfer_title);
        mTitle_name.setText("添加角色");
        ((ImageView) title_layout.findViewById(R.id.weChat_transfer_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void initView() {

        //头像
        friendMsg = ((RelativeLayout) findViewById(R.id.ali_friend_base2));
        friendImg = ((ImageView) findViewById(R.id.ali_friend_img));
        sjNick = ((ImageView) findViewById(R.id.ali_friend_sj_nick));
        sjNum = ((ImageView) findViewById(R.id.ali_friend_sj_num));
        friendLv = ((TextView) findViewById(R.id.ali_friend_lv));

        mIsFriend = ((Switch) findViewById(R.id.ali_is_friend));

        friendNick = ((EditText) findViewById(R.id.ali_friend_nick));
        friendNum = ((EditText) findViewById(R.id.ali_friend_num));
        freindRemark = ((EditText) findViewById(R.id.ali_friend_mark));

        addFriend = ((Button) findViewById(R.id.ali_friend_add));

        cOrdFriend = ((LinearLayout) findViewById(R.id.ali_friend_cOrd));
        cFriend = ((Button) findViewById(R.id.ali_friend_change));
        dFriend = ((Button) findViewById(R.id.ali_friend_delete));

        initData();
        initListener();
    }

    /**
     * 初始化
     */
    private void initData() {
        mAliFriendDao = mDaoSession.getAliFriendDao();
        mRandom = new Random();
        mIntent = getIntent();
        mEditId = mIntent.getLongExtra("editId", -1);
        Log.e(TAG, "setTitle: " + "\n" + mEditId);
        if (mEditId==-1){
            mTitle_name.setText("添加新朋友");
            addFriend.setVisibility(View.VISIBLE);
            cOrdFriend.setVisibility(View.GONE);
            mIsFriend.setChecked(isFriend);
            friendLv.setText(lv[0]);
            setRandomImg();
            getRandomNick();
            getRandomNum();
        }else if (mEditId!=-1){
            mTitle_name.setText("修改信息");
            addFriend.setVisibility(View.GONE);
            cOrdFriend.setVisibility(View.VISIBLE);
            mAliFriend = mAliFriendDao.queryBuilder().where(AliFriendDao.Properties.Id.eq(mEditId)).unique();
            mPath = mAliFriend.getImg();
            BitmapUtil.setImg(activity,friendImg, mPath);
            friendNick.setText(mAliFriend.getNick());
            isFriend = mAliFriend.getIsFriend();
            mIsFriend.setChecked(isFriend);
            friendLv.setText(mAliFriend.getVipLv());
            friendNum.setText(mAliFriend.getNum());
            freindRemark.setText(mAliFriend.getMark());
        }
    }

    private void initListener() {
        friendMsg.setOnClickListener(this);
        sjNick.setOnClickListener(this);
        sjNum.setOnClickListener(this);
        friendLv.setOnClickListener(this);
        friendNick.setOnClickListener(this);
        friendNum.setOnClickListener(this);
        addFriend.setOnClickListener(this);
        cFriend.setOnClickListener(this);
        dFriend.setOnClickListener(this);
        mIsFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(b);
                isFriend = b;
            }
        });


    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_ali_friend;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ali_friend_base2:
                showPicDialog();
                break;
            case R.id.ali_friend_sj_nick:
                getRandomNick();
                break;
            case R.id.ali_friend_sj_num:
                getRandomNum();
                break;
            case R.id.ali_friend_lv:
                showLvDialog();
                break;
            case R.id.ali_friend_add:
                addNewFriend();
                break;
            case R.id.ali_friend_change:
                changeFriend();
                break;
            case R.id.ali_friend_delete:
                deleteFriend();
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

    private void deleteFriend() {
        new AlertDialog.Builder(activity)
                .setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtil.show(activity,"删除成功",1);
                        mAliFriendDao.delete(mAliFriend);
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();

    }

    private void changeFriend() {
        String nickString = friendNick.getText().toString();
        String numString = friendNum.getText().toString();
        String lvString = friendLv.getText().toString();
        String markString = freindRemark.getText().toString();
        if (nickString==null||nickString.equals("")){
            ToastUtil.show(activity,"请设置昵称",1);
            return;
        }
        if (nickString==null||nickString.equals("")){
            ToastUtil.show(activity,"请设置账号",1);
            return;
        }

        if (markString==null||markString.equals("")){
            markString = "";
        }
        mAliFriend.setImg(mPath);
        mAliFriend.setNick(nickString);
        mAliFriend.setIsFriend(isFriend);
        mAliFriend.setVipLv(lvString);
        mAliFriend.setNum(numString);
        mAliFriend.setMark(markString);
        mAliFriendDao.insertOrReplace(mAliFriend);
        ToastUtil.show(activity,"修改成功",1);
        finish();
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
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }else {
            Log.i(TAG, "onActivityResult: "+"获取相册图片失败");
        }
    }

    private void setImageToView(Intent data) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(BitmapUtil.cutImgUri));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Log.e(TAG, "setImageToView: \n"+width+"--"+height );
            mPath = BitmapUtil.saveFile(activity, "crop", bitmap);
            friendImg.setImageBitmap(bitmap);
            friendImg.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setRandomImg() {
        String rid = getRid();
        AssetManager assets = getAssets();
        InputStream open;

        try {
            open = assets.open("image/" + rid);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeStream(open, null, options);
            friendImg.setImageBitmap(bitmap);
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getRid() {
        int s = (int) (Math.random() * CommonUtil.randomNum);
        String headportrait = "picture" + String.valueOf(s)+".jpg";
        Log.e("=====", headportrait);
        mPath = CommonUtil.defaultPath+headportrait;
        return headportrait;
    }

    private void addNewFriend() {
        String nickString = friendNick.getText().toString();
        String numString = friendNum.getText().toString();
        String lvString = friendLv.getText().toString();
        String markString = freindRemark.getText().toString();
        if (nickString==null||nickString.equals("")){
            ToastUtil.show(activity,"请设置昵称",1);
            return;
        }
        if (nickString==null||nickString.equals("")){
            ToastUtil.show(activity,"请设置账号",1);
            return;
        }

        if (markString==null||markString.equals("")){
            markString = "";
        }
        AliFriend aliFriend = new AliFriend();
        aliFriend.setImg(mPath);
        aliFriend.setNick(nickString);
        aliFriend.setIsFriend(isFriend);
        aliFriend.setVipLv(lvString);
        aliFriend.setNum(numString);
        aliFriend.setMark(markString);
        mAliFriendDao.insertOrReplace(aliFriend);
        ToastUtil.show(activity,"添加成功",1);
        finish();
    }

    private void showLvDialog() {
        new AlertDialog.Builder(activity)
                .setItems(lv, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        friendLv.setText(lv[i]);
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

    }

    private void getRandomNum() {
        int i = mRandom.nextInt(2);
        if (i==0){
            friendNum.setText(getTel());
            friendNum.setSelection(getTel().length());
        }else {
            String email = getEmail(5, 11);
            friendNum.setText(email);
            friendNum.setSelection(email.length());
        }

    }
    public static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static String qq = "0123456789";
    private static final String[] email_suffix={"@gmail.com","@yahoo.com","@msn.com","@hotmail.com",
            "@aol.com","@ask.com","@live.com","@qq.com"};
    /**
     * 返回Email
     * @param lMin 最小长度
     * @param lMax 最大长度
     * @return
     */
    public static String getEmail(int lMin,int lMax) {
        String emai = "";
        int length=getNum(lMin,lMax);
        StringBuffer sb = new StringBuffer();
        int x = (int) (Math.random() * email_suffix.length);
        sb = getSb(length, sb, x);
        emai = sb.append(email_suffix[x]).toString();
        Log.e(TAG, "getEmail: " + "\n" +emai);
        return emai;
    }

    private static StringBuffer getSb(int length, StringBuffer sb, int x) {
        if (x==email_suffix.length-1){
            int qq_length=getNum(5,11);
            for (int i = 0; i < qq_length; i++) {
                int number = (int)(Math.random()*qq.length());
                if (i==0&&number==0){
                    number = (int)(Math.random()*qq.length()-1)+1;
                }
                sb.append(qq.charAt(number));
            }
        }else {
            for (int i = 0; i < length; i++) {
                int number = (int)(Math.random()*base.length());
                sb.append(base.charAt(number));
            }
        }
        return sb;
    }

    /**
     * 返回手机号码
     */
    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    private static String getTel() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }
    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

    /**
     * 随机昵称
     */
    private void getRandomNick() {

        int length = Constant.nameStr.length;
        int i = mRandom.nextInt(length - 1);
        String nameString = Constant.nameStr[i];
        friendNick.setText(nameString);
        friendNick.setSelection(nameString.length());
    }

    /**
     * 选择图片
     */
    private void showPicDialog() {
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
}
