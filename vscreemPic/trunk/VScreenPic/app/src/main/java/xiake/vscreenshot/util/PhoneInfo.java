package xiake.vscreenshot.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.TELEPHONY_SERVICE;


/**
 * 获取手机信息
 * Created by XuZY on 2017/4/1.
 */


public class PhoneInfo {

    static Context mContext;
    private static PhoneInfo instance;

    public static PhoneInfo getInstance(Context context) {
        if (instance == null) {
            instance = new PhoneInfo(context);
        }
        return instance;
    }

    public PhoneInfo(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 获取版本code
     *
     * @return
     */
    public int getVerCode() {
        int verCode = -1;
        try {
            verCode = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public String getVerName() {
        String verName = "";
        try {
            verName = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;
    }

    /**
     * 获取手机是否root信息    * @return
     */
    public String isRoot() {
        String bool = "Root:false";
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = "Root:false";
            } else {
                bool = "Root:true";
            }
        } catch (Exception e) {
        }
        return bool;
    }

    /**
     * 获取IMEI号，，手机型号
     */
    public String getIMEI() {
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }
        String imei = mTm.getDeviceId();
        return imei;
    }

    /**
     * 获取IESI号
     *
     * @return
     */
    private String getIESI() {
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }
        String imsi = mTm.getSubscriberId();
        return imsi;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public String getTYPE() {
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        String mtype = android.os.Build.MODEL;
        return mtype;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public String getTYB() {
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        String mtyb = android.os.Build.BRAND;
        return mtyb;
    }

    /**
     * 获取手机号码
     *
     * @return
     */
    public String getPhoneNumber() {
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }
        return mTm.getLine1Number();
    }


    /**
     * 读取文件保存的 cpu 信息
     * @return
     */
    public String getCpuInfo() {
        // 文件路径
        String str1 = "/proc/cpuinfo";
        String[] cpuInfo = {"", ""}; //1-cpu型号 //2-cpu频率
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);

            // 循环读取文件全部信息
            Map<String, String> infoMap = new HashMap<>();
            while (true) {
                String info = localBufferedReader.readLine();
                if (TextUtils.isEmpty(info)) break;
                if (info.contains(":")) {
                    String[] kv = info.split(":");
                    infoMap.put(kv[0].trim(), kv[1].trim());
                }
            }
            // 获取 Processor 与 CPU part 两个作为加密参数
            cpuInfo[0] = infoMap.get("Processor").trim();
            String cpu = infoMap.get("CPU part").trim();
            if (cpu.contains("(") && cpu.contains(")")) {
                cpuInfo[1] = cpu.substring(cpu.indexOf("(") + 1, cpu.indexOf(")"));
            } else {
                cpuInfo[1] = cpu;
            }

            localBufferedReader.close();
        } catch (IOException e) {
            // 异常 两个参数为空串
        }

        return "Processor:" + cpuInfo[0] + "\nCPU part:" + cpuInfo[1];
    }

}
