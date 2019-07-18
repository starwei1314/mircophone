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

import java.util.Date;

import xiake.vscreenshot.R;
import xiake.vscreenshot.util.CommonUtil;
import xiake.vscreenshot.util.ShakeUtils;
import xiake.vscreenshot.util.ShellUtils;
import xiake.vscreenshot.util.ToastUtil;

import static xiake.vscreenshot.app.MyApplication.barHeight;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class MyWalletPreviewActivity extends BaseActivity{
    private static final String TAG = "MyWalletPreviewActivity";
    public WebView mWebView;
    ShakeUtils mShakeUtils;

    private Vibrator mVibrator;  //声明一个振动器对象
    private String mMoney;
    public RelativeLayout mSc_rl;
    public ImageView mScreenshotImg;
    Date dt= new Date();

    @Override
    protected void setTitle() {
        ((TextView) findViewById(R.id.bar)).setHeight(barHeight);
    }

    @Override
    protected void initView() {
        mShakeUtils = new ShakeUtils(this);
        Intent intent = getIntent();
        mMoney = intent.getStringExtra("money");
        if (mMoney.startsWith(".")){
            mMoney = "0"+ mMoney;
        }
        mSc_rl = ((RelativeLayout) findViewById(R.id.sc_rl));
        mScreenshotImg = ((ImageView) findViewById(R.id.screenshot_img));
        mWebView = ((WebView) findViewById(R.id.my_wallet_preview_webView));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this,"app");
        mWebView.loadUrl("file:///android_asset/my_wallet.html");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.loadUrl("Javascript:initMoney('"+ mMoney +"')");
            }
        });

        InitScreenshot();
    }
    @JavascriptInterface
    public void back(){
        Log.e(TAG, "back: " + "\n" +"diao----");
        finish();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_wallet_preview;
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
    protected void onPause(){
        super.onPause();
        mShakeUtils.onPause( );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }
}
