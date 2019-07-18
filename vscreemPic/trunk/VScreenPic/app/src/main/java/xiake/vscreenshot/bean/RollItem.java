package xiake.vscreenshot.bean;

/**
 * Created by Administrator on 2016/11/4.
 */
public class RollItem {
    private String imageUrl;//图片路径
    private String skipUrl;//跳转路径

    public RollItem(String imageUrl, String skipUrl) {
        this.imageUrl = imageUrl;
        this.skipUrl = skipUrl;
    }
    public RollItem(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSkipUrl() {
        return skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

    @Override
    public String toString() {
        return "RollItem{" +
                "imageUrl='" + imageUrl + '\'' +
                ", skipUrl='" + skipUrl + '\'' +
                '}';
    }
}
