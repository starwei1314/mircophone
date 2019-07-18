package xiake.vscreenshot.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import xiake.vscreenshot.R;
import xiake.vscreenshot.adapter.RecyclerViewAdapter;
import xiake.vscreenshot.adapter.RvAdapter;
import xiake.vscreenshot.bean.Banner;
import xiake.vscreenshot.bean.RollItem;
import xiake.vscreenshot.db.DaoManager;
import xiake.vscreenshot.util.Api;
import xiake.vscreenshot.util.ApkModel;
import xiake.vscreenshot.util.DataCleanManager;
import xiake.vscreenshot.util.MessageResult;
import xiake.vscreenshot.util.MyHelper;
import xiake.vscreenshot.util.PhoneInfo;
import xiake.vscreenshot.util.SpaceItemDecoration;
import xiake.vscreenshot.util.ToastUtil;
import xiake.vscreenshot.util.httpUtil;
import xiake.vscreenshot.widget.AutoRollLayout;

import static xiake.vscreenshot.util.JSONHelper.parseObject;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private PhoneInfo apkUpdate;
    private int serverCode;
    private File file = null;
    private String TITLE = "title";
    private ProgressDialog progressDialog;
    private String[] textRes = {"微信模拟器", "微信转账", "微信红包", "微信零钱", "新的朋友",
            "我的钱包", "支付宝余额", "支付宝账单", "支付宝转账"};
    private Integer[] imageRes = {R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4,
            R.mipmap.icon_5, R.mipmap.icon_6, R.mipmap.icon_7, R.mipmap.icon_8, R.mipmap.icon_9,};
    private List<RollItem> mRollItems = new ArrayList<>();
    public AutoRollLayout mAutoRollLayout;
    Gson gson = new Gson();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void setTitle() {
        getSharedPreferences("MyConfig", 0).edit().putBoolean("FIRST", false).commit();
    }

    @Override
    protected void initView() {
        progressDialog = getProgessDialog();
        DaoManager.getInstance().CheckUpDataBase(activity);
        apkUpdate = PhoneInfo.getInstance(this);
        checkConnection();
//        checkVersion();
        initBanner();
        mAutoRollLayout = (AutoRollLayout) findViewById(R.id.autoRollLayout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(1));
        RvAdapter adapter = new RvAdapter(this, textRes, imageRes);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerViewAdapter.ClickableViewHolder holder) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, WeChatActivity.class);
                        intent.putExtra(TITLE, textRes[0]);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, WeChatTransferActivity.class);
                        intent.putExtra(TITLE, textRes[1]);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, WeChatGiftActivity.class);
                        intent.putExtra(TITLE, textRes[2]);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, WeChatSmallMoneyActivity.class);
                        intent.putExtra(TITLE, textRes[3]);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, NewFriendActivity.class);
                        intent.putExtra(TITLE, textRes[4]);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, MyWalletActivity.class);
                        intent.putExtra(TITLE, textRes[5]);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, AlipayMoneyActivity.class);
                        intent.putExtra(TITLE, textRes[6]);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this, AlipayBillActivity.class);
                        intent.putExtra(TITLE, textRes[7]);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(MainActivity.this, AlipayTransferActivity.class);
                        intent.putExtra(TITLE, textRes[8]);
                        startActivity(intent);
                        break;

                }
            }
        });

    }
    
    /**
     * 首页轮播图
     */
    private void initBanner() {
        OkHttpUtils
                .get()
                .url(Api.BANNER_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        Log.e(TAG, "onError: \n" + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e(TAG, "onResponse: \n" + response);
                        List<Banner> banners = gson.fromJson(response, new TypeToken<List<Banner>>() {
                        }.getType());
                        for (Banner banner : banners) {
                            String requestURL = banner.getRequestURL();
                            String skipURL = banner.getSkipURL();
                            if (requestURL != null && requestURL.length() > 0&&skipURL != null && skipURL.length() > 0) {
                                mRollItems.add(new RollItem(requestURL, skipURL));
                            }
                        }

                        mAutoRollLayout.setRollItems(mRollItems);
                        mAutoRollLayout.setOnItemsChoseListener(new AutoRollLayout.OnItemsChoseListener() {
                            @Override
                            public void chooseItems(int position) {
                                String skipUrl = mRollItems.get(position).getSkipUrl();
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri uri = Uri.parse(skipUrl);
                                intent.setData(uri);
                                try{
                                    startActivity(intent);
                                }catch (RuntimeException e){
                                    ToastUtil.showShort(MainActivity.this,"该图片无跳转链接");
                                    Log.e(TAG, "chooseItems: "+e.toString() );
                                }

                            }
                        });
                    }
                });

    }


    /**
     * 检查网络状态
     */
    private void checkConnection() {
        int connectedType = httpUtil.getConnectedType(this);
//        Log.e(TAG, "checkConnection: " + "\n" + connectedType);
        if (connectedType == 0) {
            //流量
            ToastUtil.show(MainActivity.this, "移动网络状态，请注意费用哦~", 1);
        } else if (connectedType == 1) {
            //wifi
            ToastUtil.show(MainActivity.this, "WiFi网络状态，放心使用~", 1);
        } else if (connectedType == -1) {
            //没有联网
            ToastUtil.show(MainActivity.this, "请打开网络", 1);
        }

    }

    int connectCount = 0;

    /**
     * 检查版本
     */
    private void checkVersion() {

        //获取版本号
        OkHttpUtils
                .get()
                .url(Api.VERSION_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        connectCount++;
//                        Log.e(TAG, "onError: " + "\n" + e.toString());
                        if (connectCount < 5) checkVersion();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            MessageResult messageResult = parseObject(response, MessageResult.class);
                            if (messageResult.getId() == 0) {
                                String context = messageResult.getContext();
//                                Log.e(TAG, "onResponse: " + "\n" + context);
                                ApkModel apkmnodel = parseObject(context, ApkModel.class);
                                serverCode = Integer.parseInt(apkmnodel.getVercode());
                                if (Integer.parseInt(apkmnodel.getVercode()) > apkUpdate.getVerCode()) {
                                    int ismandatoryupdate = Integer.parseInt(apkmnodel.getIsmandatoryupdate());
                                    String uploadMemo = apkmnodel.getUploadMemo();
                                    if (ismandatoryupdate == 0) {
                                        //正常更新
                                        showUpdateDialog(apkUpdate.getVerName(), apkmnodel.getVername(), uploadMemo);
                                    } else {
                                        //强制更新
                                        showMandatoryUpdateDialog(apkUpdate.getVerName(), apkmnodel.getVername(), uploadMemo);
                                    }
                                } else {
                                    ToastUtil.show(MainActivity.this, "已经是最新版", 1);
                                }
                            } else {
//                                ToastUtil.show(MainActivity.this, messageResult.getErrcontent(), 1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    /**
     * 强制更新
     *
     * @param locakName
     * @param serverName
     * @param uploadMemo
     */
    private void showMandatoryUpdateDialog(String locakName, final String serverName, String uploadMemo) {
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setTitle("更新提示")
                .setMessage("当前版本:" + locakName + "\n" + "最新版本:" + serverName + "\n" + "更新内容:" + uploadMemo)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getApk(serverName);
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    /**
     * 正常更新
     *
     * @param locakName
     * @param serverName
     */
    private void showUpdateDialog(String locakName, final String serverName, String uploadMemo) {
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setTitle("更新提示")
                .setMessage("当前版本:" + locakName + "\n" + "最新版本:" + serverName + "\n" + "更新内容:\n" + uploadMemo)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getApk(serverName);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 下载apk
     *
     * @param serverName 服务器上 app版本
     */
    private void getApk(String serverName) {
        MyHelper.deleteSDFile("vjt" + serverName + ".apk");
        try {
            file = MyHelper.createSDFile("vjt" + serverName + ".apk");
//            Log.e("=====", file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .get()
                .url(Api.APK_URL)
                .build()
                .execute(new FileCallBack(MyHelper.getFilePath(), file.getName()) {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

//                        Log.e(TAG, "onError: " + "\n" + e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        progressDialog.dismiss();
                        ToastUtil.show(MainActivity.this, "下载完成", 1);
                        installApk(response);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
//                        Log.e(TAG, "inProgress: " + "\n" + progress);
                        if (progress == total) {
                            progressDialog.dismiss();
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.setProgress((int) (progress * 100));
                            } else {
                                progressDialog.show();
                                progressDialog.setProgress((int) (progress * 100));
                            }

                        }

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAutoRollLayout.setAutoRoll(false, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置5秒转一次
        mAutoRollLayout.setAutoRoll(true, 5000);
    }


    /**
     * 安装apk
     *
     * @param response
     */
    private void installApk(File response) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(response), "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    /**
     * 显示下载进度条
     */
    private ProgressDialog getProgessDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("下载中，请稍后......");
        }
        return progressDialog;
    }

    //点击两次退出程序
    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - exitTime >= 1000) {
            ToastUtil.show(this, "连按两次退出", 1);
            exitTime = time;
        } else {
            finish();
        }

    }

}
