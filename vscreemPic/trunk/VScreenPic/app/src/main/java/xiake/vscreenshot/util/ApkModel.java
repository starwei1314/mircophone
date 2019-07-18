package xiake.vscreenshot.util;

/**
 * Created by XuZY on 2017/4/1.
 */

public class ApkModel {
    private String id;
    private String appname;
    private String apkname;
    private String vername;
    private String vercode;
    private String ismandatoryupdate;

    @Override
    public String toString() {
        return "ApkModel{" +
                "id='" + id + '\'' +
                ", appname='" + appname + '\'' +
                ", apkname='" + apkname + '\'' +
                ", vername='" + vername + '\'' +
                ", vercode='" + vercode + '\'' +
                ", ismandatoryupdate='" + ismandatoryupdate + '\'' +
                ", UploadMemo='" + UploadMemo + '\'' +
                '}';
    }

    public String getUploadMemo() {
        return UploadMemo;
    }

    public void setUploadMemo(String uploadMemo) {
        UploadMemo = uploadMemo;
    }

    public String getIsmandatoryupdate() {
        return ismandatoryupdate;
    }

    public void setIsmandatoryupdate(String ismandatoryupdate) {
        this.ismandatoryupdate = ismandatoryupdate;
    }

    private String UploadMemo;

    public void ApkModel(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getApkname() {
        return apkname;
    }

    public void setApkname(String apkname) {
        this.apkname = apkname;
    }

    public String getVername() {
        return vername;
    }

    public void setVername(String vername) {
        this.vername = vername;
    }

    public String getVercode() {
        return vercode;
    }

    public void setVercode(String vercode) {
        this.vercode = vercode;
    }
}