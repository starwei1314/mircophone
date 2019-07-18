package xiake.vscreenshot.bean;

/**
 * ---Created by zsl on 2018/1/18.---
 */

public class Banner {

    /**
     * FileID : 17
     * FileName : e4d270d9-1e89-40b0-b829-96af3debe24d.png
     * FilePath : ..\Images\2018-01-18\e4d270d9-1e89-40b0-b829-96af3debe24d.png
     * FileSize : 13770
     * OrderID : "0"
     * RequestURL : null
     * SkipURL:"http://m.vsj.cn/dllc.php",
     * Type : vjt
     */

    private int FileID;
    private String FileName;
    private String FilePath;
    private int FileSize;
    private int OrderID;
    private String RequestURL;
    private String SkipURL;
    private String Type;

    public int getFileID() {
        return FileID;
    }

    public void setFileID(int FileID) {
        this.FileID = FileID;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

    public int getFileSize() {
        return FileSize;
    }

    public void setFileSize(int FileSize) {
        this.FileSize = FileSize;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public String getRequestURL() {
        return RequestURL;
    }

    public void setRequestURL(String RequestURL) {
        this.RequestURL = RequestURL;
    }

    public String getSkipURL() {
        return SkipURL;
    }

    public void setSkipURL(String skipURL) {
        SkipURL = skipURL;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "FileID=" + FileID +
                ", FileName='" + FileName + '\'' +
                ", FilePath='" + FilePath + '\'' +
                ", FileSize=" + FileSize +
                ", OrderID=" + OrderID +
                ", RequestURL='" + RequestURL + '\'' +
                ", SkipURL='" + SkipURL + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
