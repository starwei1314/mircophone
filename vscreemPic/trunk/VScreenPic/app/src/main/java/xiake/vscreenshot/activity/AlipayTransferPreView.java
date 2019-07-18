package xiake.vscreenshot.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import xiake.vscreenshot.R;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.ShakeUtils;
import xiake.vscreenshot.util.ShellUtils;
import xiake.vscreenshot.util.ToastUtil;

import static xiake.vscreenshot.app.MyApplication.barHeight;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class AlipayTransferPreView extends BaseActivity{
    private static final String TAG = "Alipay";
    public WebView mWebView;
    public String payStatus;
    public ShakeUtils mShakeUtils;
    public String friendNumber;
    public RelativeLayout mSc_rl;
    public ImageView mScreenshotImg;

    @Override
    protected void setTitle() {
        ((TextView) findViewById(R.id.bar)).setHeight(barHeight);
    }

    @Override
    protected void initView() {
        mShakeUtils = new ShakeUtils(activity);
        InitScreenshot();
        Gson gson = new Gson();
        Intent intent = getIntent();
        final ArrayList<String> list = intent.getStringArrayListExtra("list");
        String friendJson = list.get(0);
        String status = list.get(5);
        Log.e(TAG, "initView: " + "\n" +status);
        if (status.equals("交易成功")){
            payStatus = "交易成功";
        }else {
            payStatus = "交易失败";
        }
        Log.e(TAG, "initView: " + "\n" +payStatus);
//        Log.e("=====", "initView: " + "\n" +status+ "\n" +payStatus+"\n"+isShowRecord);
        final List<String> friend = gson.fromJson(friendJson,new TypeToken<List<String>>(){}.getType());
        String number = friend.get(2);
        if (number.contains("@")){
            //邮箱
            String subHead = number.substring(0, 3);
            String subLast = number.substring(number.lastIndexOf("@"));
            friendNumber = subHead+"***"+subLast;
        } else {
            //手机号
            String subHead = number.substring(0, 3);
            String subLast = number.substring(number.length() - 2);
            friendNumber = subHead+"******"+subLast;

        }

        mSc_rl = ((RelativeLayout) findViewById(R.id.sc_rl));
        mScreenshotImg = ((ImageView) findViewById(R.id.screenshot_img));
        mWebView = ((WebView) findViewById(R.id.alipay_transfer_pre_web));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/billing_details.html");
        mWebView.addJavascriptInterface(this,"alipay");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //friend:昵称，图片地址，帐号，会员等级
                //list:friend，收支类型，付款金额，付款说明，交易时间，交易状态，订单号，付款类型
                mWebView.loadUrl("Javascript:initPage('"+friend.get(0)+"','"+friend.get(1)+"','"+friend.get(3)+"','"+list.get(2)+"'," + "'"+ payStatus +"','"+list.get(1)+"','"+list.get(7)+"','"+list.get(3)+"','"+friendNumber+"','"+list.get(4)+"','"+list.get(6)+"','"+list.get(8)+"')");
            }
        });

    }

    Handler mHandler = new Handler();
    long preTimeMillis = 0;
    String path = "";
    ShellUtils.CommandResult one = null;
    private void InitScreenshot() {
        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         */
        final Vibrator mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        mShakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long curTimeMillis = System.currentTimeMillis();
                        if (curTimeMillis-preTimeMillis> CommonUtil.SHOT_TIME){//限制频率
                            preTimeMillis = curTimeMillis;
                            String time = String.valueOf(curTimeMillis);
                            Log.e(TAG, "run: " + "\n" +time);
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
                                        },1500);

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
        super.onDestroy();
        mWebView.destroy();
    }

    @JavascriptInterface
    public void log(String state){
        Log.e(TAG, "log: " + "\n" +state);
    }
    @JavascriptInterface
    public void back(){
        finish();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.ali_transfer_pre;
    }


}
