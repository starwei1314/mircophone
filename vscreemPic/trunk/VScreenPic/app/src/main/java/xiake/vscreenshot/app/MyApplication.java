package xiake.vscreenshot.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import xiake.vscreenshot.db.DaoManager;
import xiake.vscreenshot.util.MyHelper;


public class MyApplication extends Application {
    final String path = "/sdcard/Vscreenshot/screenshot";
    private static MyApplication instance;
    public static int barHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        /*全局异常*/
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        DaoManager.getInstance().initManager(getApplicationContext());
        MyHelper.createFile2(path);
        barHeight = getStatusBarHeight(getApplicationContext());
//        Log.e("===========", "onCreate: " + "\n" +barHeight);
    }
    //获得状态栏的高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }
    public static Context getInstance() {
        return instance;
    }
}
