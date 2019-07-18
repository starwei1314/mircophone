package xiake.vscreenshot.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import xiake.vscreenshot.R;
import xiake.vscreenshot.bean.Verify;
import xiake.vscreenshot.util.Api;
import xiake.vscreenshot.util.MyHelper;
import xiake.vscreenshot.util.PhoneInfo;
import xiake.vscreenshot.util.ToastUtil;
import xiake.vscreenshot.util.httpUtil;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int REQUEST_PERMISSIONS = 1;
    private int connectedType;
    PhoneInfo phoneInfo;
    private CheckBox checkBox;
    private Button button;
    private boolean mIsFirst;
    private static String[] PERMISSIONS_NEEDED = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    public String mPhoneIMEI;
    public String mSign;
    private NetReceiver mNetReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mIsFirst = getSharedPreferences("MyConfig", 0).getBoolean("FIRST", true);
        registReceiver();
        phoneInfo = PhoneInfo.getInstance(SplashActivity.this);
        checkPhoneModel();
        checkPermission(PERMISSIONS_NEEDED);
        RelativeLayout rl =  findViewById(R.id.splash_rl);
        if (mIsFirst) {
            rl.setVisibility(View.VISIBLE);
            init();
        } else {
            rl.setVisibility(View.GONE);
        }

    }

    private void registReceiver() {
        mNetReceiver = new NetReceiver();
        IntentFilter intentFilter = new IntentFilter(Api.NET_FILTER);
        registerReceiver(mNetReceiver, intentFilter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetReceiver);
    }

    /**
     * 申请权限
     *
     * @param permissionsNeeds  权限
     */
    private void checkPermission(String[] permissionsNeeds) {
        for (String permissionsNeed : permissionsNeeds) {
            if (ActivityCompat.checkSelfPermission(this, permissionsNeed) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {permissionsNeed};
                ActivityCompat.requestPermissions(this, permission, REQUEST_PERMISSIONS);
            }
        }
    }


    private void init() {
        checkBox =  findViewById(R.id.checkBox);
        button =  findViewById(R.id.button);
        TextView textView =  findViewById(R.id.alipay_name);

        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, UserAgreementActivity.class));
            }
        });

        checkBox.setChecked(true);
        Resources resources = button.getResources();
        button.setTextColor(checkBox.isChecked() ? resources.getColor(R.color.ty) : resources.getColor(R.color.grayLine));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (checkNet()) {
                        checkPhone(mPhoneIMEI, mSign);
                    }
                } else {
                    ToastUtil.show(SplashActivity.this, "未同意用户协议,无法使用本软件!", 1);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                Resources resources = button.getResources();
                button.setTextColor(isChecked ? resources.getColor(R.color.ty) : resources.getColor(R.color.grayLine));
                button.setBackgroundResource(isChecked ? R.drawable.ty : R.drawable.bty);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 判断微信是否登陆，没有就跳转到微信登陆界面（打开微信）
     * 利用Xposed 获取我的昵称，如果为空，则说明未登录？
     * 已经登陆就进入主界面
     */
    //授权验证判断是否微手机
    private void checkPhoneModel() {
        String phoneFactory = phoneInfo.getTYPE();
        String phoneTYB = phoneInfo.getTYB();
        mPhoneIMEI = phoneInfo.getIMEI();
        String phoneCPU = phoneInfo.getCpuInfo();
        String signkey = "xiake2017";
        String key = phoneFactory + phoneTYB + phoneCPU + signkey;
        mSign = MyHelper.MD5(key);
        Log.e(TAG, "checkPhoneModel: " + "\nphoneFactory=" + phoneFactory + "\nmPhoneIMEI=" + mPhoneIMEI + "\nphoneCPU=" + phoneCPU + "\nmSign=" + mSign);
    }

    private boolean checkNet() {
        connectedType = httpUtil.getConnectedType(this);
        if (connectedType == 0) {
            //流量
            ToastUtil.showShort(this, "移动网络状态，请注意费用哦~");
            return true;
        } else if (connectedType == 1) {
            //wifi
            ToastUtil.showShort(this, "WiFi网络状态，放心使用~");
            return true;
        } else if (connectedType == -1) {
            //没有联网
            ToastUtil.showShort(this, "请打开网络");
            return false;
        }
        return false;
    }

    int errorcount = 0;

    private void checkPhone(String phoneIMEI, String sign) {
        OkHttpUtils
                .get()
                .url(Api.UPDATE)
                .addParams("action", "checkphonemodel")
                .addParams("imei", phoneIMEI)
                .addParams("sign", sign)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                        if (!checkNet()){
                            ToastUtil.showShort(SplashActivity.this,"请打开网络");
                            return;
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Verify result;
                        try {
                            result = new Gson().fromJson(response, new TypeToken<Verify>() {
                            }.getType());
                            String errcontext = result.getErrcontext();
                            int resultId = result.getId();
//                            if (errcontext.contains("验证成功") && resultId == 0) {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
//                            } else {
//                                ToastUtil.showShort(SplashActivity.this, "此软件只用于微手机,请到官网www.vsj.cn购买正版微手机");
//                            }
                        } catch (JsonParseException e) {
                            ToastUtil.showShort(SplashActivity.this, "此软件只用于微手机,请到官网www.vsj.cn购买正版微手机");
                        }

                    }

                });
    }

    public class NetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onNetChange();
        }

        public void onNetChange() {
            if (checkNet()) {
                if (!mIsFirst)
                    checkPhone(mPhoneIMEI, mSign);
            }
        }
    }
}
