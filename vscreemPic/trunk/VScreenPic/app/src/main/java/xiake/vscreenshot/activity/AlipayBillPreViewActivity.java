package xiake.vscreenshot.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import xiake.db.entity.Bill;
import xiake.vscreenshot.R;
import xiake.vscreenshot.bean.BillDetails;
import xiake.vscreenshot.bean.BillHtml;
import xiake.vscreenshot.util.ShakeUtils;
import xiake.vscreenshot.util.ShellUtils;
import xiake.vscreenshot.util.ToastUtil;

import static xiake.vscreenshot.R.id.bar;
import static xiake.vscreenshot.app.MyApplication.barHeight;
import static xiake.vscreenshot.util.CommonUtil.SHOT_TIME;

/**
 * Created by Administrator on 2017/10/13 0013.
 *
 * 支付宝账单
 */
public class AlipayBillPreViewActivity extends BaseActivity{
    private static final String TAG = "AlipayBill";
    public WebView mWebView;
    public String mBillsJson;
    public ShakeUtils mShakeUtils;
    public RelativeLayout mSc_rl;
    public ImageView mScreenshotImg;

    @Override
    protected void setTitle() {
        ((TextView) findViewById(bar)).setHeight(barHeight);
    }

    @Override
    protected void initView() {
        List<BillHtml> billHtmls = new ArrayList<>();
        Intent intent = getIntent();
        Gson gson = new Gson();
        mShakeUtils = new ShakeUtils(this);
        InitScreenshot();
        String billsExtra = intent.getStringExtra("billsJson");
        List<Bill> bills = gson.fromJson(billsExtra, new TypeToken<List<Bill>>(){}.getType());
        for (Bill bill : bills) {
            String time = bill.getTime();
            List<BillDetails> billDetailses = gson.fromJson(bill.getDetails(),new TypeToken<List<BillDetails>>(){}.getType());
            billHtmls.add(new BillHtml(time,billDetailses));
        }
        mBillsJson = gson.toJson(billHtmls);
        Log.e("----aaaa----", "initView: " + "\n" +mBillsJson);

        mSc_rl = ((RelativeLayout) findViewById(R.id.sc_rl));
        mScreenshotImg = ((ImageView) findViewById(R.id.screenshot_img));
        mWebView = ((WebView) findViewById(R.id.alipay_bill_pre_webview));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/billing_list.html");
        mWebView.addJavascriptInterface(this,"transfer");
//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Log.e("++++", "onPageFinished: " + "\n" +mBillsJson);
//                mWebView.loadUrl("Javascript:getBilling('"+mBillsJson+"')");
//                Log.e("======", "onPageFinished: " + "\n" +"========");
//            }
//        });
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
                        if (curTimeMillis-preTimeMillis>SHOT_TIME){//限制频率
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
    @JavascriptInterface
    public void log(String str){
        Log.e("---log---", "log: " + "\n" +str);
    }
    @JavascriptInterface
    public void back(){
        finish();
    }
    @JavascriptInterface
    public String getBill(){
        return mBillsJson;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_alipay_bill_preview;
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
}
