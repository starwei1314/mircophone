package xiake.vscreenshot.util;

/**
 * 接口常量类
 * Created by Administrator on 2017/5/3.
 */

public  class Api {
    //正式的
    private static String BASE_HOST = "http://weipinzhan.net";


    /**
     * 版本和apk
     */
    public static String UPDATE = BASE_HOST + "//UpdateManager.aspx?";

    //验证手机
    //http://weipinzhan.net//UpdateManager.aspx?action=checkphonemodel&imei=359970063654587&sign=1b90da9c0367f0e69560e70c347cc690


    public static String ABOUT_ME=BASE_HOST+"/AboutMe.aspx";

    public static String USER_INSTRO=BASE_HOST+"/UseManual.aspx";
    public static String BASE_UPDATA = BASE_HOST + ":80";
    public static String APP_INFORMATION = BASE_UPDATA + "//UpdateManager.aspx?";
    public static String APK_URL = "http://weipinzhan.net//UpdateManager.aspx?action=getapk&appkey=vjt";
    /*测试用的url*/
    //    public static String APK_URL = "http://172.16.6.50:1013/vjt.apk";
    public static String VERSION_URL = "http://weipinzhan.net//UpdateManager.aspx?action=getversion&appkey=vjt";
    public static String NET_FILTER = "android.net.conn.CONNECTIVITY_CHANGE";
    public static String BANNER_URL = "http://www.weipinzhan.net:90/interface/UpDownInterface.ashx?action=GetALLImgJson&type=vjt";
}
