package xiake.vscreenshot.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class AddFriend implements Serializable{
    private String name;
    private String imgPath;
    private String remark;

    public AddFriend(String name, String imgPath, String remark) {
        this.name = name;
        this.imgPath = imgPath;
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AddFriend{" +
                "name='" + name + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", freindRemark='" + remark + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
