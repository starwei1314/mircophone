package xiake.vscreenshot.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import xiake.db.dao.DataDao;
import xiake.db.entity.Data;
import xiake.db.entity.User;
import xiake.vscreenshot.R;
import xiake.vscreenshot.bean.DefaultData;
import xiake.vscreenshot.db.DaoManager;
import xiake.vscreenshot.util.BitmapUtil;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.Constant;
import xiake.vscreenshot.util.DataCleanManager;
import xiake.vscreenshot.util.ShakeUtils;
import xiake.vscreenshot.util.ShellUtils;
import xiake.vscreenshot.util.ToastUtil;

import static xiake.vscreenshot.app.MyApplication.barHeight;

/**
 * Created by Administrator on 2017/9/18 0018.
 * <p>
 * 微信模拟器
 */
public class WeChatActivity extends BaseActivity implements View.OnClickListener {

    private static final int CHOOSE_PICTURE = 1;
    private static final String TAG = "WeChatActivity";
    private static final int CROP_SMALL_PICTURE = 2;
    public static final int RESULT_IMAGE = 3;
    private static final int GET_BG_IMG = 4;
    private String mPath = "";
    public WebView mWebView;
    public String mDatas;
    private boolean isKeyboardShowing;
    private float mHeight = 0;
    public ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    public int screenHeight;
    public ImageView mImageView;
    public DataDao dataDao;
    public int screenWidth;
    public PopupWindow mPopupWindow;
    public InputMethodManager mImm;
    public TextView mTitle;
    public ShakeUtils mShakeUtils;
    private Vibrator mVibrator;
    public RelativeLayout mSc_rl;
    public ImageView mScreenshotImg;
    private ArrayList<String> mImageList = new ArrayList<>();
    public ProgressBar mPb;
    public String bgPath;

    @Override
    protected void setTitle() {
        ((TextView) findViewById(R.id.bar)).setHeight(barHeight);
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        screenHeight = getScreenHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        dataDao = mDaoSession.getDataDao();
        createPopupWindow();
        mDatas = getSQData();
        Log.e(TAG, "init: " + "\n" + mDatas);
        mImm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        mShakeUtils = new ShakeUtils(this);

    }

    Handler mHandler = new Handler();
    long preTimeMillis = 0;
    String path = "";
    ShellUtils.CommandResult one = null;

    private void InitScreenshot() {
        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         */
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        mShakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long curTimeMillis = System.currentTimeMillis();
                        if (curTimeMillis - preTimeMillis > CommonUtil.SHOT_TIME) {//限制频率
                            preTimeMillis = curTimeMillis;
                            String time = String.valueOf(curTimeMillis);
                            Log.e(TAG, "run: " + "\n" + time);
                            path = "/sdcard/Vscreenshot/screenshot/" + time + ".png";
                            one = ShellUtils.execCommand("screencap -p " + path);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (one.result == 0) {

                                        /**
                                         * 四个参数就是——停止 开启 停止 开启
                                         * -1不重复，非-1为从pattern的指定下标开始重复
                                         */
                                        mVibrator.vibrate(new long[]{0, 50}, -1);
                                        //停止1秒，开启震动10秒，然后又停止1秒，又开启震动10秒，不重复.
                                        mSc_rl.setVisibility(View.VISIBLE);
                                        Glide.with(activity).load(path).asBitmap().into(mScreenshotImg);
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSc_rl.setVisibility(View.GONE);
                                            }
                                        }, 1500);

                                    } else {
                                        ToastUtil.show(activity, "截图失败,没有权限!!", 2);
                                    }
                                }
                            });
                        }

                    }
                }).start();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mShakeUtils.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShakeUtils.onPause();
    }

    @Override
    protected void onDestroy() {
        //清除本应用缓存
        super.onDestroy();
        //释放WebView资源
        mWebView.destroy();
    }

    @Override
    protected void initView() {
        mPb = ((ProgressBar) findViewById(R.id.weChat_pb));
        mTitle = ((TextView) findViewById(R.id.bar));
        mImageView = ((ImageView) findViewById(R.id.load_img));
        mWebView = (WebView) findViewById(R.id.weChat_wv);
        mSc_rl = ((RelativeLayout) findViewById(R.id.sc_rl));
        mScreenshotImg = ((ImageView) findViewById(R.id.screenshot_img));
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //        settings.setUseWideViewPort(true);
        //        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(this, "vjt");
        mWebView.loadUrl("file:///android_asset/wechat.html");
        mWebView.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().getPath());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mImageView.setVisibility(View.GONE);
            }

        });


        InitScreenshot();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                Log.e(TAG, "onTouchEvent: " + "\n" + event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: " + "\n" + event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent: " + "\n" + event.getY());
                break;
        }

        return super.onTouchEvent(event);

    }

    /**
     * 获取软键盘的高度
     */
    private void getSoftHeight() {
        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断窗口可见区域大小
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                mHeight = screenHeight - (rect.bottom - rect.top);
                int pxHeight = px2dp((int) mHeight) - 23;
                isKeyboardShowing = mHeight > screenHeight / 3;
                Log.e(TAG, "onGlobalLayout: " + "\n" + screenHeight + "\n" + mHeight + "\n" + pxHeight);
                if (isKeyboardShowing) {
                    mWebView.loadUrl("Javascript:getJpHeight('" + pxHeight + "')");
                } else {
                    mWebView.loadUrl("Javascript:getJpHeight('" + 0 + "')");
                }
            }
        };
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);


    }
    //    private int px2dp(int height) {
    //        float scale = getResources().getDisplayMetrics().density;
    //        Log.e(TAG, "px2dp: " + "\n" + scale);
    //        return (int) (height / scale + 0.5f);
    //    }

    private int px2dp(int height) {
        float scale = getResources().getDisplayMetrics().density;
        Log.e(TAG, "px2dp: " + "\n" + scale);
        return (int) (height / scale + 0.5f);
    }

    /**
     * 获取数据库的数据
     *
     * @return
     */
    public String getSQData() {
        Log.e(TAG, "getSQData: " + "\n" + "111111111");
        Data dataEntity = DaoManager.getInstance().CheckUpDataBase(activity).get(0);
        String my_info = dataEntity.getMy_info();
        String new_friend_list = dataEntity.getNew_friend_list();
        String all_friend = dataEntity.getAllfriend();
        DefaultData.MyInfoBean myInfoBean = mGson.fromJson(my_info, DefaultData.MyInfoBean.class);
        List<DefaultData.NewFriendListBean> newFriendList = mGson.fromJson(new_friend_list,
                new TypeToken<List<DefaultData.NewFriendListBean>>() {
                }.getType());
        List<DefaultData.AllfriendBean> allfriendList = mGson.fromJson(all_friend,
                new TypeToken<List<DefaultData.AllfriendBean>>() {
                }.getType());
        Log.e(TAG, "all_friend: " + "\n" + all_friend);
        DefaultData data = new DefaultData(myInfoBean, newFriendList, allfriendList);

        return mGson.toJson(data);
    }

    /**
     * 获取相片 设置头像
     */
    @JavascriptInterface
    public void getPhoto() {
        Log.e(TAG, "getPhoto: " + "\n" + "-------");
        //获取相册中的图片
        startActivityForResult(CommonUtil.getImg(), CHOOSE_PICTURE);
    }

    /**
     * 微信聊天界面发送图片消息
     */
    @JavascriptInterface
    public void getWechatPic() {
        Log.e(TAG, "getWechatPic: " + "\n" + "--------");
        Intent intent = new Intent(activity, PickOrTakeImageActivity.class);
        startActivityForResult(intent, RESULT_IMAGE);
        //        PhotoPicker.builder()
        //                .setPhotoCount(9)
        //                .setShowCamera(true)
        //                .setShowGif(true)
        //                .setPreviewEnabled(false)
        //                .start(WeChatActivity.this, PhotoPicker.REQUEST_CODE);

    }

    /**
     * 获取随机名字
     */
    @JavascriptInterface
    public String getName() {//获取随机名字
        Log.e(TAG, "getName: " + "\n" + "------getName----");
        Random random = new Random();
        int length = Constant.nameStr.length;
        int i = random.nextInt(length - 1);
        String name = Constant.nameStr[i];
        Log.e(TAG, "getName: " + "\n" + "--name--" + name);
        return name;
    }

    /**
     * 获取软键盘高度
     */
    @JavascriptInterface
    public void getKeyHeight() {//获取键盘高度
        getSoftHeight();
    }

    /**
     * 控制显示或隐藏软键盘
     */
    @JavascriptInterface
    public void hideOrShowSoftKb() {
        Log.e(TAG, "hideOrShowSoftKb: " + "\n" + "--vvvvvv--");
        //        mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mImm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_HIDDEN);
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    private int getScreenHeight() {
        int height = getWindowManager().getDefaultDisplay().getHeight();
        Log.e(TAG, "getScreenHeight: " + "\n" + height);
        return height;
    }

    /**
     * 获取随机图片
     *
     * @return
     */
    @JavascriptInterface
    public String getImg() {//获取随机图片
        Log.e(TAG, "getImg: " + "\n" + "-------getImg--------");
        int num = (int) (Math.random() * CommonUtil.randomNum);
        String picNum = "picture" + num;
        String path = CommonUtil.defaultPath + picNum + ".jpg";
        Log.e(TAG, "getImg: " + "\n" + "--path--" + path);
        return path;
    }

    /**
     * 获取   id
     *
     * @return
     */
    @JavascriptInterface
    public int getId() {
        int id = getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
        return id;
    }

    /**
     * 更新数据
     *
     * @param json
     * @param type
     * @param id
     */
    @JavascriptInterface
    public void update(String json, String type, int id) {
        int preId = getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
        if (id >= preId) {
            getSharedPreferences("config", MODE_PRIVATE).edit().putInt("id", id).commit();
        }
        Log.e(TAG, "update: " + "json = \n" + json);

        Data dataEntity = DaoManager.getInstance().CheckUpDataBase(activity).get(0);
        if (type.equals("new_friend_list")) {
            dataEntity.setNew_friend_list(json);
        } else if (type.equals("allfriend")) {
            dataEntity.setAllfriend(json);
        } else if (type.equals("my_info")) {
            User myInfo = mGson.fromJson(json, User.class);
            String my_info = mGson.toJson(myInfo);
            Log.e(TAG, "update333: " + "\n" + my_info);
            dataEntity.setMy_info(my_info);
        }
        dataDao.deleteAll();
        Log.e(TAG, "update: " + "dataEntity = \n" + mGson.toJson(dataEntity));
        dataDao.insertOrReplace(dataEntity);
        Log.e(TAG, "update: " + "\n" + dataDao.loadAll().size());
    }

    /**
     * 获取数据
     *
     * @return
     */
    @JavascriptInterface
    public String getData() {
        Log.e(TAG, "getData: " + "\n" + "222222");
        return mDatas;
    }

    /**
     * 添加20个好友
     *
     * @return
     */
    @JavascriptInterface
    public String get20() {
        int preId = getSharedPreferences("config", MODE_PRIVATE).getInt("id", 0);
        List<Data> datas = dataDao.loadAll();
        Data data = datas.get(0);
        String new_friend_list = data.getNew_friend_list();
        List<DefaultData.NewFriendListBean> newFriendList = mGson.fromJson(new_friend_list, new TypeToken<List<DefaultData.NewFriendListBean>>() {
        }.getType());
        if (newFriendList != null)
            newFriendList.clear();
        if (preId <= newFriendList.size()) {
            preId = newFriendList.size();
        }

        Random random = new Random();
        int length = Constant.nameStr.length;
        for (int i = 0; i < 20; i++) {
            int num = (int) (Math.random() * CommonUtil.randomNum);
            String picNum = "picture" + num;
            String path = CommonUtil.defaultPath + picNum + ".jpg";
            int j = random.nextInt(length - 1);
            String name = Constant.nameStr[j];
            DefaultData.NewFriendListBean newFriendListBean = new DefaultData.NewFriendListBean("我是" + name, false, name, -1, preId + i, path, 0, "", "2017-10-11 12:20", false, new ArrayList<DefaultData.ChatRecordBean>());
            newFriendList.add(newFriendListBean);
        }
        getSharedPreferences("config", MODE_PRIVATE).edit().putInt("id", preId + 20).commit();
        String newFriendstr = mGson.toJson(newFriendList);
        Log.e(TAG, "get20: " + "\n" + newFriendstr);
        return newFriendstr;
    }

    @JavascriptInterface
    public void log(String string) {
        Log.e(TAG, "log: " + "\n" + string);
    }

    /**
     * 返回
     */
    @JavascriptInterface
    public void back() {
        finish();
    }

    ArrayList<String> photos = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case CHOOSE_PICTURE://选择图片后
                        Uri uri = data.getData();
                        Intent intent = BitmapUtil.cutImage(uri); // 对图片进行裁剪处理
                        startActivityForResult(intent, CROP_SMALL_PICTURE);
                        break;
                    case CROP_SMALL_PICTURE://裁剪图片后

                        setImageToView(BitmapUtil.cutImgUri); // 让刚才选择裁剪得到的图片显示在界面上

                        break;

                    //                case PhotoPicker.REQUEST_CODE:
                    //                    if (data != null) {
                    //
                    //                        ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //                        //        compressWithLs(photos);
                    //                        compressWithRx(photos);
                    //                    }
                    //                    break;
                    case RESULT_IMAGE://获取图片集合后

                        photos.clear();
                        mImageList.clear();
                        String stringExtra = data.getStringExtra("data");
                        String imgPaths = stringExtra.substring(0, stringExtra.lastIndexOf("--"));
                        for (String s : imgPaths.split("--")) {
                            photos.add(s);
                        }
                        showProgress();
                        compressWithLs(photos, true);

                        break;
                    case GET_BG_IMG://选择聊天背景图片后
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                            mPath = BitmapUtil.saveFile(activity, "crop", bitmap);
                            photos.clear();
                            mImageList.clear();
                            photos.add(mPath);
                            showProgress();
                            compressWithLs(photos, false);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }

        } else {
            Log.e(TAG, "onActivityResult: " + "失败");
        }
    }


    /**
     * 压缩图片
     *
     * @param photos  图片路径集合
     * @param isReset 是否缩放
     */
    private void compressWithLs(final ArrayList<String> photos, final boolean isReset) {
        Luban.with(this)
                .load(photos)
                .setTargetDir(getPath())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                        Log.e(TAG, "onStart: " + "\n" + "开始压缩");
                    }

                    @Override
                    public void onSuccess(File file) {
                        if (isReset) {
                            Log.e(TAG, "onSuccess: " + "\n" + "缩放");
                            setWidthAndHeight(photos, file);
                        } else {
                            Log.e(TAG, "onSuccess: " + "\n" + "不缩放");
                            bgPath = file.getAbsolutePath();
                            mImageList.add(bgPath);
                        }
                        if (photos.size() == mImageList.size()) {
                            showResult(isReset);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        Log.e(TAG, "onError: " + "\n" + e.toString());
                    }
                }).launch();

    }



    /**
     * 显示结果
     */
    private void showResult(boolean isReset) {

        String compressPath = "";
        if (isReset) {
            Log.e(TAG, "showResult: " + "\n" + mImageList.size());
            for (int i = 0; i < mImageList.size(); i++) {
                compressPath += mImageList.get(i) + "--";
            }

            String subPathstr = compressPath.substring(0, compressPath.lastIndexOf("--"));
            Log.e(TAG, "showResult: " + "\n" + subPathstr);
            mWebView.loadUrl("Javascript:getPhoto('" + subPathstr + "')");

        } else {
            mWebView.loadUrl("Javascript:getBgPath('" + mImageList.get(0) + "')");
            Log.e(TAG, "onSuccess: " + "\n" + mImageList.get(0));
        }
        hideProgress();

    }


    /**
     * 缩放后的图片存放路径
     *
     * @return
     */
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/xiake/vscreenshot/zoom/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    /**
     * 设置图片显示宽高
     */
    int newWidth = 0;
    int newHeight = 0;
    float minWidth = 150;
    float minHeight = 150;
    float maxWidth = 400;
    float maxHeight = 400;
    float imgScale = 0;

    private void setWidthAndHeight(@NonNull List<String> list, File file) {
        float defScale = screenHeight / (screenWidth * 1.0f);
        Log.e(TAG, "accept: " + "\n" + list.size());
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width > height) {
            imgScale = width / (height * 1.0f);
        } else {
            imgScale = height / (width * 1.0f);
        }
        Log.e(TAG, "accept111: " + "\n" + width + "\n" + height + "\n" + defScale);
        if (imgScale >= 2.5) {
            newWidth = width > height ? 180 : 70;
            newHeight = width > height ? 80 : 180;
        } else {
            if (width > height) {
                if (width > maxWidth) {
                    Log.e(TAG, "accept: " + "\n" + "---111----");
                    newWidth = (int) ((maxWidth / 2) * 0.75f);
                    newHeight = (int) (((height / (width / maxWidth)) / 2) * 0.75f);
                } else if (width > minWidth) {
                    Log.e(TAG, "accept: " + "\n" + "---222----");
                    newWidth = (int) (width / 2);
                    newHeight = (int) (height / 2);
                } else {
                    Log.e(TAG, "accept: " + "\n" + "---333----");
                    newWidth = (int) width;
                    newHeight = (int) height;
                }
            } else {
                if (height > maxHeight) {
                    Log.e(TAG, "accept: " + "\n" + "---1111----");
                    newWidth = (int) (((width / (height / maxHeight)) / 2) * 0.75f);
                    newHeight = (int) ((maxHeight / 2) * 0.75f);
                } else if (height > minHeight) {
                    Log.e(TAG, "accept: " + "\n" + "---2222----");
                    newWidth = (int) (width / 2);
                    newHeight = (int) (height / 2);
                } else {
                    Log.e(TAG, "accept: " + "\n" + "---3333----");
                    newWidth = (int) width;
                    newHeight = (int) height;
                }
            }
        }
        Log.e(TAG, "accept222: " + "\n" + newWidth + "\n" + newHeight);
        mImageList.add(file.getAbsolutePath() + "," + newWidth + "," + newHeight);
    }

    private void hideProgress() {

        mPb.setVisibility(View.GONE);
    }

    private void showProgress() {

        mPb.setVisibility(View.VISIBLE);

    }


    private void setImageToView(Uri uri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            mPath = BitmapUtil.saveFile(activity, "crop", bitmap);
            //传给web
            mWebView.loadUrl("Javascript:getPath('" + mPath + "')");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "setImageToView: " + "\n" + e.toString());
        }

    }

    /**
     * 选择聊天背景图片
     *
     * @return
     */
    @JavascriptInterface
    public void getBgImg() {

        //获取相册中的图片
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, GET_BG_IMG);
//        return path;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_we_chat;
    }

    @Override
    public void onBackPressed() {
        //点击返回键
        hideProgress();
        mWebView.loadUrl("Javascript:back()");
        mTitle.setBackgroundColor(getResources().getColor(R.color.weChatGray));
    }


    View contentView = null;
    boolean isShow;

    @JavascriptInterface
    public void addFriend() {
        if (mPopupWindow != null) {
            if (isShow) {
                mPopupWindow.dismiss();
                isShow = false;
            } else {
                mPopupWindow.showAtLocation(contentView, Gravity.TOP, (screenWidth / 3) - 10, 215);
                isShow = true;
            }
        } else {
            createPopupWindow();
            mPopupWindow.showAtLocation(contentView, Gravity.TOP, (screenWidth / 3) - 10, 215);
            isShow = true;
        }
    }

    /**
     * 设置状态栏颜色
     */
    @JavascriptInterface
    public void setStatusBarColor(String string) {
        Log.e(TAG, "setStatusBarColor: " + "\n" + "---aaaa-----");
        if (string == null) {
            mTitle.setBackgroundColor(getResources().getColor(R.color.weChatGray));
        } else {
            if (string.equals("in")) {
                mTitle.setBackgroundColor(getResources().getColor(R.color.weChatGift));
            } else if (string.equals("out")) {
                mTitle.setBackgroundColor(getResources().getColor(R.color.weChatGray));
            }
        }
    }


    /**
     * 创建   PopupWindow
     */
    private void createPopupWindow() {
        contentView = LayoutInflater.from(WeChatActivity.this).inflate(R.layout.popuplayout, null);
        mPopupWindow = new PopupWindow(contentView);
        mPopupWindow.setWidth(400);
        mPopupWindow.setHeight(400);
        mPopupWindow.setOutsideTouchable(true);
        TextView addOne = (TextView) contentView.findViewById(R.id.popup_tx_1);
        TextView addTwenty = (TextView) contentView.findViewById(R.id.popup_tx_2);
        TextView clearAll = (TextView) contentView.findViewById(R.id.popup_tx_3);
        addOne.setOnClickListener(this);
        addTwenty.setOnClickListener(this);
        clearAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_tx_1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run: " + "\n" + "----1111---");
                        mWebView.loadUrl("Javascript:addOneFriend()");
                    }
                });

                mPopupWindow.dismiss();
                break;
            case R.id.popup_tx_2:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("Javascript:add20()");
                    }
                });

                mPopupWindow.dismiss();
                break;
            case R.id.popup_tx_3:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("Javascript:emptyNewFriendList()");
                    }
                });

                mPopupWindow.dismiss();
                break;
        }

    }
}
